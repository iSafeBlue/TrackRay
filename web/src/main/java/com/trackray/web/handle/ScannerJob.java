package com.trackray.web.handle;

import com.trackray.base.bean.*;
import com.trackray.base.controller.DispatchController;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.AbstractPOC;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.store.VulnDTO;
import com.trackray.base.store.VulnRepository;
import com.trackray.base.utils.CheckUtils;
import com.trackray.base.utils.SysLog;
import com.trackray.module.inner.*;
import com.trackray.web.dto.TaskDTO;
import com.trackray.web.repository.TaskRepository;
import net.sf.json.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 扫描任务类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/23 15:26
 */
@Component
public class ScannerJob implements InterruptableJob {

    private static final Logger log = LoggerFactory.getLogger(ScannerJob.class);

    private String taskKey;//任务KEY
    private JobKey jobKey; //quartz JobKey

    private boolean interrupt = false; //是否已结束

    @Autowired
    private DispatchController dispatchController;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private VulnRepository vulnRepository;

    private Task task;

    private ThreadPoolExecutor threadPool;  //任务线程池

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.jobKey = jobExecutionContext.getJobDetail().getKey();

        JobDataMap parameters = jobExecutionContext.getMergedJobDataMap();

        this.taskKey = parameters.getString("taskKey");

        TaskDTO taskDTO = taskRepository.findTaskDTOByTaskMd5(this.taskKey);

        if (taskDTO==null) {
            SysLog.error("任务数据对象为空");
            return;
        }


        this.task = this.initTaskFromDTO(taskDTO);

        saveData(task,1);
        /*try {
            new HttpClient().get(task.getTargetStr());
        } catch (Exception e) {
            SysLog.error("访问目标出现异常，请检测，已结束此任务"+e.getMessage());
            this.saveData(task , 2);
            return;
        }*/

