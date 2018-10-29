package com.trackray.rest.service.impl;

import com.trackray.rest.dao.FingerDTOMapper;
import com.trackray.rest.dao.TaskDTOMapper;
import com.trackray.rest.dao.VulnDTOMapper;
import com.trackray.rest.dto.*;
import com.trackray.rest.module.TaskData;
import com.trackray.rest.query.TaskQuery;
import com.trackray.rest.query.VulnQuery;
import com.trackray.rest.service.TaskService;
import com.trackray.scanner.attack.FuzzDir;
import com.trackray.scanner.attack.FuzzDomain;
import com.trackray.scanner.attack.Nmap;
import com.trackray.scanner.bean.*;
import com.trackray.scanner.controller.DispatchController;
import com.trackray.scanner.entity.FingerEntity;
import com.trackray.scanner.enums.Finger;
import com.trackray.scanner.handle.CoreThreadMap;
import com.trackray.scanner.handle.SystemInit;
import com.trackray.scanner.httpclient.HttpClient;
import com.trackray.scanner.httpclient.ResponseStatus;
import com.trackray.scanner.utils.CheckUtils;
import com.trackray.scanner.utils.SysLog;
import com.trackray.scanner.utils.TaskUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private DispatchController dispatchController;
    @Autowired
    private CoreThreadMap coreThreadPool;
    @Autowired
    private TaskDTOMapper taskDTOMapper;
    @Autowired
    private VulnDTOMapper vulnDTOMapper;
    @Autowired
    private FingerDTOMapper fingerDTOMapper;

    @Override
    public ResultCode createTask(TaskQuery query) {
        TaskDTOWithBLOBs dto = new TaskDTOWithBLOBs();

        if (query.getRule() ==null){
            query.setRule(new Rule());
        }


        if (CheckUtils.isJson(query.getProxy())){
            dto.setProxy(query.getProxy());
        }
        String key = TaskUtils.genTaskKey(query.getTarget());
        dto.setTaskMd5(key);

        if (StringUtils.isNotBlank(query.getCookie()))
            dto.setCookie(query.getCookie());

        if (StringUtils.isNotBlank(query.getName()))
            dto.setTaskName(query.getName());

        JSONObject proxyObj = new JSONObject();
        if(StringUtils.isNotBlank(query.getProxy())){
            if(query.getProxy().matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+(,\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)*")) {
                if (query.getProxy().contains(",")){
                for (String ip : query.getProxy().split(",")) {
                    String[] proxy = ip.split(":");
                    proxyObj.put(proxy[0],proxy[1]);
                }
                }else {
                    String[] proxy = query.getProxy().split(":");
                    proxyObj.put(proxy[0],proxy[1]);
                }
            }
        }

        JSONObject rule = JSONObject.fromObject(query.getRule());
        dto.setRule(rule.toString());
        if (!proxyObj.isEmpty())
            dto.setProxy(proxyObj.toString());
        dto.setTaskName(query.getName());
        dto.setTarget(query.getTarget());
        dto.setThread(query.getThread());
        dto.setSpiderDeep(query.getSpiderDeep());
        dto.setSpiderMax(query.getSpiderMax());
        dto.setTimeMax(query.getTimeMax());

        int result = taskDTOMapper.insertSelective(dto);

        if (result<1)
            return ResultCode.ERROR;

        return ResultCode.getInstance(200,"成功",key);
    }

    @Override
    public boolean startTask(String taskMd5) {
        List<TaskDTOWithBLOBs> tasks = this.selectTaskByMd5(taskMd5);
        if (tasks.size()!=1){
            return false;
        }
        TaskDTOWithBLOBs taskDTO = tasks.get(0);

        Task task = this.initTaskFromDTO(taskDTO);

        dispatchController.init(task);

        ThreadPoolExecutor exec = new ThreadPoolExecutor(task.getThreadPool(), task.getThreadPool() + 30, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        task.setExecutor(exec);

        this.saveData(task , 1);
        ResultCode code = dispatchController.dispatch(task);

        String taskMD5 = task.getTaskMD5();

        if (code.getCode() != 200) {
            return false;
        }
        HashMap<String,Object> value = new HashMap<String,Object>();
        value.put("create_time",System.currentTimeMillis());
        value.put("thread_pool",exec);
        value.put("task",task);
        coreThreadPool.put(taskMD5,value);
        this.scanTargets(task);

        return true;
    }

    private void scanTargets(Task task) {
        ThreadPoolExecutor exec = (ThreadPoolExecutor) task.getExecutor();
        while (exec.getTaskCount() != exec.getCompletedTaskCount()){
            //...
        }
        SysLog.info("子域名扫描结束");
        for (String target : task.getTargets()) {

            if ( task.getRule().crawler){
                dispatchController.crawler(task , target , exec);
            }
            if(task.getRule().port){
                Nmap.nmap(task , target ,  exec);
            }
            if (task.getRule().finger){
                this.fingerScan(task , target , exec);
            }
            if (task.getRule().fuzzdir){
                FuzzDir.fuzz(task , target ,exec);
            }

        }

        if (task.getRule().attack){
            dispatchController.attack(task,exec);
        }

    }

    /**
     * 暂时先这么写
     * @param task
     * @param target
     * @param exec
     */
    private void fingerScan(Task task, String target, ThreadPoolExecutor exec) {
        List<FingerDTO> fingers = fingerDTOMapper.selectByExample(new FingerDTOExample());

        for (FingerDTO finger : fingers) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (task.getResult().getItems().get(target).getSystemInfo().getFinger()!=null)
                        return;

                    String url = finger.getUrl();
                    ResponseStatus content = null;
                    HttpClient httpClient = new HttpClient();
                    try {
                        content = httpClient.get(task.getTargetStr().concat(url));
                    } catch (Exception e) {
                        return;
                    }
                    if (content != null && content.getStatusCode() == 200) {
                        Integer type = finger.getFingerType();
                        if (type == 2) {
                            if (content.getContent().contains(finger.getRegex())) {
                                Finger f = Finger.valueOf(finger.getEnumKey());
                                if (f != null) {
                                    task.getResult().getItems().get(target).getSystemInfo().setFinger(new FingerEntity(f,f.getValue()));
                                    SysLog.info("task="+task.getTaskMD5()+" 已识别到CMS为"+f.getValue());
                                }
                            }
                        } else if (type == 1) {
                            //...
                        }
                    }
                }
            };
            exec.execute(runnable);
        }

    }

    @Override
    public double taskProgress(String md5) {
        double rate = 0.00;
        if (coreThreadPool.containsKey(md5)){
            Map<String, Object> obj = coreThreadPool.get(md5);
            ThreadPoolExecutor executor = (ThreadPoolExecutor) obj.get("thread_pool");
            Long taskCount = executor.getTaskCount();
            Long completedTaskCount = executor.getCompletedTaskCount();
            double r = completedTaskCount.doubleValue() / taskCount.doubleValue();
            return Double.valueOf(String.format("%.2f",r));
        }
        return rate;
    }

    @Override
    public boolean taskDestroy(String task) {
        if (coreThreadPool.containsKey(task)){
            Map<String, Object> obj = coreThreadPool.get(task);
            ThreadPoolExecutor pool = (ThreadPoolExecutor) obj.get("thread_pool");
            pool.shutdownNow();
            SysLog.info("Task="+task+" 已结束剩余任务");
            this.saveData((Task)obj.get("task") , 2);
            coreThreadPool.remove(task);
            return true;
        }
        return false;
    }

    public int saveData(Task task , int status) {
        String taskMD5 = task.getTaskMD5();
        Result result = task.getResult();
        JSONObject json = JSONObject.fromObject(result);
        TaskDTOWithBLOBs dto = new TaskDTOWithBLOBs();
        dto.setBaseInfo(json.toString());
        dto.setStatus(status);

        TaskDTOExample example = new TaskDTOExample();
        example.createCriteria().andTaskMd5EqualTo(taskMD5);

        return taskDTOMapper.updateByExampleSelective(dto, example);
    }

    @Override
    public ResultCode getTaskData(String task) {
        List<TaskDTOWithBLOBs> tasks = this.selectTaskByMd5(task);
        if (tasks.size()!=1){
            return ResultCode.getInstance(500,"没有找到task");
        }
        TaskDTOWithBLOBs taskDTO = tasks.get(0);

        if (taskDTO.getStatus()<1)
            return ResultCode.getInstance(400,"该task未执行");

        String baseInfo = taskDTO.getBaseInfo();

        BASE64Encoder base64Encoder = new BASE64Encoder();
        String base64 = null;
        try {
            base64 = base64Encoder.encode(baseInfo.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64 = base64.replaceAll(SystemInit.LINE,"");
        String target = taskDTO.getTarget();
        VulnDTOExample example = new VulnDTOExample();
        example.createCriteria().andTaskMd5EqualTo(taskDTO.getTaskMd5());
        List<VulnDTO> vulns = vulnDTOMapper.selectByExample(example);

        TaskData<VulnDTO> data = new TaskData<VulnDTO>(target, base64, vulns);
        return ResultCode.getInstance(200,"成功",data);
    }

    @Override
    public ResultCode checkTask(String task) {
        List<TaskDTOWithBLOBs> tasks = this.selectTaskByMd5(task);
        if (tasks.size()>0){
            TaskDTOWithBLOBs tk = tasks.get(0);
            Integer status = tk.getStatus();
            switch (status){
                case 0:return ResultCode.SUCCESS;
                case 1:return ResultCode.getInstance(400,"正在扫描中");
                case 2:return ResultCode.getInstance(400,"扫描已结束");
                default:return ResultCode.ERROR;
            }

        }else{
            return ResultCode.getInstance(500,"task不存在");
        }

    }

    @Override
    public ResultCode putVuln(VulnQuery query) {
        List<TaskDTOWithBLOBs> tasks = selectTaskByMd5(query.getMd5());
        if (tasks.isEmpty()){
            return ResultCode.getInstance(500,"task不存在");
        }
        TaskDTOWithBLOBs tk = tasks.get(0);
        Integer status = tk.getStatus();
        switch (status){
            case 0:return ResultCode.getInstance(400,"扫描未开始");
            case 1:break;
            case 2:return ResultCode.getInstance(400,"扫描已结束");
        }

        VulnDTOWithBLOBs vulnDTO = this.initVulnFromQuery(query);
        int result = vulnDTOMapper.insertSelective(vulnDTO);
        if (result==1){
            return ResultCode.SUCCESS;
        }else {
            return ResultCode.ERROR;
        }
    }

    private VulnDTOWithBLOBs initVulnFromQuery(VulnQuery query) {
        VulnDTOWithBLOBs vulnDTO = new VulnDTOWithBLOBs();
        vulnDTO.setVulnId(query.getVulnid());
        vulnDTO.setAboutLink(query.getAboutLink());
        vulnDTO.setLevel(query.getLevel());
        vulnDTO.setVulnType(query.getVulnType());
        vulnDTO.setMessage(query.getMessage());
        vulnDTO.setTaskMd5(query.getMd5());
        vulnDTO.setResponse(query.getResponse());
        vulnDTO.setPayload(query.getPayload());
        JSONObject request = JSONObject.fromObject(query);
        String req = Base64Utils.encodeToString(request.toString().getBytes());
        vulnDTO.setRequest(req);
        return vulnDTO;
    }

    private Task initTaskFromDTO(TaskDTOWithBLOBs taskDTO) {
        Task task = new Task();
        if (CheckUtils.isJson(taskDTO.getProxy()))
        {
            task.setProxyMap(JSONObject.fromObject(taskDTO.getProxy()));
        }
        if (CheckUtils.isJson(taskDTO.getRule())){
            Rule rule = (Rule) JSONObject.toBean(JSONObject.fromObject(taskDTO.getRule()), Rule.class);
            task.setRule(rule);
        }
        task.setCookie(taskDTO.getCookie());
        task.setTaskMD5(taskDTO.getTaskMd5());
        task.setTargetStr(taskDTO.getTarget());
        task.setName(taskDTO.getTaskName());
        task.setThreadPool(taskDTO.getThread());
        task.setMaxSpider(taskDTO.getSpiderMax());
        task.setSpiderDeep(taskDTO.getSpiderDeep());
        task.getTargets().add(taskDTO.getTarget());
        task.getResult().getItems().put(taskDTO.getTarget(),new ResultItem());
        return task;
    }

    private List<TaskDTOWithBLOBs> selectTaskByMd5(String taskMd5) {
        TaskDTOExample example = new TaskDTOExample();
        example.createCriteria().andTaskMd5EqualTo(taskMd5);
        return taskDTOMapper.selectByExampleWithBLOBs(example);
    }


}
