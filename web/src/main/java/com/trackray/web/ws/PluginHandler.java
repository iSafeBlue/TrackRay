package com.trackray.web.ws;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.trackray.base.bean.Banner;
import com.trackray.base.utils.Message;
import com.trackray.web.service.PluginService;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.web.utils.wagu.Block;
import com.trackray.web.utils.wagu.Board;
import com.trackray.web.utils.wagu.Table;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * websocket处理类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Component
public class PluginHandler extends TextWebSocketHandler {


    private static final Logger log = LoggerFactory.getLogger(PluginHandler.class);

    @Autowired
    private PluginService pluginService;

    private Map<String,String> uses = new ConcurrentHashMap<>();
    private Map<String,Map<String,String>> sets = new ConcurrentHashMap<>();
    private Map<String,Map<String,Object>> plugins = new ConcurrentHashMap<>();

    @Autowired
    private Banner banner;
    @Override
    public  void afterConnectionEstablished(WebSocketSession session)
            throws Exception {


        session.sendMessage(Message.RED(banner.generate()));
        session.sendMessage(Message.GREEN("Tips:你可以尝试输入help来获取命令帮助文档"));
        //session.sendMessage(Message.NORMAL(StrUtils.formatJson(pluginService.findPlugins().toString())));
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();
        if (message.getPayloadLength()>0){
            String payload = message.getPayload();
            if (uses.containsKey(id) && uses.get(id)!=null && plugins.containsKey(id) && plugins.get(id).size()==2){
                Map<String, Object> classMap = plugins.get(id);
                WebSocketPlugin plugin = (WebSocketPlugin) classMap.get("plugin");
                plugin.append(payload);
            }else if (payload.startsWith("search ")){
                String ex = payload.substring(payload.indexOf(" ") + 1, payload.length());
                if (ex.equals("*")){
                    session.sendMessage(Message.WHITE(formatPluginsToTable(pluginService.findWebsocketPlugins())));
                }else{
                    JSONArray plugin = pluginService.findPlugins();
                    JSONArray temp = new JSONArray();
                    boolean flag = true;
                    for (Object o : plugin) {
                        String oo = o.toString();
                        if (oo.toLowerCase().contains(ex.toLowerCase())){
                            temp.add(oo);
                            flag=false;
                        }
                    }
                    if (flag)
                        session.sendMessage(Message.YELLOW("[-]plugin not found!"));
                    else
                        session.sendMessage(Message.WHITE(formatPluginsToTable(temp).replaceAll(ex,"<b><font color='red'>"+ex+"</font></b>")));
                }

            } else if (payload.startsWith("use ")){
                String[] args = payload.split(" ");
                String key = args[1];
                if (StringUtils.isNotBlank(key) && pluginService.usePlugin(session,key)){
                    uses.put(id,key);
                    Map<String, String> map = new ConcurrentHashMap<>();
                    sets.put(id,map);
                }
            }else if (payload.startsWith("set ")){
                String[] args = payload.split(" ");
                String param = args[1];
                String value = args[2];
                String key = null;
                if (StringUtils.isNoneBlank(param,value,(key = uses.get(id))) ){
                    sets.get(id).put(param,value);
                }
            }else if (payload.equals("build")){
                if (StringUtils.isNotBlank(uses.get(id))){ //  && sets.get(id).size()>0
                    Map<String, String> map = sets.get(id);
                    String key = uses.get(id);
                    WebSocketPlugin plugin = pluginService.buildPlugin(session,map,key);
                    if (plugin!=null){
                        Rule rule = plugin.getClass().getAnnotation(Rule.class);
                        Map<String, Object> classMap = new ConcurrentHashMap<String, Object>();
                        if (rule.sync()){
                            Thread thread = new Thread(plugin);
                            thread.start();
                            classMap.put("thread",thread);
                            classMap.put("plugin",plugin);
                            plugins.put(id,classMap);
                        }else{
                            plugin.build();
                            classMap.put("plugin",plugin);
                            plugins.put(id,classMap);
                        }
                    }
                }
            }else if (payload.equals("kill")){
                kill(id);
            }else{
/*
                if (uses.containsKey(id) && uses.get(id)!=null && plugins.get(id).size()==2){
                    Map<String, Object> classMap = plugins.get(id);
                    WebSocketPlugin plugin = (WebSocketPlugin) classMap.get("plugin");
                    plugin.append(payload);
                }else {
                    session.sendMessage(Message.NORMAL("command not found"));
                }
*/
                session.sendMessage(Message.NORMAL("command not found"));

            }


        }
        super.handleTextMessage(session, message);

    }

