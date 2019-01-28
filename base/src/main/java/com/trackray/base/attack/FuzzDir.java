package com.trackray.base.attack;

import com.trackray.base.bean.Task;
import com.trackray.base.enums.Language;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Deprecated
public class FuzzDir implements Runnable {

    private Task task;
    private String dir;
    private HttpClient httpClient = new HttpClient();
    private int maxSucc = 50;
    private String target;
    public FuzzDir(Task task, String target, String dir) {
        this.task = task;
        this.dir = dir;
        this.target = target;
    }


    public static void fuzz(Task task ,String target , ExecutorService exec) {

        for (String dir : Payload.dirPayload) {
            if (StringUtils.isNotBlank(dir)){
                exec.execute(new FuzzDir(task, target, dir));
            }
        }

    }

    @Override
    public void run() {
        if (task.getResult().getItems().get(target).getSystemInfo().getDirs().size() >= maxSucc )
        {
            return;
        }

        Language lang = task.getResult().getItems().get(target).getSystemInfo().getLanguage();
        if (dir.contains("%EXT%")) {
            dir = dir.replaceAll("%EXT%", lang != null && lang != Language.OTHER ? lang.getSuffix() : "php");
        }
        try {
            String link = target.concat(dir);
            ResponseStatus res = httpClient.get(link);
            if (res.getStatusCode()!=404){
                SysLog.info("扫描到路径 dir "+link);

                task.getResult().getItems().get(target).getSystemInfo().getDirs().put(link,res.getStatusCode());
            }
        } catch (Exception e) {

        }

    }
}
