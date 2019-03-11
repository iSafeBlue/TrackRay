package com.trackray.base.controller;

import com.trackray.base.attack.Crawler;
import com.trackray.base.attack.SQLMap;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.VulnType;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.utils.SysLog;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Controller("crawlerController")
public class CrawlerController {

    @Autowired
    private DispatchController dispatchController;


    public void crawler(CrawlerPage crawler) {
        String base = crawler.getBase();
        Crawler spider = new Crawler(crawler,dispatchController.getAppContext());
        spider.ERROR_MAX = 15;//设定最多15次超时
        spider.SPIDER_MAX = crawler.getTask().getMaxSpider();
        spider.addURL(crawler.getRequest().getUrl(),0);
        spider.begin();
        List<SQLMap> sqlmaps = spider.getSqlmaps();
        //crawler.getTask().getResult().getSenseInfo().getOther().put("sqlmaps",sqlmaps);
        Runnable sqlmapThread = new Runnable() {
            @Override
            public void run()  {
                List<Vulnerable> vuls = new ArrayList<>();
                int size = sqlmaps.size();
                if (size<1){
                    return;
                }
                for (int i=0;i<3;i++){
                    try {
                        Thread.currentThread().sleep(25000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int j=0;j<sqlmaps.size();j++){
                        SQLMap sqlmap = sqlmaps.get(j);
                        int status = sqlmap.status();
                        if (status == Constant.SQLMap.STATUS_CREATED){
                            sqlmap.start();
                        }
                        if (status == Constant.SQLMap.STATUS_END){
                            sqlmaps.remove(j);
                            JSONObject data = sqlmap.data();
                            if (data.getJSONArray("data").size()>0){
                                Vulnerable build = Vulnerable.builder().vulType(VulnType.SQL_INJECTION.getType())
                                        .request(data.toString()).vulName(VulnType.SQL_INJECTION.getName()).description("检测到sql注入").build();
                                if (!crawler.getTask().getResult().getItems().get(base).getVulns().contains(build))
                                {
                                    crawler.getTask().getResult().getItems().get(base).getVulns().add(build);
                                }
                            }
                        }

                        if (i==2 && status != Constant.SQLMap.STATUS_END){
                            sqlmap.stop();
                        }

                    }

                }
                SysLog.info("sqlmap检测完毕");
                return;
            }
        };

        crawler.getTempData().put("sqlmap",sqlmapThread);
    }
}
