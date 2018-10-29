package com.trackray.scanner.controller;

import com.trackray.scanner.attack.Crawler;
import com.trackray.scanner.attack.SQLMap;
import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.enums.VulnType;
import com.trackray.scanner.bean.Vulnerable;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.utils.SysLog;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Controller("crawlerController")
public class CrawlerController {

    public void crawler(CrawlerPage crawler) {
        String base = crawler.getBase();
        Crawler spider = new Crawler(crawler);
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
                                Vulnerable build = new Vulnerable.VulnBuilder().vulnType(VulnType.SQL_INJECTION)
                                        .response(data.toString()).message("检测到SQL注入").build();
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
