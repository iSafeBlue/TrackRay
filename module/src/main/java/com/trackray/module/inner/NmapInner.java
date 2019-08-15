package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/9 16:43
 */
@Plugin(value = "nmapHelper" , title = "nmap for java" , author = "浅蓝")
@Rule
public class NmapInner extends InnerPlugin<NmapInner>{

    private static final String[] FILES = {"nmap","/usr/bin/nmap"};

    private File outfile;
    private List<String > commands = new ArrayList<>();
    private String target;
    private int timeout = 0;//分钟


    @Override
    public void process() {

        for (String s : FILES) {
            try {
                Shell shell = shell().block(true);
                shell.exec(s.concat(" ").concat("--version"));
                String content = shell.readAll();
                if (StringUtils.contains(content,"nmap.org")) {
                    result = this;
                    commands.add(s);
                    break;
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    public class NmapPort{
        private String connectModel = "-sS";
        private String port;
        private List<String> otherOptions = new ArrayList<>();
        public NmapPort sS(){
            connectModel = "-sS";
            return this;
        }
        public NmapPort sT(){
            connectModel = "-sT";
            return this;
        }
        public NmapPort sU(){
            connectModel = "-sU";
            return this;
        }
        public NmapPort fast(){
            port = "-F";
            return this;
        }
        public NmapPort sN(){
            connectModel = "-sN";
            return this;
        }

        public NmapPort sX(){
            connectModel = "-sX";
            return this;
        }
        public NmapPort sA(){
            connectModel = "-sA";
            return this;
        }
        public NmapPort port(String... ports){
            port = "-p "+ String.join(",",ports);
            return this;
        }
        public NmapPort port(int start,int end){
            port = "-p "+ ( start+"-"+end);
            return this;
        }
        public NmapPort port(int port){
            return port(String.valueOf(port));
        }

        public NmapPort IPv6(){
            otherOptions.add("-6");
            return this;
        }

        public NmapPort openFireFuckfuckBiubiubiuBoom(){
            otherOptions.add("-A");
            return this;
        }
        public NmapPort sV(){
            otherOptions.add("-sV");
            return this;
        }
        public NmapPort sR(){
            otherOptions.add("-sR");
            return this;
        }
        public NmapPort versionLight(){
            otherOptions.add("--version-light");
            return this;
        }
        public NmapPort versionAll(){
            otherOptions.add("--version-all");
            return this;
        }
        public NmapPort T2(){
            otherOptions.add("-T2");
            return this;
        }
        public NmapPort T3(){
            otherOptions.add("-T3");
            return this;
        }
        public NmapPort T4(){
            otherOptions.add("-T4");
            return this;
        }
        public NmapPort T5(){
            otherOptions.add("-T5");
            return this;
        }

        public NmapPort O(){
            otherOptions.add("-O");
            return this;
        }

        public NmapPort versionTrace(){
            otherOptions.add("--version-trace");
            return this;
        }

        public NmapTarget next(){
            if (this.connectModel!=null)
                NmapInner.this.commands.add(this.connectModel);
            if (this.port!=null)
                NmapInner.this.commands.add(this.port);
            if (this.otherOptions!=null)
               NmapInner.this.commands.addAll(this.otherOptions);
            NmapInner.this.commands.add("--open");
            return new NmapTarget();
        }

    }

    public class NmapHost{

    }

    public class NmapTarget{
        private String target;

        public NmapInner target(String host){
            this.target = host;
            NmapInner.this.target = host;
            return NmapInner.this;
        }

    }


    public interface NmapModel{
        NmapPort port();
        NmapHost host();
    }


    public NmapModel open(){
        commands.clear();
        commands.add("nmap");
        return new NmapModel() {
            @Override
            public NmapPort port() {
                return new NmapPort();
            }

            @Override
            public NmapHost host() {
                return new NmapHost();
            }
        };
    }
    public interface NmapOutput{
        NmapInner stdout();
        NmapInner xmlout();
        NmapInner grepout();
        NmapInner allout();
    }
    public NmapOutput output(File file){
        NmapInner.this.outfile = file;
        String path = file.getAbsolutePath();
        return new NmapOutput(){
            @Override
            public NmapInner stdout() {
                NmapInner.this.commands.add("-oN");
                NmapInner.this.commands.add(path);
                return NmapInner.this;
            }

            @Override
            public NmapInner xmlout() {
                NmapInner.this.commands.add("-oX");
                NmapInner.this.commands.add(path);
                return NmapInner.this;
            }

            @Override
            public NmapInner grepout() {
                NmapInner.this.commands.add("-oG");
                NmapInner.this.commands.add(path);
                return NmapInner.this;
            }

            @Override
            public NmapInner allout() {
                NmapInner.this.commands.add("-oA");
                NmapInner.this.commands.add(path);
                return NmapInner.this;
            }
        };
    }

    public NmapInner timeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    private Shell nmapShell;
    public void run(){
        commands.add(this.target);
        String[] commandArr = this.commands.toArray(new String[]{});

        nmapShell = shell().block(timeout>0?false:true);
        try {
            nmapShell.exec(String.join(" ",commandArr));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }


    }

    public String getResultContent(){

        if (timeout>0){
            int num = 6 * timeout;
            while (true){
                if (num <= 0){
                    log.error("nmap 扫描超时");
                    break;
                }
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                    break;
                }
                num--;
            }

        }

        String content = nmapShell.readAll();
        nmapShell.destroy();
        String filecontent = readResult();
        return filecontent.isEmpty()?content:filecontent;

    }

    private String readResult(){
        if (outfile!=null && outfile.exists() && outfile.length()>0){
            try {
                return FileUtils.readFileToString(outfile);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return "";
    }

}
