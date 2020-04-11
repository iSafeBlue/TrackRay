package com.trackray.module;

import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Task;
import com.trackray.base.utils.IOUtils;
import com.trackray.module.inner.FuckChildDomain;
import com.trackray.module.inner.Nmap;
import com.trackray.module.inner.NmapInner;
import com.trackray.module.inner.SgkSearch;
import com.trackray.module.plugin.webapp.dedecms.DedeFindManage;
import com.trackray.module.plugin.webapp.spring.SpringCloudServerConfigFileRead;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/24 13:40
 */
public class Verify {



    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        NmapInner nmapInner = new NmapInner();


       /* SpringCloudServerConfigFileRead read = new SpringCloudServerConfigFileRead();
        read.addParam("key","value");

        Object result = read.executor().result();// 阻塞
        System.out.println(result);

        FutureTask task = new FutureTask<>(read); // 非阻塞

        task.run();
        while (!task.isDone()){
            Object o = task.get();
            System.out.println(o);
            break;
        }
        */

    }



}