        this.threadPool = new ThreadPoolExecutor(task.getThreadPool(), task.getThreadPool() + 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        task.setExecutor(this.threadPool);


        switch (whatTarget()){
            case Constant.IP_TYPE:
                log.info(taskinfo()+"目标是IP主机");

                fuckIPinfo();

                fuckCSegment();

                break;
            case Constant.URL_TYPE:
                log.info(taskinfo()+"目标是域名主机");

                fuckWhois();

                fuckIPinfo();

                if (task.getRule().childdomain){
                    fuckChildDomain();
                }

                if (task.getRule().sense)
                {
                    fuckBroDomain();
                }
                break;
        }

        saveData(task,1);

        if (task.getRule().thorough)
            fuckThorough();


        saveData(task,1);

        if (task.getRule().crawler)
            fuckCrawler();

        if (task.getRule().fuzzdir)
            fuckDir();

        saveData(task,1);

        if (task.getRule().port)
            fuckPort();
        else
            task.getResult().getVariables().put("nmapScaned",true);

        saveData(task,1);

        if (task.getRule().finger)
            fuckFinger();
        else
            task.getResult().getVariables().put("fingerScaned",true);

        if (task.getRule().attack)
            fuckPlugin();

        while (true)
        {

            Date endTime = jobExecutionContext.getTrigger().getEndTime();

            Date currTime = new Date();

            if (currTime.compareTo(endTime) > -1 ){
                log.info(taskinfo().concat("当前任务已超时，即将结束任务"));
                break;
            }

            if (interrupt || threadPool.isShutdown() || ((threadPool.getTaskCount())==(threadPool.getCompletedTaskCount()) ) ){
                log.info(taskinfo().concat("该任务已完成"));
                break;
            }else{
                int activeCount = threadPool.getActiveCount();
                log.info(taskinfo().concat(String.format("当前活动任务数:%s",String.valueOf(activeCount))));
                saveData(task,1);
            }

            try {
                Thread.sleep(10000);    //十秒一次
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        threadPool.shutdownNow();
        saveData(task,2);

        log.info("======================任务基本信息=========================");


        Result result = task.getResult();
        log.info(result.toString());

        log.info(com.alibaba.fastjson.JSONObject.toJSONString(result));

        log.info("=========================================================");



        log.info("======================漏洞详情============================");

        List<VulnDTO> vulns = vulnRepository.findAllByTaskMd5(task.getTaskMD5());
        for (VulnDTO vuln : vulns) {
            log.info(vuln.toString());
        }


        log.info("=========================================================");

        log.info("======================异常信息============================");

        for (Exception exception : task.getExceptions()) {
            log.error("异常：",exception);
        }

        log.info("=========================================================");

    }

    /**
     * 获取IP基本信息
     */
    private void fuckIPinfo() {
        log.info(taskinfo()+"开始检查IP基本信息");
        FuckIPinfo ipinfo = new FuckIPinfo();
        ipinfo.setTask(this.task);
        ipinfo.executor();
    }

    /**
     * 插件扫描
     */
    private void fuckPlugin() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean fingerScaned = (boolean) task.getResult().getVariables().getOrDefault("fingerScaned", false);
                    boolean nmapScaned = (boolean) task.getResult().getVariables().getOrDefault("nmapScaned", false);
                    if (fingerScaned && nmapScaned)
                        break;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                }

                log.info(taskinfo() + "开始调用漏洞检测插件");

                WebApplicationContext context = dispatchController.getAppContext();

                Map<String, AbstractPOC> beans = context.getBeansOfType(AbstractPOC.class);

                SimpleVulRule simpleVul = dispatchController.getAppContext().getBean(SimpleVulRule.class);
                simpleVul.setTask(task);
                threadPool.submit(simpleVul);//简单的漏洞规则

                //调用JSON格式的漏洞插件
                JSONInner jsonInner = dispatchController.getAppContext().getBean(JSONInner.class);
                jsonInner.setTask(task);
                threadPool.submit(jsonInner);

                //调用独立的漏洞插件
                for (Map.Entry<String, AbstractPOC> entry : beans.entrySet()) {
                    AbstractPOC exp = entry.getValue();
                    exp.setTask(task);
                    exp.setTarget(task.getTargetStr());
                    threadPool.submit(exp);
                }
                log.info(taskinfo() + "调用漏洞检测插件结束");
            }
        };

        threadPool.submit(runnable);

    }


    /**
     * 深度扫描 AWVS和NESSUS
     */
    private void fuckThorough() {
        log.info(taskinfo()+"开始深度扫描");

        FuckAwvs fuckAwvs = dispatchController.getAppContext().getBean(FuckAwvs.class);
        fuckAwvs.setTask(task);
        Object result = fuckAwvs.executor().result();
        if (result!=null){
            Map<String,Object> map = (Map<String, Object>) result;

            AwvsScan awvsScan = dispatchController.getAppContext().getBean(AwvsScan.class);

            awvsScan.setTask(task);

            awvsScan.addParam("map",map);

            threadPool.submit(awvsScan);
        }
        log.info(taskinfo()+"深度扫描结束");

    }

    /**
     * 目录爆破
     */
    private void fuckDir() {
        log.info(taskinfo()+"开始扫描目录");
        FuzzDir fuzzDir = dispatchController.getAppContext().getBean(FuzzDir.class);

        fuzzDir.setTask(task);
        threadPool.submit(fuzzDir);
        log.info(taskinfo()+"扫描目录结束");
    }

    /**
     * 网页爬虫
     */
    private void fuckCrawler() {
        log.info(taskinfo()+"开始网页爬虫");
        FuckCrawler crawler = dispatchController.getAppContext().getBean(FuckCrawler.class);
        crawler.setTask(task);
        threadPool.submit(crawler);
        log.info(taskinfo()+"网页爬虫结束");
    }

    /**
     * 指纹识别
     * @Description: 这一步会阻塞，等待检测完成后才走下一步
     */
    private void fuckFinger() {
        log.info(taskinfo()+"开始鉴别指纹");
        FingerScan fingerScan = dispatchController.getAppContext().getBean(FingerScan.class);

        fingerScan.setTask(task);
        Future<AbstractPlugin<FingerPrint>> future = threadPool.submit(fingerScan);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                //如果没有结束或关闭则一直循环
                int flag = 30;//等待15分钟 超时强制结束线程
                while(true){
                    flag--;
                    boolean done = future.isDone();
                    boolean cancelled = future.isCancelled();
                    if(done || cancelled || interrupt)
                        break;
                    if (flag<=0)
                        break;
                    try {
                        Thread.sleep(10000);//一次十秒
                    } catch (InterruptedException e) {
                        break;
                    }

                }
                future.cancel(true);
                task.getResult().getVariables().put("fingerScaned",true);
                log.info(taskinfo()+"指纹扫描结束");
            }
        });

        /*threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get(1,TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    task.getExceptions().add(e);
                } catch (ExecutionException e) {
                    task.getExceptions().add(e);
                } catch (TimeoutException e) {
                    task.getExceptions().add(e);
                    log.error("指纹扫描超时");
                }finally {
                    future.cancel(true);
                    log.info("指纹扫描结束");
                    if (future.isDone()||future.isCancelled())
                        task.getResult().getVariables().put("fingerScaned",true);
                }
            }
        });*/


    }

    /**
     * 扫描端口
     * @Description: 这一步会阻塞，等待检测完成后才走下一步
     */
    private void fuckPort() {
        log.info(taskinfo()+"开始扫描端口");
        Nmap nmap = dispatchController.getAppContext().getBean(Nmap.class);
        nmap.setTask(task);
        Future future = threadPool.submit(nmap);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                //如果没有结束或关闭则一直循环
                int flag = 30;//15分钟
                while(true){
                    flag--;
                    boolean done = future.isDone();
                    boolean cancelled = future.isCancelled();
                    if(done || cancelled || interrupt)
                        break;
                    if (flag<=0)
                        break;
                    try {
                        Thread.sleep(10000);//一次十秒
                    } catch (InterruptedException e) {
                        break;
                    }

                }
                future.cancel(true);
                task.getResult().getVariables().put("nmapScaned",true);
                log.info(taskinfo()+"nmap端口扫描结束");
            }
        });

        /*threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get(1,TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    task.getExceptions().add(e);
                } catch (ExecutionException e) {
                    task.getExceptions().add(e);
                } catch (TimeoutException e) {
                    task.getExceptions().add(e);
                    log.error("nmap扫描超时");
                }finally {
                    future.cancel(true);
                    log.info("Nmap端口扫描结束");
                    if (future.isDone()||future.isCancelled())
                        task.getResult().getVariables().put("nmapScaned",true);
                }
            }
        });*/
    }

    /**
     * 扫描兄弟域名
     */
    private void fuckBroDomain() {
        log.info(taskinfo()+"开始扫描兄弟域名");
        FuckBroDomain broDomain = dispatchController.getAppContext().getBean(FuckBroDomain.class);
        broDomain.setTask(task);
        threadPool.submit(broDomain);
        log.info(taskinfo()+"扫描兄弟域名结束");
    }

    /**
     * 扫描子域名
     */
    private void fuckChildDomain() {
        log.info(taskinfo()+"开始扫描子域名");
        FuckChildDomain childDomain = dispatchController.getAppContext().getBean(FuckChildDomain.class);

        childDomain.setTask(task);
        threadPool.submit(childDomain);
        log.info(taskinfo()+"扫描子域名结束");
    }

    /**
     * 扫描域名基本信息
     * Whois、域名商、DNS服务器、CDN服务商
     */
    private void fuckWhois() {
        log.info(taskinfo()+"开始检查域名基本信息");
        FuckWhois whois = dispatchController.getAppContext().getBean(FuckWhois.class);

        whois.setTask(task);
        threadPool.submit(whois);
        log.info(taskinfo()+"检查域名基本信息结束");
    }

    /**
     * 扫描C段存活主机
     */
    private void fuckCSegment() {
        //TODO:Fuck C段
        //用 NMAP或 CENSYS
    }

    private int whatTarget() {
        Task.Target target = this.task.getTarget();

        return target.type;
    }


    public int saveData(Task task , int status) {

        String taskMD5 = task.getTaskMD5();

        log.info(taskinfo()+taskMD5+"：正在保存数据 状态："+status);


        Result result = task.getResult();

        String json = com.alibaba.fastjson.JSONObject.toJSONString(result);

        TaskDTO dto = taskRepository.findTaskDTOByTaskMd5(taskMD5);

        dto.setBaseInfo(json);

        dto.setStatus(status);

        List<VulnDTO> vulns = vulnRepository.findAllByTaskMd5(taskMD5);

        if (vulns!=null && !vulns.isEmpty()){
            int max = 0;
            for (VulnDTO vuln : vulns) {
                Integer level = vuln.getLevel();
                if (level!=null && level>max)
                    max = level;
            }
            if (max>2)
                max = 2;
            dto.setLevel(max);
        }

        return taskRepository.save(dto)!=null?1:0;
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
        return task;
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {

        log.info(taskinfo().concat("接收到终止命令，正在结束未执行的任务，指纹识别/端口识别需要等待。"));

        interrupt = true;

        System.gc();

    }

    public String taskinfo(){
        return String.format("job[%s]   ", jobKey);
    }
}
