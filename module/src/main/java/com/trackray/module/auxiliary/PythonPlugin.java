package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Python;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.CheckUtils;
import com.trackray.base.utils.Message;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/3/1 17:44
 */
//@Rule(sync = true,websocket = true)
//@Plugin(value="python" , title = "python 插件集",author = "浅蓝")
public class PythonPlugin extends WebSocketPlugin {
    private static final String PATH = BASE+"\\python\\";

    private File file;

    @Autowired
    private Python python;

    private Map<String,InputStream> strems = new HashMap<>();

    @Override
    public boolean check(Map param) {
        file = new File(PATH);
        boolean flag = file.exists();
        if (!flag && PATH.contains("web"+File.separator+"target"+File.separator+"web")){
            String var1 = "web" + File.separator + "target" + File.separator + "web";
            String var2 = "module" + File.separator + "target" + File.separator + "module";

            try {
                String jar = PATH.replace(var1,var2).split("!")[0];
                File file = new File(new URL(jar).toURI());
                if (!file.exists()){
                    return false;
                }
                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()){
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().contains("include/python") && entry.getName().endsWith(".py")){
                        String[] splitPath = entry.getName().split("/");
                        strems.put(splitPath[splitPath.length-1],jarFile.getInputStream(entry));
                    }
                }
                return !strems.isEmpty();

            } catch (Exception e) {
                return false;
            }
        }
        return flag;
    }

    @Override
    public String start() {

        send("插件列表:");
        send("==================");

        if (strems.isEmpty()){
            for (File f : file.listFiles()) {

                if (f.isFile() && f.getName().endsWith(".py")){

                    sendColorMsg(Message.BLUE(f.getName()));

                }

            }
        }else {

            for (String s : strems.keySet()) {
                sendColorMsg(Message.BLUE(s));
            }
        }


        send("使用方法：");
        send("plugin.py");


            send("==================");
            send("请输入:");


            String input = getInput();

            InputStream pystream = null;
            try {

                if (strems.isEmpty()) {
                    File pyfile = new File(this.file, input);

                    if (pyfile.exists()) {
                        pystream = new FileInputStream(pyfile);
                    }

                } else {

                    if (strems.containsKey(input)){
                        pystream = strems.get(input);
                    }

                }

                if (pystream!=null){

                    PythonInterpreter interpreter = python.interpreter();

                    interpreter.execfile(pystream);

                    PyFunction func_second= interpreter.get("params",PyFunction.class);

                    PyObject paramDict = func_second.__call__();

                    String dictStr = paramDict.toString();

                    if (CheckUtils.isJson(dictStr)){

                        JSONObject dict = JSONObject.fromObject(dictStr);

                        sendColorMsg(Message.BLUE("参数："+dictStr));

                        HashMap param = new HashMap();

                        for (Object o : dict.keySet()) {

                            sendColorMsg(Message.YELLOW("请输入参数:"+o));

                            param.put(new PyString(o.toString()), new PyString(getInput()));

                        }

                        PyFunction func = interpreter.get("verify",PyFunction.class);

                        PyObject result = func.__call__(new PyDictionary(param));

                        String str = result.toString();

                        if (StringUtils.isNotEmpty(str)){

                            sendColorMsg(Message.GREEN(str));

                        }

                    }else{
                        sendColorMsg(Message.RED("params 函数的返回值必须为字典"));
                    }
                }else{
                    sendColorMsg(Message.RED("沒有找到你输入的python脚本"));
                }

            }catch (Exception e){
                sendColorMsg(Message.RED(e.toString()));
            }

        return "bye";
    }




}
