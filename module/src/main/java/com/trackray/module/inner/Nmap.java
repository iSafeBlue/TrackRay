package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Task;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.SystemOS;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ReUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.utils.XMLUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.swing.plaf.BorderUIResource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(enable = false)
@Plugin(value="nmap",title = "NMAP模块" , author = "浅蓝" )
public class Nmap extends InnerPlugin {

    @Value("${temp.dir}")
    private String tempDirl;

    private static String filenameTemplate = "nmap_%s_%s.xml";


    //nmap -sT -A -sV -O -T5 --open -oX nping.xml target

    private static String nmap = "nmap";

    private String filename = "";

    @Override
    public void process() {
        result = start();
    }

    @Override
    public Object start() {
        if (!Constant.AVAILABLE_NMAP)
            return null;
        String realIP = this.task.getResult().getHostInfo().getRealIP();
        filename = tempDirl.concat(String.format(filenameTemplate, realIP, task.getTaskMD5()));

        Shell shell = shell();
        try {
            //shell.block(true).target(nmap).exec("-sT","-A","-sV","-O","-T5","--open","-oX",filename,realIP);
            shell.block(true).target(nmap).exec("-sT -A -sV -O -T5 --open -oX " + filename + " "+realIP);

            String content = shell.content();

            if (content!=null){
                String fileContent = FileUtils.readFileToString(new File(filename));
                if (StringUtils.isBlank(fileContent))
                    return null;

                parseResult(fileContent);
            }


        } catch (IOException e) {
            task.getExceptions().add(e);
        }

        return null;
    }

    @Override
    public void after() {
        File file = new File(filename);
        if (file!=null && file.exists() )
            file.delete();
    }

    private boolean checkNotBlank(Elements elements){
        return elements!=null && !elements.isEmpty();
    }
    private void parseResult(String fileContent) {

        Document document = Jsoup.parse(fileContent);

        Elements ports = document.getElementsByTag("port");

        Elements os = document.getElementsByTag("osmatch");


        if (checkNotBlank(ports)){
            for (Element port : ports) {
                PortEntity.PortEntityBuilder builder = PortEntity.builder();

                String protocol = port.attr("protocol");
                String portid = port.attr("portid");

                builder.protocol(protocol).port(Integer.parseInt(portid));

                Elements service = port.getElementsByTag("service");
                Elements script = port.getElementsByTag("script");

                if (checkNotBlank(service)){
                    Element first = service.first();
                    String name = first.attr("name");
                    String product = first.attr("product");
                    String version = first.attr("version");
                    builder.service(name);
                    builder.product(product);
                    builder.version(version);
                }

                if (checkNotBlank(script)){
                    HashMap<String, String> map = new HashMap<>();
                    for (Element scr : script) {
                        String id = scr.attr("id");
                        String output = scr.attr("output");
                        if (StringUtils.isNoneEmpty(id,output)){
                            map.put(id,output);
                        }
                    }
                    builder.banner(map);
                }

                PortEntity entity = builder.build();

                this.task.getResult().getSystemInfo().getPorts().add(entity);
            }
        }

        if (checkNotBlank(os)){
            Element first = os.first();
            String name = first.attr("name");
            SystemEntity.SystemEntityBuilder builder = SystemEntity.builder();
            if (name.toLowerCase().contains("inux")){
                builder.os(SystemOS.LINUX);
            }else if(name.toLowerCase().contains("indows")){
                builder.os(SystemOS.WINDOWS);
            }else{
                builder.os(SystemOS.OTHER);
            }
            builder.version(name);
            SystemEntity entity = builder.build();

            this.task.getResult().getSystemInfo().setSystemOS(entity);
        }

    }


}
