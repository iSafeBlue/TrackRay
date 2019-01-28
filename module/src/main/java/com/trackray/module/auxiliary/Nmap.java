package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Task;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.SystemOS;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ReUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(enable = false)
@Plugin(value="nmap",title = "NMAP模块" , author = "blue" )
public class Nmap extends CommonPlugin {
    private Task task;
    private String target;
    private String ip;

    private static String nmap = "nmap";
    @Override
    public boolean check(Map param) {
        if (param.containsKey("task") )
            task = (Task) param.get("task");
        target = param.get("target").toString();

        if (ReUtils.isIp(target)){
            ip = ReUtils.getIp(target);
        }else{
            ip = DomainUtils.getHostAddress(DomainUtils.getHost(target));
        }
        return true;
    }

    @Override
    public Object start() {
        Shell shell = shell();
        try {
            shell.block(true).target(nmap).exec("-O",this.ip);
        } catch (IOException e) {
            nmap = Constant.NmapComm.NMAP_DIR+nmap;
            e.printStackTrace();
        }
        String portStr = shell.readAll();
        List<Map<Integer, String>> ports = scanPorts(portStr);
        if (task!=null)
        {
            for (Map<Integer, String> map : ports) {
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    task.getResult().getItems().get(target).getSystemInfo().getPorts().add(new PortEntity(entry.getKey(),entry.getValue()));
                }

            }
            scanOS(portStr);
        }
        return ports.toString();
    }

    private List<Map<Integer,String>> scanPorts(String portStr){
        ArrayList<Map<Integer, String>> maps = new ArrayList<>();
        String[] ps = portStr.split("\n");
        for(String s : ps){
            if (task!=null && task.getResult().getItems().get(target).getSystemInfo().getPorts().size() >= 50){
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
                maps.add(new HashMap<Integer,String>(){{put(port,service);}});
            }
        }
        return maps;
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
                            task.getResult().getItems().get(target).getSystemInfo().setSystemOS(new SystemEntity(systemOS,os));
                        }else if (i==values.length-1){
                            task.getResult().getItems().get(target).getSystemInfo().setSystemOS(new SystemEntity(SystemOS.OTHER,os));
                        }
                    }

                }
            }
        }
    }
}
