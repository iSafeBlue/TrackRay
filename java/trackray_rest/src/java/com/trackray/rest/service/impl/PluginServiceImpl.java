package com.trackray.rest.service.impl;

import com.google.common.cache.Cache;
import com.trackray.rest.service.PluginService;
import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.plugin.CommonPlugin;
import com.trackray.scanner.plugin.WebSocketPlugin;
import com.trackray.scanner.utils.StrUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    private ConcurrentHashMap<String,Future<AbstractPlugin>> pluginPool;

    @Override
    public JSONArray findPlugins(){
        WebApplicationContext context = getContext();
        Map<String, Object> bean = context.getBeansWithAnnotation(Plugin.class);
        JSONArray arr = new JSONArray();
        for (Map.Entry<String, Object> entry : bean.entrySet()) {
            JSONObject obj = new JSONObject();
            AbstractPlugin plugin = (AbstractPlugin) entry.getValue();
            Rule rule = plugin.getClass().getAnnotation(Rule.class);
            Plugin p = plugin.getClass().getAnnotation(Plugin.class);
            obj.put("plugin_key",entry.getKey());

            JSONObject base = new JSONObject();
            base.put("author",p.author());
            base.put("title",p.title());
            base.put("desc",p.desc());
            base.put("link",p.link());
            obj.put("base_info",base);

            JSONObject rules = new JSONObject();
            rules.put("sync",rule.sync());
            rules.put("params",rule.param());
            rules.put("enable",rule.enable());
            rules.put("websocket",rule.websocket());
            obj.put("plugin_rule",rules);
            arr.add(obj);
        }
        return arr;
    }

    @Override
    public void usePlugin(Map<String, String> map, HttpServletResponse response) throws IOException {

        String key = map.get("key");
        if (!getContext().containsBean(key)){
            response.getWriter().print(ResultCode.ERROR("插件不存在"));
            return;
        }
        CommonPlugin bean = null;
        try {
            bean = (CommonPlugin) getBean(key);
        } catch (Exception e) {
            response.getWriter().print(ResultCode.ERROR("插件不合法"));
            return;
        }
        if (bean!=null && bean instanceof CommonPlugin){
        }else {
            response.getWriter().print(ResultCode.ERROR("插件不存在"));
            return;
        }

        Rule rule = bean.getClass().getAnnotation(Rule.class);
        if (!rule.enable()){
            response.getWriter().print(ResultCode.WARN("该插件未启用"));
            return;
        }
        if (rule.websocket()){
            response.getWriter().print(ResultCode.WARN("该插件类型为websocket"));
            return;
        }
        try {
            bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (rule.param().length>0){
            for (String s : rule.param()) {
                if (!map.containsKey(s)){
                    response.getWriter().print(ResultCode.WARN("请填写参数:".concat(s)));
                    return;
                }
            }
        }

        bean.setParam(map);
        bean.response = response;
        String type = rule.type().getValue();
        String charset = rule.charset().getValue();
        String value = String.format("%s;%s", type, charset);
        response.setContentType(value);

        if (StringUtils.isNotBlank(rule.filename())){
            response.setHeader("Content-Disposition","attachment;filename="+rule.filename());
        }

        if (rule.headers().length>0){
            for (String header : rule.headers()) {
                if (header.contains(":")){
                    int i = header.indexOf(":");
                    String k = header.substring(0, i);
                    String v = header.substring(i + 1, header.length());
                    response.addHeader(k,v);
                }
            }

        }
        if (rule.sync()){
            String rand = UUID.randomUUID().toString();
            ExecutorService singerPool = Executors.newSingleThreadExecutor();
            Future<AbstractPlugin> submit = singerPool.submit(bean);
            pluginPool.put(rand,submit);
            JSONObject next = new JSONObject();
            next.put("task",rand);
            next.put("method","result");
            response.getWriter().print(ResultCode.SUCCESS(next));
            return;
        }else{
            AbstractPlugin executor = bean.executor();
            Object obj = executor.result();
            response.getWriter().print(obj==null?"参数不正确":obj);
            return;
        }

    }

    @Override
    public void getPlugin(String task, HttpServletResponse response) throws IOException {

        if (!this.pluginPool.containsKey(task)){
            response.getWriter().print(ResultCode.WARN("该任务不存在"));
            return;
        }

        Future<AbstractPlugin> future = this.pluginPool.get(task);
        AbstractPlugin obj = null;
        try {
            obj =  future.get();
        } catch (Exception e) {
            response.getWriter().print(ResultCode.ERROR("任务未结束，请等待"));
            return;
        }

        if (obj!=null){
            Object result = obj.result();
            this.pluginPool.remove(task);
            response.getWriter().print(result==null?"参数不正确":result);
            return;
        }else{
            response.getWriter().print(ResultCode.ERROR);
            return;
        }

    }

    @Override
    public boolean usePlugin(WebSocketSession session, String key) throws IOException {
        if (!getContext().containsBean(key)){
            session.sendMessage(new TextMessage("插件不存在"));
            return false;
        }
        AbstractPlugin bean = null;
        try {
            bean = (AbstractPlugin) getBean(key);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("插件不合法"));
            return false;
        }
        if (bean!=null && bean instanceof AbstractPlugin){
        }else {
            session.sendMessage(new TextMessage("插件不合法"));
            return false;
        }

        Rule rule = bean.getClass().getAnnotation(Rule.class);
        if (!rule.enable()){
            session.sendMessage(new TextMessage("插件未启用"));
            return false;
        }
        if (!rule.websocket()) {
            session.sendMessage(new TextMessage("该插件不支持websocket"));
            return false;
        }

        JSONObject ruleObj = rule(key);
        session.sendMessage(new TextMessage(StrUtils.formatJson(ruleObj.toString())));
        return true;
    }

    @Override
    public WebSocketPlugin buildPlugin(WebSocketSession session, Map<String, String> map, String key) {

        WebSocketPlugin bean = getContext().getBean(key, WebSocketPlugin.class);

        try {
            bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        bean.setParam(map);
        bean.setSession(session);
        return bean;
    }

    @Override
    public ResultCode killPlugin(String task) {
        if (!this.pluginPool.containsKey(task)){
            return ResultCode.ERROR("任务不存在");
        }else{
            Future<AbstractPlugin> future = this.pluginPool.remove(task);
            if (future.isDone()){
                Object o = null;
                try {
                    o = future.get().result();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return ResultCode.SUCCESS(o);
            }else{
                boolean cancel = future.cancel(true);
                return ResultCode.SUCCESS(cancel);
            }

        }
    }

    private WebApplicationContext getContext(){
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        return context;
    }
    private Object getBean(String bean){
        return  getContext().getBean(bean,AbstractPlugin.class);
    }

    public JSONObject rule(String bean){
        JSONObject result = new JSONObject();
        if (!getContext().containsBean(bean)){
            return result;
        }
        AbstractPlugin plugin = null;
        try {
            plugin = (AbstractPlugin) getBean(bean);
        }catch (Exception e){
            return result;
        }
        Rule rule = plugin.getClass().getAnnotation(Rule.class);
        Plugin p = plugin.getClass().getAnnotation(Plugin.class);
        JSONObject obj = new JSONObject();
        obj.put("plugin_name",bean);

        JSONObject base = new JSONObject();
        base.put("author",p.author());
        base.put("title",p.title());
        base.put("desc",p.desc());
        base.put("link",p.link());
        obj.put("base_info",base);

        JSONObject rules = new JSONObject();
        rules.put("sync",rule.sync());
        rules.put("params",rule.param());
        rules.put("enable",rule.enable());
        rules.put("websocket",rule.websocket());
        obj.put("plugin_rule",rules);
        return obj;
    }

}
