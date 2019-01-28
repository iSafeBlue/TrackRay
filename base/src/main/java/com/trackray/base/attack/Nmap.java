package com.trackray.base.attack;

import com.trackray.base.bean.Task;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.SystemOS;
import com.trackray.base.handle.Shell;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Deprecated
public class Nmap implements Runnable{

    private Task task ;
    private String ip ;
    private String base;
    public Nmap(Task task ,String base, String ip){
        this.task = task ;
        this.ip = ip ;
        this.base = base;
    }
    public Nmap(){ }
    public static void nmap(Task task, String base ,ExecutorService exec) {
        String ip = task.getResult().getIpInfo().getIp();
        if(ip!=null){
            exec.execute(new Nmap(task ,base, task.getResult().getIpInfo().getIp()));
        }

    }

    public Nmap(String ip){
        this.ip = ip;
    }

    public static void main(String[] args) {
    }
    public static String exec(String ...param){
        Shell shell = new Shell();
        try {
            shell.block(true).target("nmap").exec(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shell.readAll();
    }
    public HashSet<Integer> scanPort(String  param){
        HashSet<Integer> set = new HashSet<>();
        Shell shell = new Shell();
        try {
            shell.block(true).target("nmap").exec(param,this.ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String portStr = shell.readAll();
        String[] ps = portStr.split("\n");
        for(String s : ps){
            if (set.size() >= 50){
                break;
            }
            if(s.contains("open")){
                String[] one = s.split("open");
                if(!one[0].matches("^\\d+/.*")){
                    continue;
                }
                String po = one[0].split("/")[0].trim();
                Integer port = Integer.parseInt(po);
                set.add(port);
            }
        }
        return set;
    }
    private void scan(){


        Shell shell = new Shell();

        try {
            shell.block(true).target("nmap").exec("-O",this.ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String portStr = shell.readAll();
        scanPorts(portStr);
        scanOS(portStr);
    }

    private void scanPorts(String portStr){

        String[] ps = portStr.split("\n");
        for(String s : ps){
            if (task.getResult().getItems().get(base).getSystemInfo().getPorts().size() >= 50){
                break;
            }
            if(s.contains("open")){
                String[] one = s.split("open");
                if(!one[0].matches("^\\d+/.*")){
                    continue;
                }
                String po = one[0].split("/")[0].trim();
                Integer port = Integer.parseInt(po);
                String service = one[1].trim();
                task.getResult().getItems().get(base).getSystemInfo().getPorts().add(new PortEntity(port,service));
            }
        }
    }

    private void scanOS(String portStr){
        String[] ps = portStr.split("\n");
        for(String s : ps){
            if(s.contains("Aggressive OS guesses")){
                String[] sp1 = s.split(":");
                String[] sp2 = sp1[1].split(",");
                if(sp2.length > 0 && sp2[0].contains("%")){
                    String os = sp2[0].substring(0,sp2[0].indexOf("(")).trim();
                    SystemOS[] values = SystemOS.values();
                    for (int i=0;i<values.length;i++){
                        SystemOS systemOS = values[i];
                        String lower = systemOS.name().toLowerCase();
                        if (os.toLowerCase().contains(lower)){
                            task.getResult().getItems().get(base).getSystemInfo().setSystemOS(new SystemEntity(systemOS,os));
                        }else if (i==values.length-1){
                            task.getResult().getItems().get(base).getSystemInfo().setSystemOS(new SystemEntity(SystemOS.OTHER,os));
                        }
                    }

                }
            }
        }
    }

    @Override
    public void run() {
        scan();
    }

}
