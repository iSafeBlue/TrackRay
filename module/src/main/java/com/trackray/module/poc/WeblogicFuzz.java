package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.plugin.AbstractPOC;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;

import java.net.MalformedURLException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/24 12:10
 */
@Plugin(title = "Weblogic 敏感路径扫描" , author = "浅蓝")
public class WeblogicFuzz extends AbstractPOC {

    @Override
    public void attack(Task task) {
        for (String url : urls) {


            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    try {
                        String u = getTarget().concat(url);

                        int statusCode = new HttpURLRequest().url(u).get().getStatusCode();

                        if (statusCode>0&&statusCode != 404) {
                            task.getResult().getSystemInfo().getDirs().put(u, statusCode);
                        }

                    } catch (MalformedURLException e) {
                    }


                }
            };

            task.getExecutor().submit(runnable);

        }



    }

    @Override
    public boolean check(Result result) {

        if (result.getSystemInfo().getWebServer() == WEBServer.WEBLOGIC)
            return true;

        String url404 = getTarget().concat("/asdasdsad");

        String[] flag = {"Hypertext Transfer Protocol","From RFC 2068","unavailable and has no forwarding address"};

        try {
            HttpURLRequest url = requests.url(url404);
            HttpResponse response = url.get();

            String body = response.body();

            if (StringUtils.containsAny(body , flag)){
                this.getTask().getResult().getAdditional()
                        .put("网站的WEB服务器","Weblogic");
                getTask().getResult().getSystemInfo().setWebServer(WEBServer.WEBLOGIC);
                return true;
            }

        } catch (MalformedURLException e) {
        }
        return false;
    }

    public static String[] urls = ("/AdminCaptureRootCA\n" +
            "/AdminClients\n" +
            "/AdminConnections\n" +
            "/AdminEvents\n" +
            "/AdminJDBC\n" +
            "/AdminLicense\n" +
            "/AdminMain\n" +
            "/AdminProps\n" +
            "/AdminRealm\n" +
            "/AdminThreads\n" +
            "/AdminVersion\n" +
            "/BizTalkServer\n" +
            "/Bootstrap\n" +
            "/Certificate\n" +
            "/Classpath/\n" +
            "/ConsoleHelp/\n" +
            "/ConsoleHelp\n" +
            "/DefaultWebApp\n" +
            "/HTTPClntClose\n" +
            "/HTTPClntLogin\n" +
            "/HTTPClntRecv\n" +
            "/HTTPClntSend\n" +
            "/LogfileSearch\n" +
            "/LogfileTail\n" +
            "/Login.jsp\n" +
            "/MANIFEST.MF\n" +
            "/META-INF\n" +
            "/SimpappServlet\n" +
            "/StockServlet\n" +
            "/T3AdminMain\n" +
            "/UniversityServlet\n" +
            "/WEB-INF\n" +
            "/WEB-INF./web.xml\n" +
            "/WEB-INF/web.xml\n" +
            "/WLDummyInitJVMIDs\n" +
            "/WebServiceServlet\n" +
            "/_tmp_war\n" +
            "/_tmp_war_DefaultWebApp\n" +
            "/a2e2gp2r2/x.jsp\n" +
            "/actions\n" +
            "/admin/login.do\n" +
            "/applet\n" +
            "/applications\n" +
            "/authenticatedy\n" +
            "/bea_wls_internal/classes/\n" +
            "/bea_wls_internal/WebServiceServlet\n" +
            "/bea_wls_internal/getior\n" +
            "/bea_wls_internal\n" +
            "/bea_wls_internal/HTTPClntSend\n" +
            "/bea_wls_internal/HTTPClntRecv\n" +
            "/bea_wls_internal/iiop/ClientSend\n" +
            "/bea_wls_internal/iiop/ClientRecv\n" +
            "/bea_wls_internal/iiop/ClientLogin\n" +
            "/bea_wls_internal/WLDummyInitJVMIDs\n" +
            "/bea_wls_internal/a2e2gp2r2/x.jsp\n" +
            "/bea_wls_internal/psquare/x.jsp\n" +
            "/bea_wls_internal/iiop/ClientClose\n" +
            "/beanManaged\n" +
            "/certificate\n" +
            "/classes\n" +
            "/classes/\n" +
            "/com\n" +
            "/common\n" +
            "/config\n" +
            "/console\n" +
            "/cookies\n" +
            "/default\n" +
            "/docs51\n" +
            "/domain\n" +
            "/drp-exports\n" +
            "/drp-publish\n" +
            "/dummy\n" +
            "/e2ePortalProject/Login.portal\n" +
            "/ejb\n" +
            "/ejbSimpappServlet\n" +
            "/error\n" +
            "/examplesWebApp/EJBeanManagedClient.jsp\n" +
            "/examplesWebApp/WebservicesEJB.jsp\n" +
            "/examplesWebApp/OrderParser.jsp?xmlfile=C:/bea/weblogic81/samples/server/examples/src/examples/xml/orderParser/order.xml\n" +
            "/examplesWebApp/index.jsp\n" +
            "/examplesWebApp/InteractiveQuery.jsp\n" +
            "/examplesWebApp/SessionServlet\n" +
            "/fault\n" +
            "/file\n" +
            "/file/\n" +
            "/fileRealm\n" +
            "/fileRealm.properties\n" +
            "/getior\n" +
            "/graphics\n" +
            "/helloKona\n" +
            "/helloWorld\n" +
            "/iiop/ClientClose\n" +
            "/iiop/ClientRecv\n" +
            "/iiop/ClientLogin\n" +
            "/iiop/ClientSend\n" +
            "/images\n" +
            "/index\n" +
            "/index.jsp\n" +
            "/internal\n" +
            "/jmssender\n" +
            "/jmstrader\n" +
            "/jspbuild\n" +
            "/jwsdir\n" +
            "/login.jsp\n" +
            "/manifest.mf\n" +
            "/mapping\n" +
            "/mydomain\n" +
            "/myservlet\n" +
            "/page\n" +
            "/patient/login.do\n" +
            "/patient/register.do\n" +
            "/phone\n" +
            "/physican/login.do\n" +
            "/portalAppAdmin/login.jsp\n" +
            "/properties\n" +
            "/proxy\n" +
            "/psquare/x.jsp\n" +
            "/public_html\n" +
            "/servlet\n" +
            "/servletimages\n" +
            "/servlets/\n" +
            "/session\n" +
            "/simpapp\n" +
            "/simple\n" +
            "/simpleFormServlet\n" +
            "/snoop\n" +
            "/survey\n" +
            "/system\n" +
            "/taglib-uri\n" +
            "/uddi\n" +
            "/uddi/uddilistener\n" +
            "/uddiexplorer\n" +
            "/uddilistener\n" +
            "/utils\n" +
            "/web\n" +
            "/web.xml\n" +
            "/weblogic\n" +
            "/weblogic.properties\n" +
            "/weblogic.xml\n" +
            "/webservice\n" +
            "/webshare\n" +
            "/wl_management_internal2/FileDistribution\n" +
            "/wl_management_internal2/Bootstrap\n" +
            "/wl_management_internal2/Admin\n" +
            "/wl_management_internal2/wl_management\n" +
            "/wl_management_internal1/LogfileTail\n" +
            "/wl_management_internal1/LogfileSearch\n" +
            "/wl_management_internal1\n" +
            "/wl_management\n" +
            "/wl_management_internal2\n" +
            "/wliconsole\n" +
            "/_async/AsyncResponseService\n"+
            "/wls-wsat/CoordinatorPortType\n"+
            "/wlserver\n").split("\n");
}
