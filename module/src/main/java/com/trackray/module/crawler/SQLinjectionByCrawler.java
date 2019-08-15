package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.RegexUtil;
import com.trackray.base.utils.SysLog;
import lombok.Builder;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Plugin(title = "基于爬虫的SQL注入检测插件" , author = "浅蓝")
public class SQLinjectionByCrawler extends CrawlerPlugin {

    @Value("${sqlmap.host}")
    private String sqlmapAPI = "http://127.0.0.1:8775/";

    @Override
    public boolean check() {
        if (target.toString().matches(".*\\?.*=.*"))
        {
            return true;
        }
        return false;
    }

    @Override
    public void process() {
        if (!Constant.AVAILABLE_SQLMAP)
            return ;
        String url = target.toString();

        Sqlmap sqlmap = new Sqlmap();
        sqlmap.setTarget(url);
        sqlmap.setServer(sqlmapAPI);

        if (sqlmap.newTask()){
            sqlmap.optionSet(
                    new HashMap<String,String>(){{
                        put("level","3");
                        put("risk","3");
                        put("batch","true");
                        put("smart","true");
                    }}
            );

            if (sqlmap.startScan()){
                while (true){
                    String status = sqlmap.statusScan();
                    if (status.equals("running")){
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(status.contains("terminated")){
                        break;
                    }
                    else{
                        break;
                    }
                    if(System.currentTimeMillis() - sqlmap.date > 150000){
                        sqlmap.stopScan();
                        sqlmap.killScan();
                        break;
                    }
                }

                sqlmap.dataScan();

                String result = sqlmap.getResult();
                if (result==null)
                    return;
                JSONArray payload = new JSONArray();
                try {
                    String matchers = RegexUtil.extractStr(result,"\"payload\":\"(.*?)\"}");
                    payload.add(matchers);
                    Vulnerable vulnerable = Vulnerable.builder()
                            .type(Vulnerable.Type.SQL_INJECTION.getType())
                            .level(Vulnerable.Level.HIGH.getLevel())
                            .title("基于爬虫+SQLMAP的 SQL注入")
                            .address(url)
                            .payload(payload.toString()).build();

                    addVulnerable(vulnerable);
                }catch (Exception e){
                    task.getExceptions().add(e);
                }


            }
        }

    }

    public void log(String log){
        SysLog.info(log);
    }
    @Data
    public class Sqlmap{

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

        private boolean newTask(){
            HttpResponse req = get(this.server + "task/new");
            if (req!=null){
                JSONObject json = toJSON(req.body());
                this.taskid = json.getString("taskid");
                log.info("创建了一个SQLMAP任务:" + taskid);
                return true;
            }
            return false;
        }

        private boolean startScan(){
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
                task.getExceptions().add(e);
            }
            return false;
        }

        private String statusScan(){
            HttpResponse req = get(server + "scan/" + taskid + "/status");
            JSONObject json = toJSON(req.body());
            if (json.containsKey("status")){
                String status = json.getString("status");
                return status;
            }
            return "error";
        }

        private void dataScan(){
            JSONObject json = toJSON(get(server + "scan/" + taskid + "/data").body());
            if (json.containsKey("data")){
                JSONArray data = json.getJSONArray("data");
                if(data.size()>0){
                    this.result = data.toString();
                    log.info("存在注入："+ this.result);
                }
            }
        }

        private void optionSet(Map<String,String > param){
            JSONObject obj = new JSONObject();
            obj.put("options",param);
            try {
                requests.url(server + "option/" + taskid + "/set")
                        .contentType("application/json")
                        .data(obj.toString()).post();
            } catch (MalformedURLException e) {
                task.getExceptions().add(e);
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

        private void stopScan(){
            get(server + "scan/" + taskid + "/stop");
        }
        private void killScan(){
            get(server + "scan/" + taskid + "/kill");
        }

    }

}
