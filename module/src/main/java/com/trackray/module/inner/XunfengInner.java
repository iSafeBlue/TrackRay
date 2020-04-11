package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Python;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.lang3.StringUtils;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/23 20:04
 */
@Plugin(value =  "xunfengInner" , title = "巡风内部插件")
@Rule
public class XunfengInner extends InnerPlugin<String> {

    public static final String PLUGIN_PATH = BASE + "/xunfeng/";
    public static boolean init_state = false;
    @Autowired
    private Python python;

    private String filename;
    private String ip ;
    private Integer port;
    private Integer timeout = 30;

    @Override
    public void process() {

        try {
            PythonInterpreter interpreter = python.interpreter();
            interpreter.execfile(filename);
            PyFunction check = interpreter.get("check", PyFunction.class);

            PyObject check_call = check.__call__(new PyString(ip), new PyInteger(port), new PyInteger(timeout));

            String result = check_call.toString();
            if (result!=null &&
                    !StringUtils.contains(result,"None")
                    && !StringUtils.contains(result,"False")) {

                //PyObject get_plugin_info = interpreter.get("get_plugin_info").__call__();
                //Map map = (Map) get_plugin_info.getDict().__tojava__(Map.class);
                this.result = result;
                return;
            }
        }catch (Exception e){
            log.error(e.toString());
        }

        result="";
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Python getPython() {
        return python;
    }

    public void setPython(Python python) {
        this.python = python;
    }
}