    @Override
    public  void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        log.info("connect websocket closed.......");
        String id = session.getId();
        kill(id);
        super.afterConnectionClosed(session, closeStatus);

    }

    /**
     * 杀死插件线程
     * @param id
     */
    private void kill(String id) {
        Map<String, Object> plugin = plugins.remove(id);
        if (plugin!=null && plugin.containsKey("plugin")){
            WebSocketPlugin p = (WebSocketPlugin) plugin.remove("plugin");
            if (p!=null)
                p.onClose();
        }
        if (plugin!=null && plugin.containsKey("thread")){
            Thread t = (Thread) plugin.remove("thread");
            if (t!=null && t.getState() != Thread.State.TERMINATED){
                t.interrupt();
            }
        }
        sets.remove(id);
        uses.remove(id);
    }

    /**
     * 格式化插件库JSON转换命令行表格风格
     * @param plugins 插件集合
     * @return string类型的表格字符串
     */
    private static String formatPluginsToTable(List<Map> plugins){
        List<String> headersList = Arrays.asList("key", "author", "websocket","sync" ,"param", "plugin name");
        ArrayList<List<String>> rows = new ArrayList<>();
        int k_max = headersList.get(0).length();//25;
        int n_max = headersList.get(5).length();//62;
        int a_max = headersList.get(1).length();//12;
        int pa_max = headersList.get(4).length();//50;
        for (int i = 0; i < plugins.size(); i++) {
            Map plugin = plugins.get(i);
            String plugin_key = (String) plugin.get("plugin_key");
            k_max = plugin_key.length()>k_max?plugin_key.length():k_max;
            String name = ((Map)plugin.get("base_info")).get("title").toString();
            n_max = name.length()>n_max?name.length():n_max;
            String author = ((Map)plugin.get("base_info")).get("author").toString();
            a_max = author.length()>a_max?name.length():a_max;

            String desc = ((Map)plugin.get("base_info")).get("desc").toString();

            String sync = ((Map)plugin.get("plugin_rule")).get("sync").toString();
            String websocket = ((Map)plugin.get("plugin_rule")).get("websocket").toString();
            String params = ((Map)plugin.get("plugin_rule")).get("params").toString();
            pa_max = params.length()>pa_max?params.length():pa_max;
            List<String> row = Arrays.asList(plugin_key, author,websocket, sync, params , name);
            rows.add(row);
        }
        int width = k_max + a_max + 9 + 9 + pa_max + n_max;
        Board board = new Board(width+10);
        Table table = new Table(board, width, headersList, rows);
        table.setGridMode(Table.GRID_COLUMN);
        //setting width and data-align of columns
        List<Integer> colWidthsList = Arrays.asList(k_max, a_max, 9,9, pa_max,n_max);
        List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
        table.setColWidthsList(colWidthsList);
        table.setColAlignsList(colAlignList);
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();
        String[] split = tableString.split("\n");
        StringBuffer buff = new StringBuffer();
        for (String s : split) {
            String substring = s.substring(0, s.contains("|") ? s.lastIndexOf("|"): s.length());
            buff.append(substring+"\n");
        }
        return buff.toString();
    }
}