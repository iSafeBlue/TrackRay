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
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/3/1 17:44
 */
@Rule(sync = true,websocket = true)
@Plugin(value="python" , title = "python 插件集",author = "浅蓝")
public class PyScript extends WebSocketPlugin {

    private File file;

    @Autowired
    private Python python;

    @Override
    public boolean check(Map param) {
        file = new File(python.getScripts());
        return file.exists() && file.isDirectory();
    }

    @Override
    public String start() {

        println("插件列表:");
        println("==================");

        for (File f : file.listFiles()) {

            if (f.isFile() && f.getName().endsWith(".py")){

                sendColorMsg(Message.BLUE(f.getName()));

            }

        }

        println("==================");

        println("请输入文件名 如:plugin.py");


            println("==================");
            println("请输入:");


            String input = getInput();

            InputStream pystream = null;
            try {

                File pyfile = new File(this.file, input);

                if (pyfile.exists()) {
                    pystream = new FileInputStream(pyfile);
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

                            sendColorMsg(Message.GREEN(HtmlUtils.htmlEscape(str)));

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
