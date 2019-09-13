package com.trackray.module.mvc.mindMap;

import com.alibaba.fastjson.JSON;
import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.utils.CheckUtils;
import com.trackray.module.mvc.gps.GpsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/9/12 20:18
 */
@Plugin(value = "mindMap",title = "思维导图制作" , author = "浅蓝" , desc = "改自 https://github.com/ondras/my-mind")
@Rule(auth = true)
public class MindMap extends MVCPlugin {

    @Autowired
    private MindMapRepository mindMapRepository;

    @Override
    public void index() {
        List datas = new ArrayList<MindMapData>();
        for (MindMapData data : mindMapRepository.findAll()) {
            datas.add(data);
        }
        model.addObject("datas",datas);
        model.setViewName("index");

    }

    public static final String MIND_TEMPLATE = "{\"root\":{\"id\":\"kyjgpfrx\",\"text\":\"%s\",\"layout\":\"map\",\"children\":[{\"id\":\"czkiymdg\",\"text\":\"Item 1\",\"side\":\"right\"},{\"id\":\"qxpcwptr\",\"text\":\"Item 2\",\"side\":\"left\"}]}}";

    @Function(value = "create",desc = "创建思维导图")
    public void create(){
        String title = param.getOrDefault("title","").toString();
        if (title.isEmpty()){
            model.setViewName("../common/default");
            model.addObject("msg", "error");
        }else{
            MindMapData mindMapData = new MindMapData();
            String data = String.format(MIND_TEMPLATE, title);
            mindMapData.setData(data);
            mindMapData.setTitle(title);

            MindMapData savedMapData = mindMapRepository.save(mindMapData);
            model.setViewName("../common/default");
            model.addObject("msg", JSON.toJSONString(savedMapData));
        }
    }

    @Function(value = "mind",desc = "查看思维导图")
    public void mind(){
        long id = Long.parseLong(param.get("id").toString());
        MindMapData data = mindMapRepository.findById(id).get();

        String js = "var json = "+data.getData()+";\r\n"+
                "MM.UI.Backend._loadDone(json);";

        model.setViewName("mind");
        model.addObject("js",js);
        model.addObject("title",data.getTitle());
        model.addObject("id",data.getId());
    }

    @Function(value = "save",desc = "保存思维导图")
    public void save(){
        String data = param.get("data").toString();
        long id = Long.parseLong(param.get("id").toString());
        if (CheckUtils.isJson(data)){
            MindMapData mapData = mindMapRepository.findById(id).get();
            mapData.setData(data);
            MindMapData savedMindMap = mindMapRepository.save(mapData);
            if (savedMindMap!=null)
                write("保存成功");
            else
                write("保存失败");

        }else {
            write("数据格式有误");
        }
    }

    @Function(value = "delete",desc = "删除思维导图")
    public void delete(){
        long id = Long.parseLong(param.get("id").toString());
        mindMapRepository.deleteById(id);
        write("success");
    }


}
interface MindMapRepository extends CrudRepository<MindMapData, Long> {


}