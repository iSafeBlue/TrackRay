package com.trackray.scanner.attack;

import com.trackray.scanner.bean.ResultItem;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.httpclient.HttpClient;
import com.trackray.scanner.utils.ReUtils;
import com.trackray.scanner.utils.SysLog;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public class FuzzDomain implements Runnable{
    private Task task;
    private String prefix;
    private String root;
    private HttpClient httpClient = new HttpClient();
    private int maxSucc = 30;

    public FuzzDomain(Task task, String root, String prefix) {
        this.task = task;
        this.prefix = prefix;
        this.root = root;
    }

    public static void fuzz(Task task) {
        ExecutorService exec = task.getExecutor();
        String domain = ReUtils.getDomain(task.getTargetStr());
        for (String prefix : Payload.domainPayload) {
            if (StringUtils.isNotBlank(prefix)) {
                exec.execute(new FuzzDomain(task,domain,prefix));
            }
        }

    }

    @Override
    public void run() {
        if (task.getResult().getAssets().getChildDomain().size() >= maxSucc){
            return;
        }
        String domain = String.format("%s.%s", prefix, root);
        String ip =null;
        try {
            ip = InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException e) {
            return;
        }
        if (ReUtils.isIp(ip) && !"127.0.0.1".equals(ip)){
            SysLog.info("扫描到子域名 "+domain);
            task.getResult().getAssets().getChildDomain().put(domain,ip);
            task.getTargets().add("http://".concat(domain)+"/");
            //task.getTargets().add("http://".concat(ip));

            task.getResult().getItems().put("http://".concat(domain)+"/",new ResultItem());
            //task.getResult().getItems().put("http://".concat(ip),new ResultItem());
        }

    }
}
