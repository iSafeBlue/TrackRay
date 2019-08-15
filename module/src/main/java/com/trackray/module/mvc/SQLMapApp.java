package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.utils.StrUtils;
import com.trackray.module.inner.SQLMapInner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/10 19:12
 */
@Plugin(title = "SQL注入检测" , value = "sqlmapApp" , author = "浅蓝")
@Rule(auth = true)
public class SQLMapApp extends MVCPlugin {

    @Autowired
    private SQLMapInner sqlMapInner;

    @Override
    public void index() {
        List<String> tasks = sqlMapInner.findTasks();
        model.addObject("tasks",tasks);
        model.addObject("status",Constant.AVAILABLE_SQLMAP);
        model.setViewName("index");
    }
    @Function
    public void scan(){
        String url = param.getOrDefault("url", "").toString();
        String cookie = param.getOrDefault("cookie", "").toString();
        String data = param.getOrDefault("data", "").toString();
        String level = param.getOrDefault("level", "").toString();
        String risk = param.getOrDefault("risk", "").toString();
        HashMap<String, String> map = new HashMap<>();
        if (sqlMapInner.newTask()){

            if (StringUtils.isNotEmpty(url))
                sqlMapInner.setTarget(url);

            if (StringUtils.isNotEmpty(cookie))
                sqlMapInner.setCookie(cookie);

            if (StringUtils.isNotEmpty(data))
                sqlMapInner.setData(data);

            if (StringUtils.isNotEmpty(level))
                map.put("level",level);

            if (StringUtils.isNotEmpty(risk))
                map.put("risk",risk);

            if (!map.isEmpty())
                sqlMapInner.optionSet(map);

            if (sqlMapInner.startScan()){
                write("正在扫描中");
            }else{
                write("扫描失败，请检查日志");
            }
            return;
        }else{
            write("创建任务失败");
        }
    }
    @Function
    public void data(){
        if (param.containsKey("task")){
            try {
                String task = param.get("task").toString();
                sqlMapInner.setTaskid(task);
                String status = sqlMapInner.statusScan();
                String data = sqlMapInner.dataScan();
                if (StringUtils.equals(status,"terminated")){
                    write(StrUtils.formatJson(data));
                }else if(StringUtils.equals(status,"running")){
                    write("任务正在扫描中，请等待扫描结束");
                }else {
                    write(status);
                }
            }catch (Exception e){
                write(e.getMessage());
            }
        }else {
            write("任务不存在");
        }
    }
    @Function
    public void stop(){
        if (param.containsKey("task")){
            String task = param.get("task").toString();
            sqlMapInner.setTaskid(task);
            sqlMapInner.stopScan();
            sqlMapInner.killScan();
            write("已停止");
        }else {
            write("任务不存在");
        }
    }

}
