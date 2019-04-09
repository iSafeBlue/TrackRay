package com.trackray.base.plugin;

import com.trackray.base.annotation.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
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

    @Function
    public abstract void index();

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
                model.setViewName(pluginKey.concat("/"+model.getViewName())); // 如果功能执行正常 那在viewName则为：插件id/视图名
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

        return model;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setModel(ModelAndView model) {
        this.model = model;
    }


}
