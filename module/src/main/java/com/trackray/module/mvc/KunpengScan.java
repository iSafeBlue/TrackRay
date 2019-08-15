package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.MVCPlugin;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.trackray.base.utils.CheckUtils;
import com.trackray.base.utils.StrUtils;
import com.trackray.module.inner.JSONPlugin;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/6 10:29
 */
@Plugin(title = "鲲鹏扫描器" , value = "kunpengScan")
@Rule(auth = true)
public class KunpengScan extends MVCPlugin {

    public final static String LIB_FILENAME = "kunpeng_%s_v%s.%s";
    public final static String VERSION = "20190527";

    public final static String WIN_LIB =  BASE + "/kunpengScan/" + String.format(LIB_FILENAME , "win",VERSION,"dll");
    public final static String LINUX_LIB = BASE + "/kunpengScan/" + String.format(LIB_FILENAME , "linux",VERSION,"so");

    @Value("${ceye.io.identifier}")
    private String identifier;

    public interface Kunpeng extends Library {
        Kunpeng INSTANCE = (Kunpeng) Native.load(
                Constant.TRACKRAY_SYSTEMOS==Constant.WINDOWS?WIN_LIB:LINUX_LIB
        ,Kunpeng.class);



        /*  配置设置，传入配置JSON，格式为：
           {
               "timeout": 15, // 插件连接超时
               "aider": "http://123.123.123.123:8088", // 漏洞辅助验证接口，部分漏洞无法通过回显判断是否存在漏洞，可通过辅助验证接口进行判断。
               "http_proxy": "http://123.123.123.123:1080", // HTTP代理，所有插件http请求流量将通过代理发送（需使用内置的http请求函数util.RequestDo）
               "pass_list": ["passtest"], // 默认密码字典，不定义则使用硬编码在代码里的小字典
               "extra_plugin_path": "/tmp/plugin/" // 除已编译好的插件（Go、JSON）外，可指定额外插件目录（仅支持JSON插件），指定后程序会周期读取加载插件
           }
       */
        public  void SetConfig(String configJSON);
        public  String GetPlugins();
        public  String Check(String taskJSON);

    }


    @Data
    public static class Plugin {
        private String name;
        private String remarks;
        private String type;
        private String target;
    }

    public List<Plugin> plugins = new ArrayList<>();
    //public Set<String> types = new HashSet<>();
    public Set<String> targets = new HashSet<>();

    @Override
    public void index() {

        loadPlugins();
        model.addObject("plugins",plugins);
        model.addObject("targets",targets);
        model.setViewName("index");
    }

    @Function(value = "check" , desc = "检测插件")
    public void check(){
        String result = checkPlugins();
        try {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String checkPlugins(){
        JSONObject cfg = new JSONObject();
        cfg.put("timeout",Integer.parseInt(getParam().getOrDefault("timeout","15").toString()));
        cfg.put("aider","http://"+identifier);
        cfg.put("extra_plugin_path", new File(JSONPlugin.jsonPath).getAbsolutePath());
        Kunpeng.INSTANCE.SetConfig(cfg.toString());
        JSONObject jsonObject = new JSONObject();
        if (getParam().containsKey("netloc"))
            jsonObject.put("netloc",getParam().getOrDefault("netloc",""));
        if (getParam().containsKey("target"))
            jsonObject.put("target",getParam().getOrDefault("target","web"));
        if (getParam().containsKey("type"))
            jsonObject.put("type",getParam().getOrDefault("type","web"));
        String check_json = jsonObject.toString();
        String checkResult = Kunpeng.INSTANCE.Check(check_json);
        if (CheckUtils.isJson(checkResult)){
            return StrUtils.formatJson(checkResult);
        }else{
            String s = Native.toString(Native.toByteArray(checkResult), "UTF-8");
            return StrUtils.formatJson(s);
        }
    }

    private void loadPlugins() {
        String result = Kunpeng.INSTANCE.GetPlugins();
        result = Native.toString(Native.toByteArray(result), "UTF-8");
        targets.clear();
        plugins.clear();
        JSONArray arr = toJSON(result);
        if (arr!=null && !arr.isEmpty()){
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String name = obj.getString("name");
                String remarks = obj.getString("remarks");
                String target = obj.getString("target");
                String type = obj.getString("type");

                Plugin plugin = new Plugin();
                plugin.setName(name);
                plugin.setRemarks(remarks);
                plugin.setTarget(target);
                plugin.setType(type);

                targets.add(target);
                plugins.add(plugin);

            }
        }
    }

    public static JSONArray toJSON (String str) {

        str = str.substring(str.indexOf("["));
        if (CheckUtils.isJson(str)){
            return JSONArray.fromObject(str);
        }
        return new JSONArray();
    }

}
