package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import org.javaweb.core.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/22 14:01
 */
@Plugin(title = "列目录漏洞",author = "浅蓝")
public class ListDir extends CrawlerPlugin {
    @Override
    public boolean check() {
        return target.toString().contains(".");
    }

    public static void main(String[] args) {
        System.out.println("/asdas".split("/").length);
    }

    @Override
    public void process() {

        String url = target.toString();

        try {
            URL u = new URL(url);
            String path = u.getPath();
            if (path.contains("/") ){
                String[] paths = path.split("/");
                if (paths.length>2){
                    LinkedList<String> list = new LinkedList<>();
                    list.addAll(Arrays.asList(paths));
                    listDir(u , list , 0);
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        url = url.substring(0, url.lastIndexOf("/"));

        try {
            String body = requests.url(url).get().body();



        } catch (MalformedURLException e) {

        }

    }

    private void listDir(URL u, LinkedList<String> paths , int num) {

        if (paths.size()<=2 && num>3)
            return;
        String remove = paths.removeLast();
        StringBuffer b = new StringBuffer();
        b.append("/");
        for (String path : paths) {
            b.append(path).append("/");
        }
        String path = b.toString();

        String url = urltoString(u).concat(path);
        try {
            String body = requests.url(url).get().body();

            if (StringUtils.contains(body,"Index of /") && body.contains("a href")){
                Vulnerable vulnerable = Vulnerable.builder()
                        .title("文件遍历漏洞")
                        .address(url)
                        .payload(url)
                        .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                        .level(Vulnerable.Level.MIDDLE.getLevel())
                        .build();

                addVulnerable(vulnerable);
                return;
            }else{
                listDir(u , paths , num+1);
            }

        }catch (Exception e){
            return;
        }



    }

    private String urltoString(URL u) {
        int len = u.getProtocol().length() + 1;
        if (u.getAuthority() != null && u.getAuthority().length() > 0)
            len += 2 + u.getAuthority().length();
        if (u.getPath() != null) {
            len += u.getPath().length();
        }
        if (u.getQuery() != null) {
            len += 1 + u.getQuery().length();
        }
        if (u.getRef() != null)
            len += 1 + u.getRef().length();

        StringBuffer result = new StringBuffer(len);
        result.append(u.getProtocol());
        result.append(":");
        if (u.getAuthority() != null && u.getAuthority().length() > 0) {
            result.append("//");
            result.append(u.getAuthority());
        }
        if (u.getPath() != null) {
            result.append(u.getPath());
        }
        return result.toString();
    }
}
