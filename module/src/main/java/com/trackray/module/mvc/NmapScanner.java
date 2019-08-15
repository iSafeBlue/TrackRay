package com.trackray.module.mvc;

import com.alibaba.fastjson.JSONObject;
import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.SystemOS;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.utils.StrUtils;
import com.trackray.module.inner.NmapInner;
import com.trackray.module.mvc.gps.GpsData;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/9 20:03
 */
@Plugin(value = "nmapScanner",title = "Nmap扫描" , author = "浅蓝")
@Rule(auth = true)
public class NmapScanner extends MVCPlugin {

    @Autowired
    private NmapRepository nmapRepository;

    @Autowired
    private NmapInner nmapInner;

    @Value("${temp.dir}")
    private String tempdir;
    private File nmapDir;
    @Override
    public boolean check(Map<String, Object> param) {
        nmapDir = new File(tempdir, "nmap");
        if (!nmapDir.exists())
            nmapDir.mkdirs();
        return nmapDir.exists();
    }

    @Override
    public void index() {
        HashMap<String, String> map = new HashMap<>();
        for (NmapData nmapData : nmapRepository.findAll()) {
            map.put(nmapData.getTaskid(),nmapData.getName());
        }
        model.addObject("tasks",map);
        model.setViewName("index");
    }

    @Function
    public void scan(){
        String host = param.getOrDefault("host", "").toString();
        String cusPort = param.getOrDefault("cusPort", "").toString();
        String scanPort = param.getOrDefault("scanPort", "").toString();
        String scanHost = param.getOrDefault("scanHost", "").toString();

        if (scanPort.equals("on")){
            NmapInner.NmapPort port = nmapInner.open().port();
            if (param.containsKey("tcp"))
                port.sT();
            else if (param.containsKey("udp"))
                port.sU();
            else if (param.containsKey("syn"))
                port.sS();

            if (param.containsKey("fast")){
                port.fast();
            }else if (param.containsKey("deep")){
                port.port(1,65535);
            }else if (param.containsKey("cus") && cusPort.length()>0){
                port.port(cusPort);
            }else{
                port.fast();
            }

            if (param.containsKey("sv"))
                port.sV();
            if (param.containsKey("o"))
                port.O();
            if (param.containsKey("a"))
                port.openFireFuckfuckBiubiubiuBoom();
            if (param.containsKey("t4"))
                port.T4();

            NmapInner target = port.next().target(host);
            String taskid = UUID.randomUUID().toString();
            File nmapFile = new File(nmapDir, taskid);
            target.output(nmapFile).xmlout().run();

            NmapData nmapData = new NmapData();
            nmapData.setTaskid(taskid);
            nmapData.setFilename(nmapFile.getAbsolutePath());
            nmapData.setName(host);
            NmapData save = nmapRepository.save(nmapData);
            if (save!=null)
                write("success");
            return;
        }else if (scanHost.equals("on")){

        }
        write("error");
    }
    @Function
    public void data(){
        String task = param.getOrDefault("task", "").toString();
        if (!task.isEmpty()){
            NmapData data = nmapRepository.findNmapDataByTaskid(task);
            if (data!=null){
                String filename = data.getFilename();
                File file = new File(filename);
                if (file.exists()&&file.length()>0){
                    try {
                        String con = FileUtils.readFileToString(file);
                        parseResult(con);
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                    }
                    return;
                }else{
                    write("未完成扫描");
                    return;
                }

            }
        }
        write("未找到任务");
    }
    @Function
    public void del(){
        String task = param.getOrDefault("task", "").toString();
        if (!task.isEmpty()){
            NmapData data = nmapRepository.findNmapDataByTaskid(task);
            new File(data.getFilename()).delete();
            nmapRepository.delete(data);
            write("删除成功");
        }
        write("删除失败");
    }

    private boolean checkNotBlank(Elements elements){
        return elements!=null && !elements.isEmpty();
    }

    private void parseResult(String fileContent) {

        JSONObject json = new JSONObject();

        Set<PortEntity> portset = new HashSet<>();

        Document document = Jsoup.parse(fileContent);

        Elements ports = document.getElementsByTag("port");

        Elements os = document.getElementsByTag("osmatch");


        if (checkNotBlank(ports)){
            for (Element port : ports) {
                PortEntity.PortEntityBuilder builder = PortEntity.builder();

                String protocol = port.attr("protocol");
                String portid = port.attr("portid");

                builder.protocol(protocol).port(Integer.parseInt(portid));

                Elements service = port.getElementsByTag("service");
                Elements script = port.getElementsByTag("script");

                if (checkNotBlank(service)){
                    Element first = service.first();
                    String name = first.attr("name");
                    String product = first.attr("product");
                    String version = first.attr("version");
                    builder.service(name);
                    builder.product(product);
                    builder.version(version);
                }

                if (checkNotBlank(script)){
                    HashMap<String, String> map = new HashMap<>();
                    for (Element scr : script) {
                        String id = scr.attr("id");
                        String output = scr.attr("output");
                        if (StringUtils.isNoneEmpty(id,output)){
                            map.put(id,output);
                        }
                    }
                    builder.banner(map);
                }

                PortEntity entity = builder.build();

                portset.add(entity);
            }
        }

        if (checkNotBlank(os)){
            Element first = os.first();
            String name = first.attr("name");
            SystemEntity.SystemEntityBuilder builder = SystemEntity.builder();
            if (name.toLowerCase().contains("inux")){
                builder.os(SystemOS.LINUX);
            }else if(name.toLowerCase().contains("indows")){
                builder.os(SystemOS.WINDOWS);
            }else{
                builder.os(SystemOS.OTHER);
            }
            builder.version(name);
            SystemEntity entity = builder.build();

            json.put("system",(entity));
        }

        json.put("port",(portset));

        write(StrUtils.formatJson(json.toJSONString()));
    }



}
@Entity
@Data
class NmapData implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String taskid;
    private String filename;
    private String name;

}

interface NmapRepository extends CrudRepository<NmapData, Long> {
    public NmapData findNmapDataByTaskid(String taskid);
}
