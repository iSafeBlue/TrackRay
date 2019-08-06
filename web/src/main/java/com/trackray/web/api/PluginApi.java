package com.trackray.web.api;

import com.trackray.web.service.PluginService;
import com.trackray.base.bean.ResultCode;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/plugin")
@Controller
public class PluginApi {

    @Autowired
    private PluginService pluginService;

    /**
     * 列出所有插件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list" )
    public ResultCode list(){
        JSONArray plugins = pluginService.findPlugins();
        return ResultCode.getInstance(200,"正常",plugins);
    }

    /**
     * 调用接口式插件
     * @param map   请求参数
     * @param request   servlet请求对象
     * @param response  servlet响应对象
     * @throws IOException
     */
    @RequestMapping(value = "use")
    public void use(@RequestParam Map<String,String> map , HttpServletRequest request ,HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        if (map==null || map.isEmpty() || !map.containsKey("key")){
            ResultCode code = ResultCode.ERROR("参数不可为空");
            response.getWriter().print(code.toString());
        }else if (map.containsKey("key")){
            String key =  map.get("key");
            pluginService.usePlugin(map , request , response);
        }
    }

    /**
     * MVC类插件调用
     * @param plugin 插件名
     * @param function  功能名
     * @param map   请求参数
     * @param model Model和View对象
     * @param request   servlet请求对象
     * @param response  servlet响应对象
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/mvc/{plugin}/{function}")
    public ModelAndView fetch(
            @PathVariable String plugin,
            @PathVariable(required = false) String function,
            @RequestParam Map<String,String> map ,
            ModelAndView model,
            HttpServletRequest request ,
            HttpServletResponse response) throws IOException {

        ModelAndView modelAndView = pluginService.fetchPlugin(plugin, function, map, model, request, response);

        return modelAndView;
    }


    /* @RequestMapping(value = "use")
    public ResultCode use(@RequestParam Map<String,String> map){

        if (map==null || map.isEmpty()){
            return ResultCode.ERROR("参数不可为空");
        }else if (map.containsKey("key")){
            String key =  map.get("key");
            return pluginService.usePlugin(map);
        }
        return ResultCode.ERROR("参数不可为空");
    }
    */

    /**
     * 多线程插件结果调用接口
     * @param task  taskid
     * @param request   servlet请求
     * @param response  servlet响应
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "result")
    public void result(String task , HttpServletRequest request , HttpServletResponse response) throws IOException {
        switch (request.getParameter("type")) {
            case "html":response.setContentType("text/html");break;
            case "text":response.setContentType("text/plain");break;
            default:response.setContentType("application/json");break;
        }

        if(StringUtils.isNotBlank(task)){
            this.pluginService.getPlugin(task , response);
        }else {
            response.getWriter().print(ResultCode.WARN("请输入task参数"));
        }
    }


    /**
     * 停止多线程插件接口
     * @param task  taskid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "stop")
    public ResultCode stop(String task){

        if (StringUtils.isNotBlank(task)){
            return this.pluginService.killPlugin(task);
        }

        return ResultCode.ERROR;
    }

}
