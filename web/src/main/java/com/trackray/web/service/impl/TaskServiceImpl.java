package com.trackray.web.service.impl;

import com.trackray.base.attack.Awvs;
import com.trackray.web.dto.*;
import com.trackray.web.module.TaskData;
import com.trackray.web.query.TaskQuery;
import com.trackray.web.query.VulnQuery;
import com.trackray.web.repository.TaskRepository;
import com.trackray.web.repository.VulnRepository;
import com.trackray.web.service.TaskService;
import com.trackray.base.bean.*;
import com.trackray.base.controller.DispatchController;
import com.trackray.base.handle.CoreThreadMap;
import com.trackray.base.handle.SystemInit;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.utils.CheckUtils;
import com.trackray.base.utils.SysLog;
import com.trackray.base.utils.TaskUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.apache.logging.log4j.ThreadContext.containsKey;


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private DispatchController dispatchController;
    @Autowired
    private CoreThreadMap coreThreadPool;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private VulnRepository vulnRepository;

    @Autowired
    private Awvs awvs;

    @Override
    public ResultCode createTask(TaskQuery query) {
        TaskDTO dto = new TaskDTO();

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

        TaskDTO saveobj = taskRepository.save(dto);
        if (saveobj==null)
            return ResultCode.ERROR;

        return ResultCode.getInstance(200,"成功",key);
    }

    @Override
    public boolean startTask(String taskMd5) {
        List<TaskDTO> tasks = this.selectTaskByMd5(taskMd5);
        if (tasks.size()!=1){
            return false;
        }
        TaskDTO taskDTO = tasks.get(0);

        Task task = this.initTaskFromDTO(taskDTO);

        dispatchController.init(task);

        ThreadPoolExecutor exec = new ThreadPoolExecutor(task.getThreadPool(), task.getThreadPool() + 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        task.setExecutor(exec);

        this.saveData(task , 1);
        ResultCode code = dispatchController.dispatch(task);

        String taskMD5 = task.getTaskMD5();

        if (code.getCode() != 200) {
            this.saveData(task , 2);
            return false;
        }else{
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
        SysLog.info("信息探测结束！");
        for (String target : task.getTargets()) {

            if (task.getRule().thorough){
                SysLog.info("开始深度扫描");
                awvs(task , target);
                SysLog.info("深度扫描结束");
            } else if(task.getRule().crawler){
                dispatchController.crawler(task , target , exec);
            }
            if(task.getRule().port){
                //Nmap.nmap(task , target ,  exec);
                nmap(task,target);
            }
            if (task.getRule().finger){
                finger(task,target);
            }
            if (task.getRule().fuzzdir){
                //FuzzDir.fuzz(task , target ,exec);
                fuzzdir(task,target);
            }

        }

        if (task.getRule().attack){
            dispatchController.attack(task,exec);
        }

    }

    private void awvs(Task task, String target) {

        if (this.awvs!=null && awvs.ok){
            String targetId = this.awvs.createTarget(target);

            this.awvs.startScan(targetId);
            try {
                out:for (int i = 1; i <=5 ; i++) {
                    Thread.sleep(5000); //给awvs一点时间初始化任务

                    List<Awvs.Scan> scans = awvs.scans();

                    in:for (Awvs.Scan scan : scans) {
                        String tid = scan.getTargetId();
                        if(targetId.equals(tid)){
                            String scanId = scan.getScanId();

                            String scanSessionId = scan.getCurrentSession().getScanSessionId();

                            SysLog.info(String.format("Task=%s TargetID=%s scanID=%s sessionID=%s",task.getTaskMD5(),targetId,scanId,scanSessionId));


                            if(StringUtils.isAnyEmpty(scanId,scanSessionId)){
                                //如果scanid或sessionid有任意一个为空 ，则需要进行重试
                                SysLog.warn("第"+i+"次重试");

                                Thread.sleep(10000);

                                continue out;//跳过外层循环
                            }else{

                                SysLog.info("深度任务已初始化完成，即将开始深度扫描");

                                Map<String, Object> temp = task.getResult().getItems().get(target).getTemp();
                                temp.put("target_id",targetId);
                                temp.put("scan_id",scanId);
                                temp.put("session_id",scanSessionId);

                                break out;//结束外层循环
                            }


                        }
                    }

                }
            } catch (InterruptedException e) {
                SysLog.error(e);
            }

        }

        AbstractPlugin awvs = (AbstractPlugin) dispatchController.getAppContext().getBean("awvsScan");
        awvs.setParam(
                new HashMap<String,Object>(){{
                    put("task",task);
                    put("target",target);
                }}
        );
        task.getExecutor().submit(awvs);
    }

    private void nmap(Task task, String target) {
        AbstractPlugin nmap = (AbstractPlugin) dispatchController.getAppContext().getBean("nmap");
        nmap.setParam(
                new HashMap<String,Object>(){{
                    put("task",task);
                    put("target",target);
                }}
        );
        task.getExecutor().submit(nmap);
    }

    private void fuzzdir(Task task, String target) {
        AbstractPlugin fuzzDir = (AbstractPlugin) dispatchController.getAppContext().getBean("fuzzDir");
        fuzzDir.setParam(
                new HashMap<String,Object>(){{
                    put("task",task);
                    put("target",target);
                }}
        );
        task.getExecutor().submit(fuzzDir);
    }

    private void finger(Task task,String target){
        WebApplicationContext context = dispatchController.getAppContext();
        AbstractPlugin fingerScan = (AbstractPlugin) context.getBean("fingerScan");
        fingerScan.setParam(
                new HashMap<String,Object>(){{
                    put("task",task);
                    put("target",target);
                }}
        );
        task.getExecutor().submit(fingerScan);
    }

    /*private void fingerScan(Task task, String target, ThreadPoolExecutor exec) {
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

    }*/

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

            /*
                同时结束awvs扫描任务
             */
            Task t = (Task) obj.get("task");
            for (ResultItem resultItem : t.getResult().getItems().values()) {
                String scan_id = resultItem.getTemp().get("scan_id").toString();
                if (scan_id!=null && !scan_id.isEmpty()){
                    awvs.stopScan(scan_id);
                }
            }


            //this.saveData((Task)obj.get("task") , 2);
            //coreThreadPool.remove(task);
            return true;
        }
        return false;
    }

    public int saveData(Task task , int status) {
        String taskMD5 = task.getTaskMD5();
        Result result = task.getResult();
        JSONObject json = JSONObject.fromObject(result);

        TaskDTO dto = taskRepository.findTaskDTOByTaskMd5(taskMD5);
        dto.setBaseInfo(json.toString());
        dto.setStatus(status);
        return taskRepository.save(dto)!=null?1:0;
    }

    @Override
    public ResultCode getTaskData(String task) {
        List<TaskDTO> tasks = this.selectTaskByMd5(task);
        if (tasks.size()!=1){
            return ResultCode.getInstance(500,"没有找到task");
        }
        TaskDTO taskDTO = tasks.get(0);

        if (taskDTO.getStatus()<1)
            return ResultCode.getInstance(400,"该task未执行");

        String baseInfo = taskDTO.getBaseInfo();

        /*BASE64Encoder base64Encoder = new BASE64Encoder();
        String base64 = null;
        try {
            base64 = base64Encoder.encode(baseInfo.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64 = base64.replaceAll(SystemInit.LINE,"");
        String target = taskDTO.getTarget();*/

        //List<VulnDTO> vulns = vulnRepository.findAllByTaskMd5(taskDTO.getTaskMd5());

        //TaskData<VulnDTO> data = new TaskData<VulnDTO>(target, base64, vulns);
        return ResultCode.getInstance(200,"成功",baseInfo);
    }

    @Override
    public ResultCode checkTask(String task) {
        List<TaskDTO> tasks = this.selectTaskByMd5(task);
        if (tasks.size()>0){
            TaskDTO tk = tasks.get(0);
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
        List<TaskDTO> tasks = selectTaskByMd5(query.getMd5());
        if (tasks.isEmpty()){
            return ResultCode.getInstance(500,"task不存在");
        }
        TaskDTO tk = tasks.get(0);
        Integer status = tk.getStatus();
        switch (status){
            case 0:return ResultCode.getInstance(400,"扫描未开始");
            case 1:break;
            case 2:return ResultCode.getInstance(400,"扫描已结束");
        }
        VulnDTO vulnDTO = this.initVulnFromQuery(query);
        VulnDTO saveobj = vulnRepository.save(vulnDTO);
        if (saveobj!=null){
            return ResultCode.SUCCESS;
        }else {
            return ResultCode.ERROR;
        }
    }

    private VulnDTO initVulnFromQuery(VulnQuery query) {
        VulnDTO vulnDTO = new VulnDTO();
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

    private Task initTaskFromDTO(TaskDTO taskDTO) {
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

    private List<TaskDTO> selectTaskByMd5(String taskMd5) {
        return taskRepository.findAllByTaskMd5(taskMd5);
    }


}
