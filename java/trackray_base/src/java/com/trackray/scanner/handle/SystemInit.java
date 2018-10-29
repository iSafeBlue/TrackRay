package com.trackray.scanner.handle;

import com.trackray.scanner.attack.Payload;
import com.trackray.scanner.attack.SQLMap;
import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.utils.PropertyUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SystemInit
{

    public static String LINE = System.getProperty("line.separator");

    public void init(){
        String domain = PropertyUtil.getProperty("dict.domain");
        String dir = PropertyUtil.getProperty("dict.dir");

        Constant.CENSYS_APPID = PropertyUtil.getProperty("censys.appid");
        Constant.CENSYS_SECRET = PropertyUtil.getProperty("censys.secret");
        SQLMap.API = PropertyUtil.getProperty("sqlmap.root");
        Constant.Vuln.VULN_ADD_API = PropertyUtil.getProperty("vuln.root").concat("/task/vuln/put");
        Constant.NmapComm.NMAP_DIR = PropertyUtil.getProperty("nmap.path");

        List<String> domainFile = null;
        List<String> dirFile = null;
        try {
            String filepath = new File(this.getClass().getClassLoader().getResource(PropertyUtil.getProperty("include.path")).getPath()).getPath();
            Constant.RESOURCES_PATH = filepath;
            Payload.domainPayload = FileUtils.readLines(new File(this.getClass().getClassLoader().getResource(domain).getFile()));
            Payload.dirPayload = FileUtils.readLines(new File(this.getClass().getClassLoader().getResource(dir).getFile()));
        } catch (Exception e) {

        }

        String p = "'';!--\"<XSS>=&{()}\n" +
                "\\\";alert('XSS');//\n" +
                "<script>alert(1)</script>\n"+
                "<h1>XSS</h1>";

        Payload.xssPayload = Arrays.asList(p.split("\n"));

    }


}
