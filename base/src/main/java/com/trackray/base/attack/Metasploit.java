package com.trackray.base.attack;

import com.trackray.base.utils.StrUtils;
import com.trackray.base.utils.SysLog;
import lombok.Data;
import net.dongliu.requests.Requests;
import net.sf.json.JSONObject;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Msf模块
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/16 10:51
 */
@Component
@Scope("prototype")
@Data
public class Metasploit {

    private static final Map header = Collections.singletonMap("Content-type", "binary/message-pack");

    @org.springframework.beans.factory.annotation.Value("${metasploit.host}")
    private String host = "127.0.0.1:55553";
    @org.springframework.beans.factory.annotation.Value("${metasploit.user}")
    private String username = "msf";
    @org.springframework.beans.factory.annotation.Value("${metasploit.pass}")
    private String password = "msf";

    private String token;

    private  boolean logind;

    private int consoleID;

    private String console = "msf > ";

    private boolean busy = false;

    private int callTimeout = 30000;

    private static final String format = "http://%s/api/1.0";



    public final static class Command{
        public final static String AuthLogin = "auth.login";
        public final static String AuthLogout = "auth.logout";
        public final static String AuthTokenList = "auth.token_list";
        public final static String AuthTokenAdd = "auth.token_add";
        public final static String AuthTokenGenerate = "auth.token_generate";
        public final static String AuthTokenRemove = "auth.token_remove";
        public final static String ConsoleCreate = "console.create";
        public final static String ConsoleList = "console.list";
        public final static String ConsoleDestroy = "console.destroy";
        public final static String ConsoleRead = "console.read";
        public final static String ConsoleWrite = "console.write";
        public final static String ConsoleTabs = "console.tabs";
        public final static String ConsoleSessionKill = "console.session_kill";
        public final static String ConsoleSessionDetach = "console.session_detach";
        public final static String CoreVersion = "core.version";
        public final static String CoreStop = "core.stop";
        public final static String CoreSetG = "core.setg";
        public final static String CoreUnsetG = "core.unsetg";
        public final static String CoreSave = "core.save";
        public final static String CoreReloadModules = "core.reload_modules";
        public final static String CoreModuleStats = "core.module_stats";
        public final static String CoreAddModulePath = "core.add_module_path";
        public final static String CoreThreadList = "core.thread_list";
        public final static String CoreThreadKill = "core.thread_kill";
        public final static String DbHosts = "db.hosts";
        public final static String DbServices = "db.services";
        public final static String DbVulns = "db.vulns";
        public final static String DbWorkspaces = "db.workspaces";
        public final static String DbCurrentWorkspace = "db.current_workspace";
        public final static String DbGetWorkspace = "db.get_workspace";
        public final static String DbSetWorkspace = "db.set_workspace";
        public final static String DbDelWorkspace = "db.del_workspace";
        public final static String DbAddWorkspace = "db.add_workspace";
        public final static String DbGetHost = "db.get_host";
        public final static String DbReportHost = "db.report_host";
        public final static String DbReportService = "db.report_service";
        public final static String DbGetService = "db.get_service";
        public final static String DbGetNote = "db.get_note";
        public final static String DbGetClient = "db.get_client";
        public final static String DbReportClient = "db.report_client";
        public final static String DbReportNote = "db.report_note";
        public final static String DbNotes = "db.notes";
        public final static String DbReportAuthInfo = "db.report_auth_info";
        public final static String DbGetAuthInfo = "db.get_auth_info";
        public final static String DbGetRef = "db.get_ref";
        public final static String DbDelVuln = "db.del_vuln";
        public final static String DbDelNote = "db.del_note";
        public final static String DbDelService = "db.del_service";
        public final static String DbDelHost = "db.del_host";
        public final static String DbReportVuln = "db.report_vuln";
        public final static String DbEvents = "db.events";
        public final static String DbReportEvent = "db.report_event";
        public final static String DbReportLoot = "db.report_loot";
        public final static String DbLoots = "db.loots";
        public final static String DbReportCred = "db.report_cred";
        public final static String DbCreds = "db.creds";
        public final static String DbImportData = "db.import_data";
        public final static String DbGetVuln = "db.get_vuln";
        public final static String DbClients = "db.clients";
        public final static String DbDelClient = "db.del_client";
        public final static String DbDriver = "db.driver";
        public final static String DbConnect = "db.connect";
        public final static String DbStatus = "db.status";
        public final static String DbDisconnect = "db.disconnect";
        public final static String JobList = "job.list";
        public final static String JobStop = "job.stop";
        public final static String JobInfo = "job.info";
        public final static String ModuleExploits = "module.exploits";
        public final static String ModuleAuxiliary = "module.auxiliary";
        public final static String ModulePayloads = "module.payloads";
        public final static String ModuleEncoders = "module.encoders";
        public final static String ModuleNops = "module.nops";
        public final static String ModulePost = "module.post";
        public final static String ModuleInfo = "module.info";
        public final static String ModuleCompatiblePayloads = "module.compatible_payloads";
        public final static String ModuleCompatibleSessions = "module.compatible_sessions";
        public final static String ModuleTargetCompatiblePayloads = "module.target_compatible_payloads";
        public final static String ModuleOptions = "module.options";
        public final static String ModuleExecute = "module.execute";
        public final static String ModuleEncodeFormats = "module.encode_formats";
        public final static String ModuleEncode = "module.encode";
        public final static String PluginLoad = "plugin.load";
        public final static String PluginUnload = "plugin.unload";
        public final static String PluginLoaded = "plugin.loaded";
        public final static String SessionList = "session.list";
        public final static String SessionStop = "session.stop";
        public final static String SessionShellRead = "session.shell_read";
        public final static String SessionShellWrite = "session.shell_write";
        public final static String SessionShellUpgrade = "session.shell_upgrade";
        public final static String SessionMeterpreterRead = "session.meterpreter_read";
        public final static String SessionRingRead = "session.ring_read";
        public final static String SessionRingPut = "session.ring_put";
        public final static String SessionRingLast = "session.ring_last";
        public final static String SessionRingClear = "session.ring_clear";
        public final static String SessionMeterpreterWrite = "session.meterpreter_write";
        public final static String SessionMeterpreterSessionDetach = "session.meterpreter_session_detach";
        public final static String SessionMeterpreterSessionKill = "session.meterpreter_session_kill";
        public final static String SessionMeterpreterTabs = "session.meterpreter_tabs";
        public final static String SessionMeterpreterRunSingle = "session.meterpreter_run_single";
        public final static String SessionMeterpreterScript = "session.meterpreter_script";
        public final static String SessionMeterpreterDirectorySeparator = "session.meterpreter_directory_separator";
        public final static String SessionCompatibleModules = "session.compatible_modules";
    }

