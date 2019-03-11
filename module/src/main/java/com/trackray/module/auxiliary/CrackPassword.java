package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.CheckUtils;
import com.trackray.base.utils.SysLog;
import net.sf.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Maps;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/13 15:36
 */
@Plugin(title = "暴力破解密码模块",author = "blue"  )
@Rule(params = {@Param(key = "thread",defaultValue = "5" , desc = "线程数")} , websocket = true , sync = true)
public class CrackPassword extends WebSocketPlugin {

    private ServerMap servers = new ServerMap()
            .add("mysql","3306").add("mssql","1433").add("ssh","22")
            .add("telnet","23").add("rdp","3389").add("ftp","21")
            .add("oracle","1521").add("PostgreSQL","5432").add("redis","6379")
            .add("MongoDB","27017");
    //key对应方法名，只写了mysql，其他实在懒得写了，自己扩展时注意在maven加依赖...

    private ThreadPoolExecutor pool ;
    private String host;
    private String protocol;
    private String port;
    private List<String> udict,pdict;
    private final String PATH =BASE + "/CrackPassword/";

    private Map<String,String> resultMap = new HashMap<>();

    @Override
    public boolean check(Map param) {
        int poolsize = Integer.parseInt(param.get(currentParams()[0].key()).toString());
        pool = new ThreadPoolExecutor(poolsize, poolsize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        return poolsize>0;
    }

    @Override
    public void before() {

        for (String k : servers.keySet()) {
            send(k);
        }
        send("[!]请选您要破解的服务");
        String serverName = getInput();
        if (StringUtils.isNotBlank(serverName) && servers.containsKey(serverName)){
            send("你选择的是"+serverName);
            this.protocol=serverName;
        }else{
            return;
        }
        send("[!]请输入主机名与端口，如：127.0.0.1:3306 不填写端口则使用默认端口");
        String host = getInput();
        if (StringUtils.isNotBlank(host)){
            if (host.contains(":")){
                String[] split = host.split(":");
                this.host = split[0];
                this.port = split[1];
            }else{
                this.host = host;
                this.port = servers.get(protocol);
            }
        }
        
        send("[!]是否使用默认用户名字典[y/n]");
        String yn = getInput();
        if (yn.equals("n")){
            send("[!]请输入用户名字典 ");
            send("1:[\"admin\",\"username\",\"root\"] 一般模式");
            send("2:dict.txt 相对路径模式");
            send("3:http://host/dict.txt 远程文件模式");
            String dict = getInput();
            this.udict = this.inputToDict(dict);
        }else{
            this.udict = readLocalDict("user.txt");
        }


        send("[!]是否使用默认密码字典[y/n]");
        yn = getInput();
        if (yn.equals("n")){
            send("[!]请输入密码字典 ");
            send("1:[\"123\",\"password\",\"admin888\"] 一般模式");
            send("2:dict.txt 相对路径模式");
            send("3:http://host/dict.txt 远程文件模式");
            String dict = getInput();
            this.pdict = this.inputToDict(dict);
        }else{
            this.pdict = readLocalDict("pass.txt");
        }

    }
    private List<String> inputToDict(String dict){
        List<String> dicts = new ArrayList<String>();
        if (CheckUtils.isJson(dict)){
            dicts = JSONArray.fromObject(dict);
        }else if (dict.toLowerCase().startsWith("http")){
            crawlerPage.getRequest().setUrl(dict);
            fetcher.run(crawlerPage);
            if (crawlerPage.getResponse().getStatus().getContentTypeString().contains("text")){
                String tempDictString = crawlerPage.getResponse().getStatus().getContentString();
                if (tempDictString.contains("\r\n")){
                    dicts = Arrays.asList(tempDictString.split("\r\n"));
                }else if(tempDictString.contains("\n")){
                    dicts = Arrays.asList(tempDictString.split("\n"));
                }
            }
        }else{
            dicts = readLocalDict(dict);
        }
        return dicts;
    }

    public List<String > readLocalDict(String filepath){
        try {
            return FileUtils.readLines(new File(PATH.concat(filepath)));
        } catch (IOException e) {
            SysLog.error(e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Map<String, String> start() {
        if (StringUtils.isAnyBlank(host,port,protocol)){
            errorMsg = "参数填写有误";
            return null;
        }

        CrackPassword context = this;

        try {
            for (String user : udict) {
                for (String pass : pdict) {

                    Parameter parameter = new Parameter() {
                        @Override
                        public String username() {
                            return user;
                        }
                        @Override
                        public String password() {
                            return pass;
                        }
                    };

                    pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                context.getClass().getMethod(protocol,Parameter.class).invoke(context,parameter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }

            send("正在破解中请耐心等待");
        } catch (Exception e) {
            SysLog.error(e.getMessage());
        }

        while (true){
            if (!resultMap.isEmpty() ||
                    pool.isShutdown() ||
                    pool.isTerminated() )
            {
                onClose();
                break;
            }
        }
        return resultMap;
    }

    @Override
    public void onClose() {
        if (!pool.isShutdown())
            pool.shutdownNow();
    }
    /**
     * ======================================================
     * ==================开始自定义接口和类=====================
     * ======================================================
     */
    private class ServerMap extends HashMap<String,String> {

        public ServerMap add(String key, String value) {
            super.put(key, value);
            return this;
        }
    }

    private interface Parameter{
        String username();
        String password();
    }
    /*====================================================
    ====================暴力破解实现方法=====================
    ====================================================*/
    public void mysql(Parameter parameter){

        String driver ="";
        String url = "jdbc:mysql://"+host+":"+port+"/test?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String username = parameter.username();
        String password = parameter.password();

        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            SysLog.info(username+" -- "+password + " --- "+e);
            return;
        }finally {
            try {
                if (conn!=null)
                    conn.close();
            } catch (SQLException e) {
                SysLog.error(e.getMessage());
            }
        }

        resultMap.put(username,password);
        send("[+]破解成功："+resultMap);
        onClose();
    }

    public static void main(String[] args) {

        CrackPassword c = new CrackPassword();
        c.setParam(Maps.newHashMap("thread","20"));
        c.executor();
    }

}
