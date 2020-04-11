package com.trackray.base.plugin;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Option;
import com.trackray.base.store.SettingDTO;
import com.trackray.base.store.SettingRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVC插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/2 16:33
 */
public abstract class MVCPlugin extends CommonPlugin<ModelAndView>{

    protected ModelAndView model;
    protected String function;

    @Autowired
    public SettingRepository settingRepository;

    @Function
    public abstract void index();

    @Function
    public void setting(){
        model.setViewName("../common/setting");

        Option[] options = currentRule().options();
        if (options.length == 0)
        {
            model.addObject("msg","该插件未开启配置功能");
            return;
        }
        List attrs = new ArrayList<Map>();
        for (Option option : options) {
            String name = option.name();
            String description = option.description();
            String formName = option.formName();
            SettingDTO setting = settingRepository.findSettingDTOByFormNameAndPluginKey(formName,currentPlugin().value());
            String optionValue = "";
            if (setting!=null){
                optionValue = setting.getFormValue();
            }else {
                optionValue = option.defaultValue();
            }
            Map attr = new HashMap<String,String>();
            attr.put("value", optionValue);
            attr.put("name", name);
            attr.put("description", description);
            attr.put("formName", formName);
            attrs.add(attr);
        }
        if (settingRepository.findAllByPluginKey(currentPlugin().value()).isEmpty())
            model.addObject("msg","该插件未初始化配置，请填写配置项后提交。");
        model.addObject("attrs",attrs);
    }

    @Function
    public void doSetting(){
        System.out.println(param);
        boolean flag = false;
        String pluginKey = currentPlugin().value();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            SettingDTO setting = settingRepository.findSettingDTOByFormNameAndPluginKey(key, pluginKey);
            if (setting==null){//首次保存
                setting = new SettingDTO();
                setting.setPluginKey(pluginKey);
                setting.setFormName(key);
                setting.setFormValue(value);
                settingRepository.save(setting);
            }else{
                    setting.setFormValue(value);
                    settingRepository.save(setting);
            }
            flag = true;

        }
        if (!flag)
            write("{\"msg\":\"保存失败，未找到配置项\"}");
        else
            write("{\"msg\":\"保存配置成功\"}");

    }

    public List<SettingDTO> findOptions(){
        return settingRepository.findAllByPluginKey(currentPlugin().value());
    }

    @Override
    public boolean check(Map<String, Object> param) {
        return true;
    }

    @Override
    public ModelAndView start() {
        boolean flag = false;
        Method target = null;
        Method[] methods = this.getClass().getMethods();

        for (Method method : methods) {
            Function func = method.getAnnotation(Function.class);
            if (func!=null)
            {
                String value = func.value();
                if (StringUtils.isEmpty(value))
                    value = method.getName();//如果不填写function注解的value则认为mapping是方法名
                if(function.equals(value)){
                    flag = true;
                    target = method;
                    break;
                }
            }
        }
        String pluginKey = this.currentPlugin().value();//插件对象在spring中的id

        if (flag){
            try {
                target.invoke(this, null);
                model.setViewName(pluginKey.concat("/" + model.getViewName())); // 如果功能执行正常 那在viewName则为：插件id/视图名
            } catch (Exception e) {
                model.setViewName("common/error");
                model.addObject("msg",e.getMessage());
            }

        }else{
            try {
                this.getClass().getMethod("index",null).invoke(this, null);//执行默认的index方法
                model.setViewName(pluginKey.concat("/"+model.getViewName())); // 如果功能执行正常 那在viewName则为：插件id/视图名
            } catch (Exception e) {
                model.setViewName("common/error");
                model.addObject("msg",e.getMessage());
            }
        }

        /*
            如果流已经被使用，就强制使用反射将 usingOutputStream 重置，不让他抛异常。
         */
        if (response.isCommitted()){

            try {
                Field field = this.response.getClass().getDeclaredField("response");
                field.setAccessible(true);
                Object response = field.get(this.response);

                Field usingOutputStream = response.getClass().getDeclaredField("usingOutputStream");
                usingOutputStream.setAccessible(true);
                usingOutputStream.setBoolean(response,false);
            }catch (Exception e){
                log.error("重置stream异常",e);
            }

        }

        return model;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setModel(ModelAndView model) {
        this.model = model;
    }


}
