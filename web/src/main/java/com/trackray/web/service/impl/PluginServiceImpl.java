package com.trackray.web.service.impl;

import com.trackray.base.annotation.Param;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.web.service.PluginService;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.controller.DispatchController;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.web.utils.ServletUtils;
import com.trackray.web.utils.wagu.Block;
import com.trackray.web.utils.wagu.Board;
import com.trackray.web.utils.wagu.Table;
import com.trackray.base.utils.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    private ConcurrentHashMap<String,Future<AbstractPlugin>> pluginPool;
    @Autowired
    private DispatchController dispatchController;

    /**
     * 查找普通插件方法
     * @return
     */
    @Override
    public JSONArray findPlugins(){
        WebApplicationContext context = getContext();
        Map<String, CommonPlugin> bean = context.getBeansOfType(CommonPlugin.class);
        JSONArray arr = new JSONArray();
        for (Map.Entry<String, CommonPlugin> entry : bean.entrySet()) {
            JSONObject obj = new JSONObject();
            AbstractPlugin plugin =  entry.getValue();
            Rule rule = plugin.currentRule();
            Plugin p = plugin.currentPlugin();

            if (p==null || rule == null)
                continue;
            if (plugin instanceof MVCPlugin)
                continue;
            obj.put("plugin_key",entry.getKey());

            JSONObject base = new JSONObject();
            base.put("author",p.author());
            base.put("title",p.title());
            base.put("desc",p.desc());
            base.put("link",p.link());
            base.put("time",p.time());
            obj.put("base_info",base);

            JSONObject rules = new JSONObject();
            rules.put("sync",rule.sync());

            ArrayList<String> ruleparam = new ArrayList<>();
            ArrayList<String> defparam = new ArrayList<>();
            ArrayList<String> descparam = new ArrayList<>();
            Param[] params = rule.params();
            if (!rule.defParam() && params[0].key().equals("RHOST") && params[3].key().equals("SSL")){

            }else{
                for (Param param : params) {
                    ruleparam.add(param.key());
                    defparam.add(param.defaultValue());
                    descparam.add(param.desc());
                }
            }

            rules.put("params",ruleparam);
            rules.put("def_params",defparam);
            rules.put("desc_params",descparam);

            rules.put("enable",rule.enable());
            rules.put("websocket",rule.websocket());
            obj.put("plugin_rule",rules);
            arr.add(obj);
        }
        return arr;
    }

    /**
     * 查找WEBSOCKET插件方法
     * @return
     */
    @Override
    public JSONArray findWebsocketPlugins(){
        WebApplicationContext context = getContext();
        Map<String, WebSocketPlugin> bean = context.getBeansOfType(WebSocketPlugin.class);
        JSONArray arr = new JSONArray();
        for (Map.Entry<String, WebSocketPlugin> entry : bean.entrySet()) {
            JSONObject obj = new JSONObject();
            AbstractPlugin plugin =  entry.getValue();
            Rule rule = plugin.currentRule();
            Plugin p = plugin.currentPlugin();

            if (p==null || rule == null)
                continue;
            obj.put("plugin_key",entry.getKey());

            JSONObject base = new JSONObject();
            base.put("author",p.author());
            base.put("title",p.title());
            base.put("desc",p.desc());
            base.put("link",p.link());
            base.put("time",p.time());
            obj.put("base_info",base);

            JSONObject rules = new JSONObject();
            rules.put("sync",rule.sync());

            ArrayList<String> ruleparam = new ArrayList<>();
            ArrayList<String> defparam = new ArrayList<>();
            ArrayList<String> descparam = new ArrayList<>();
            Param[] params = rule.params();
            if (!rule.defParam() && params[0].key().equals("RHOST") && params[3].key().equals("SSL")){

            }else{
                for (Param param : params) {
                    ruleparam.add(param.key());
                    defparam.add(param.defaultValue());
                    descparam.add(param.desc());
                }
            }

            rules.put("params",ruleparam);
            rules.put("def_params",defparam);
            rules.put("desc_params",descparam);

            rules.put("enable",rule.enable());
            rules.put("websocket",rule.websocket());
            obj.put("plugin_rule",rules);
            arr.add(obj);
        }
        return arr;
    }

    public Map<String,MVCPlugin> findMVCPlugins(){
        HashMap<String, MVCPlugin> map = new HashMap<>();
        WebApplicationContext context = getContext();
        Map<String, MVCPlugin> bean = context.getBeansOfType(MVCPlugin.class);
        for (Map.Entry<String, MVCPlugin> entry : bean.entrySet()) {
            MVCPlugin plugin =  entry.getValue();
            Rule rule = plugin.currentRule();
            Plugin p = plugin.currentPlugin();
            map.put(p.value(),plugin);
        }
        return map;
    }


    /**
     * 使用插件方法（接口）
     * @param map 参数
     * @param response
     * @throws IOException
     */
    @Override
    public void usePlugin(Map<String, String> map, HttpServletRequest request, HttpServletResponse response) throws IOException {

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

        Rule rule = bean.currentRule();
        Param[] params = rule.params();
        if (!rule.enable()){
            response.getWriter().print(ResultCode.WARN("该插件未启用"));
            return;
        }
        if (rule.websocket()){
            response.getWriter().print(ResultCode.WARN("该插件类型为websocket"));
            return;
        }
        if (rule.auth() && !ServletUtils.isLogged(request)){
            response.getWriter().print(ResultCode.WARN("该插件需要登录认证"));
            return;
        }
        /*try {
            bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        if (params.length>0){
            for (Param param : params) {
                if (!map.containsKey(param.key()) || StringUtils.isEmpty(map.get(param.key()))){
                    map.put(param.key(),param.defaultValue());
                }
            }
           /* String[] param = rule.param();
            String[] defparam = rule.defParam();
            *//*if (defparam.length != param.length){
                response.getWriter().print("参数与默认参数数量不同，请检查");
                return;
            }*//*
            if (param.length == defparam.length){
                for (int i = 0; i < param.length; i++) {
                    String p = param[i];    //参数键
                    String dp = defparam[i];//默认值
                *//*
                    如果请求中的键不存在或者键的值为空，则替换值为默认值
                 *//*
                    if (!map.containsKey(p) || StringUtils.isEmpty(map.get(p))){
                        map.put(p,dp);
                    }

                }
            }

            *//*for (String s : rule.param()) {
                if (!map.containsKey(s)){
                    response.getWriter().print(ResultCode.WARN("请填写参数:".concat(s)));
                    return;
                }
            }*/



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
            if (!response.isCommitted())
                response.getWriter().print(obj==null?ResultCode.WARN(executor.errorMsg):obj);
            return;
        }

    }

    /**
     * 通过多线程插件任务id获取插件执行结果
     * @param task
     * @param response
     * @throws IOException
     */
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
            response.getWriter().print(result==null?ResultCode.WARN(obj.errorMsg):result);
            return;
        }else{
            response.getWriter().print(ResultCode.ERROR);
            return;
        }

    }

    /**
     * 使用插件处理方法(websocket)
     * @param session websocket会话对象
     * @param key 插件KEY
     * @return
     * @throws IOException
     */
    @Override
    public boolean usePlugin(WebSocketSession session, String key) throws IOException {
        if (!getContext().containsBean(key)){
            session.sendMessage(Message.NORMAL("插件不存在"));
            return false;
        }
        AbstractPlugin bean = null;
        try {
            bean = (AbstractPlugin) getBean(key);
        } catch (Exception e) {
            session.sendMessage(Message.NORMAL("插件不合法"));
            return false;
        }
        if (bean!=null && bean instanceof AbstractPlugin){
        }else {
            session.sendMessage(Message.NORMAL("插件不合法"));
            return false;
        }

        Rule rule = bean.currentRule();
        if (!rule.enable()){
            session.sendMessage(Message.NORMAL("插件未启用"));
            return false;
        }
        if (!rule.websocket()) {
            session.sendMessage(Message.NORMAL("该插件不支持websocket"));
            return false;
        }

        JSONObject ruleObj = rule(key);
        String tableStr = this.pluginToTable(ruleObj);
        session.sendMessage(Message.NORMAL(tableStr));
        return true;
    }

    /**
     * 构建websocket插件
     * @param session websocket会话对象
     * @param map 插件参数
     * @param key 插件KEY
     * @return
     */
    @Override
    public WebSocketPlugin buildPlugin(WebSocketSession session, Map<String, String> map, String key) {

        WebSocketPlugin bean = getContext().getBean(key, WebSocketPlugin.class);

        /*try {
            bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
        Rule rule = bean.currentRule();
        Param[] params = rule.params();
/*        if (rule.param().length>0){
            String[] param = rule.param();
            String[] defparam = rule.defParam();
            if (param.length == defparam.length){
                for (int i = 0; i < param.length; i++) {
                    String p = param[i];    //参数键
                    String dp = defparam[i];//默认值
                    *//*
                        如果请求中的键不存在或者键的值为空，则替换值为默认值
                     *//*
                    if (!map.containsKey(p) || StringUtils.isEmpty(map.get(p))){
                        map.put(p,dp);
                    }
                }
            }
        }*/

        if (params.length>0){
            for (Param param : params) {
                if (!map.containsKey(param.key()) || StringUtils.isEmpty(map.get(param.key()))){
                    map.put(param.key(),param.defaultValue());
                }
            }


        }


        bean.setParam(map);
        bean.setSession(session);
        return bean;
    }

    /**
     * 杀死多线程插件插件
     * @param task 线程插件任务ID
     * @return
     */
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

    /**
     * 执行可视化页面类型插件
     * @param plugin 插件名
     * @param function 功能名
     * @param map   参数集合
     * @param model 页面对象
     * @param request   请求对象
     * @param response  响应对象
     */
    @Override
    public ModelAndView fetchPlugin(String plugin, String function, Map<String, String> map, ModelAndView model, HttpServletRequest request, HttpServletResponse response) {

        if (!getContext().containsBean(plugin)){
            model.setViewName("common/error");
            model.addObject("msg","插件未找到");
        }else{

            MVCPlugin mvcPlugin = (MVCPlugin) this.getBean(plugin);

            if (mvcPlugin.currentRule().auth() && !ServletUtils.isLogged(request)){
                model.setViewName("common/error");
                model.addObject("msg","该插件需要登录认证");
                return model;
            }

            if (mvcPlugin.currentRule().enable()){

                mvcPlugin.setParam(map);

                mvcPlugin.setModel(model);

                mvcPlugin.request = request;

                mvcPlugin.response = response;

                mvcPlugin.setFunction(function);

                AbstractPlugin<ModelAndView> executor = mvcPlugin.executor();

                model = executor.result();
            }else{
                model.setViewName("common/error");
                model.addObject("msg","该插件未开启");
            }

        }

        return model;
    }

    /**
     * 获取当前spring容器上下文对象
     * @return
     */
    private WebApplicationContext getContext(){
        //WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        return dispatchController.getAppContext();
    }

    /**
     * 通过 beanname 获取插件对象
     * @param bean
     * @return
     */
    private Object getBean(String bean){
        return  getContext().getBean(bean,AbstractPlugin.class);
    }

    /**
     * 获取插件规则，转换为JSON对象
     * @param bean
     * @return
     */
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

        Rule rule = plugin.currentRule();
        Plugin p = plugin.currentPlugin();
        JSONObject obj = new JSONObject();
        obj.put("plugin_key",bean);

        JSONObject base = new JSONObject();
        base.put("author",p.author());
        base.put("title",p.title());
        base.put("desc",p.desc());
        base.put("link",p.link());
        obj.put("base_info",base);

        JSONObject rules = new JSONObject();


        ArrayList<String> ruleparam = new ArrayList<>();
        ArrayList<String> defparam = new ArrayList<>();
        ArrayList<String> descparam = new ArrayList<>();
        Param[] params = rule.params();
        if (!rule.defParam() && params[0].key().equals("RHOST") && params[3].key().equals("SSL")){

        }else{
            for (Param param : params) {
                ruleparam.add(param.key());
                defparam.add(param.defaultValue());
                descparam.add(param.desc());
            }
        }

        rules.put("params",ruleparam);
        rules.put("def_params",defparam);
        rules.put("desc_params",descparam);


        rules.put("sync",rule.sync());
        rules.put("enable",rule.enable());
        rules.put("websocket",rule.websocket());
        obj.put("plugin_rule",rules);
        return obj;
    }

    /**
     * 插件规则转换为制表格式输出
     * @param rule
     * @return
     */
    private String pluginToTable(JSONObject rule) {
        List<String> headersList = Arrays.asList("key", "value");
        Map plugin = rule;
        int length = 10;

        String plugin_key = (String) plugin.get("plugin_key");
        length = plugin_key.length()>length?plugin_key.length():length;

        String name = ((Map)plugin.get("base_info")).get("title").toString();
        length = name.length()>length?name.length():length;

        String author = ((Map)plugin.get("base_info")).get("author").toString();

        String desc = ((Map)plugin.get("base_info")).get("desc").toString();
        length = desc.length()>length?desc.length():length;

        String link = ((Map)plugin.get("base_info")).get("link").toString();
        length = link.length()>length?link.length():length;

        String sync = ((Map)plugin.get("plugin_rule")).get("sync").toString();
        String websocket = ((Map)plugin.get("plugin_rule")).get("websocket").toString();

        List<List<String>> rowsList = Arrays.asList(
                Arrays.asList("plugin_key", plugin_key),
                Arrays.asList("name", name),
                Arrays.asList("author", author),
                Arrays.asList("sync", sync),
                Arrays.asList("websocket", websocket),
                Arrays.asList("desc", desc),
                Arrays.asList("link", link)
        );
        Board board = new Board(length+10+10);
        Table table = new Table(board, length, headersList, rowsList);
        table.setGridMode(Table.GRID_FULL);
        //setting width and data-align of columns
        List<Integer> colWidthsList = Arrays.asList(10 , length);
        table.setColWidthsList(colWidthsList);
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();

        List<String> params = (List) ((Map) plugin.get("plugin_rule")).get("params");
        List<String> def_params = (List) ((Map) plugin.get("plugin_rule")).get("def_params");
        List<String> desc_params = (List) ((Map) plugin.get("plugin_rule")).get("desc_params");
        if (params!=null && params.size()>0){
            boolean defflag = params.size() == def_params.size()?true:false;
            boolean descflag = params.size() == desc_params.size()?true:false;
            ArrayList<List<String>> rows = new ArrayList<List<String>>();
            int n_max=7,d_max=7,de_max=11;
            for (int i = 0; i < params.size(); i++) {
                String param = params.get(i);
                List<String> row = Arrays.asList(param, defflag ? def_params.get(i) : "", descflag ? desc_params.get(i) : "");
                n_max = row.get(0).length()>n_max?row.get(0).length():n_max;
                d_max = row.get(1).length()>d_max?row.get(1).length():d_max;
                de_max = row.get(2).length()>de_max?row.get(2).length():de_max;

                rows.add(row);
            }
            List<String> headers = Arrays.asList("Name", "Default" , "Description");

            Board board2 = new Board(n_max+d_max+de_max+10);
            Table table2 = new Table(board2, n_max+d_max+de_max, headers, rows);
            table2.setColWidthsList(Arrays.asList(n_max, d_max, de_max));
            table2.setGridMode(Table.GRID_COLUMN);
            //setting width and data-align of columns
            Block tableBlock2 = table2.tableToBlocks();
            board2.setInitialBlock(tableBlock2);
            board2.build();
            String tableString2 = board2.getPreview();
            String template = "插件信息\n%s\n参数信息\n%s";
            template = String.format(template , tableString , tableString2);
            return template;
        }
        return tableString;
    }
}
