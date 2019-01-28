package com.trackray.base.handle;

import com.trackray.base.bean.Task;
import com.trackray.base.utils.SysLog;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务线程监控
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class ThreadMonitor {
    private int maxTime;
    private int sleepTime;
    private CoreThreadMap coreThreadPool;

    public void save(Task task , int status){};

    public void monitor() throws InterruptedException {
        new Thread(){
            @Override
            public void run() {

                while (true)
                {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (coreThreadPool.size() > 0){
                        for (Map.Entry<String, Map<String, Object>> entry : coreThreadPool.entrySet()) {
                            Task task = (Task) entry.getValue().get("task");
                            Long taskTime = (Long) entry.getValue().get("create_time");
                            ThreadPoolExecutor value = (ThreadPoolExecutor) entry.getValue().get("thread_pool");
                            long currenTime = System.currentTimeMillis();
                            long temp = currenTime - taskTime;
                            if ( temp > maxTime){
                                SysLog.info(String.format("Task=%s 线程共启动%d毫秒，已超过设定最大时间%d，已结束该线程",entry.getKey(),temp,maxTime));
                                save(task , 2);
                                value.shutdownNow();
                                coreThreadPool.remove(entry.getKey());
                            }else{
                                if (value.isShutdown() || ((value.getTaskCount())==(value.getCompletedTaskCount()) && value.getPoolSize()>0) ){
                                    SysLog.info(String.format("Task=%s 该任务已完成",entry.getKey()));
                                    value.shutdownNow();
                                    coreThreadPool.remove(entry.getKey());
                                    save(task , 2);
                                    continue;
                                }

                                SysLog.info(String.format("Task=%s 线程共启动%d毫秒 任务数=%d 已完成=%d ",entry.getKey(),temp,value.getTaskCount(),value.getCompletedTaskCount()));
                                save(task , 1);
                            }

                        }
                        System.gc(); //垃圾回收
                    }


                }

            }
        }.start();
    }


    public void setCoreThreadPool(CoreThreadMap coreThreadPool) {
        this.coreThreadPool = coreThreadPool;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
