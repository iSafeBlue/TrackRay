package com.trackray.base.attack;

import lombok.Data;
import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/3/1 17:43
 */
@Component("py")
@Data
public class Python {

    @Value("${python.package.path}")
    private String packages;

    @Value("${python.script.path}")
    private String scripts;
    
    @Value("${maven.repository.path}")
    private String repository;

    private String jythonLib = "/org/python/jython-standalone/2.7.1/jython-standalone-2.7.1.jar/Lib";

    private PythonInterpreter interpreter;

    public synchronized PythonInterpreter interpreter(){

        if (this.interpreter!=null){
            return interpreter;
        }

        Properties props = new Properties();
        props.put("python.console.encoding", "UTF-8");
        props.put("python.import.site", "false");

        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);

        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = interpreter.getSystemState();
        sys.path.add(packages);
        sys.path.add(scripts);
        sys.path.add(repository.concat(jythonLib));
        if (interpreter!=null){
            this.interpreter = interpreter;
        }

        return interpreter;
    }

}
