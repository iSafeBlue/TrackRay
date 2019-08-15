package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.InnerPlugin;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/10 19:18
 */
@Plugin(title = "SQLMap 内置插件" , author = "浅蓝" , value = "sqlmapInner")
@Rule
@Data
public class SQLMapInner extends InnerPlugin{

    @Override
    public void process() {
    }
    @Value("${sqlmap.host}")
    private String server ;
    private String target ;
    private String taskid ;
    private String engineid ;
    private String status ;
    private String data ;
    private String referer ;
    private String cookie ;
    private Long date = System.currentTimeMillis();
    private String result;
    public boolean newTask(){
        HttpResponse req = get(this.server + "task/new");
        if (req!=null){
            JSONObject json = toJSON(req.body());
            this.taskid = json.getString("taskid");
            log.info("创建了一个SQLMAP任务:" + taskid);
            return true;
        }
        return false;
    }

    public boolean startScan(){
        try {
            HttpURLRequest req = requests.url(server + "scan/" + taskid + "/start")
                    .contentType("application/json");
            JSONObject json = new JSONObject();
            json.put("url",target);

            if (StringUtils.isNotEmpty(cookie)){
                json.put("cookie",cookie);
            }
            if (StringUtils.isNotEmpty(data)){
                json.put("data",data);
            }

            req.data(json.toString());

            HttpResponse post = req.post();

            if (post!=null){
                JSONObject obj = toJSON(post.body());
                this.engineid = obj.getString("engineid");
                if (StringUtils.isNotEmpty(engineid)){
                    log.info("任务扫描中:"+engineid);
                    return true;
                }
            }

        } catch (MalformedURLException e) {
            log.error(e.getMessage(),e);
        }
        return false;
    }

    public List<String> findTasks(){
        List<String> list = new ArrayList<>();
        HttpResponse req = get(server + "admin/list");
        JSONObject json = toJSON(req.body());
        if (json.containsKey("tasks")){
            JSONObject tasks = json.getJSONObject("tasks");
            list.addAll(tasks.keySet());
        }
        return list;
    }

    public String statusScan(){
        HttpResponse req = get(server + "scan/" + taskid + "/status");
        JSONObject json = toJSON(req.body());
        if (json.containsKey("status")){
            String status = json.getString("status");
            return status;
        }
        return "error";
    }

    public String dataScan(){
        JSONObject json = toJSON(get(server + "scan/" + taskid + "/data").body());
        if (json.containsKey("data")){
            JSONArray data = json.getJSONArray("data");
            if(data.size()>0){
                this.result= data.toString();
                log.info("存在注入："+ this.result);
            }
        }
        return result;
    }

    public void optionSet(Map<String,String > param){
        JSONObject obj = new JSONObject();
        obj.put("options",param);
        try {
            requests.url(server + "option/" + taskid + "/set")
                    .contentType("application/json")
                    .data(obj.toString()).post();
        } catch (MalformedURLException e) {
            log.error(e.getMessage(),e);
        }
    }
    private HttpResponse get(String url){
        try {
            return requests.url(url).get();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    private JSONObject toJSON(String text){
        return JSONObject.fromObject(text);
    }

    public void stopScan(){
        get(server + "scan/" + taskid + "/stop");
    }
    public void killScan(){
        get(server + "scan/" + taskid + "/kill");
    }

}