    public boolean login(){
        try {
            Map send = sendList(list(Command.AuthLogin, username, password));
            if (send.containsKey("token")){
                this.token = send.get("token").toString();
                logind = true;
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }


    public void close(){
        logout();
    }
    public void logout(){
        destroyConsole();
        Map map = sendList(list(Command.AuthLogout, token));
    }

    public int createConsole(){
        Map map = sendList(list(Command.ConsoleCreate, token));
        if (map.containsKey("id")){
            consoleID = Integer.parseInt( map.get("id").toString());
            return consoleID;
        }
        return -1;
    }

    public String banner(){
        String banner = execute(Arrays.asList(""));
        return banner;
    }

    public void destroyConsole(){
        Map map = sendList(list(Command.ConsoleDestroy, token , String.valueOf(consoleID)));
    }

    public String execute(List<String> commands){
        executeCommand(commands);
        Map map = readResult();
        if (map!=null && map.containsKey("data") && map.containsKey("prompt") && map.containsKey("busy"))
        {
            String response = StrUtils.unicodeToString(map.get("data").toString());
            String prompt = StrUtils.unicodeToString(map.get("prompt").toString());
            busy = (boolean) map.get("busy");
            this.console = prompt;
            return response;
        }
        return "execute wrong!";
    }

    public Map executeCommand(List<String> commands){
        StringBuffer buff = new StringBuffer();
        for (String command : commands) {
            buff.append(command).append("\n");
        }
        Map map = sendList(list(Command.ConsoleWrite, token, String.valueOf(consoleID), buff.toString()));
        return map;
    }

    public Map readResult(){
        Map map = sendList(list(Command.ConsoleRead, token, String.valueOf(consoleID)));
        return map;
    }

    public byte[] send(byte[] p){
        byte[] result= Requests.post(format())
                .timeout(callTimeout)
                .headers(header)
                .body(p).send().readToBytes();
        return result;
    }
    public Map sendList(List list){
        try {
            byte[] result= send(pack(list));
            Value unpack = unpack(result);
            return JSONObject.fromObject(unpack.toString());
        }catch (Exception e){
            SysLog.error(e.getMessage());
        }

        return Collections.EMPTY_MAP;
    }

    public byte[] pack(Object obj) {
        try {
            return MessagePack.pack( obj );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Value unpack(byte[] obj) {
        try {
            return MessagePack.unpack(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final String format() {
        return String.format(this.format,host);
    }

    public List<String> list(String... param){
        return Arrays.asList(param);
    }

    public String[] arr(String command ,String... param){
        return param;
    }


    @Override
    protected void finalize() throws Throwable {
        if (logind)
        {
            logout();
            logind = false;
        }
        super.finalize();
    }
}
