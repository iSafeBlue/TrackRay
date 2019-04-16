package com.trackray.base.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class Constant {

    public static String USER_DIR = System.getProperty("user.dir"); // 当前程序工作目录

    public static String RESOURCES_PATH = USER_DIR.concat("/resources/");

    public static String RESOURCES_INCLUDE_PATH; //插件包含资源文件路径

    /* Task任务目标类型 */
    public static final int URL_TYPE = 1;
    public static final int IP_TYPE = 2;
    public static String CENSYS_APPID = "";
    public static String CENSYS_SECRET = "";


    public static final int VULN_TYPE_XSS = 1;
    public static final int VULN_TYPE_SQLINJECTION = 2;
    public static final int VULN_TYPE_FILE_HANDLE = 3;

    public static class SQLMap{
        public static final int STATUS_CREATED = 0;
        public static final int STATUS_RUNNING = 2;
        public static final int STATUS_END = 1;
    }

    public static class Vuln{
        public static final String FILE_READ_VULN_REGEX = "\\?\\S+=(\\S+/\\S+\\.\\S+|\\S+\\.\\S+)";

        public static final Map<String,String> FINGERS_REGEX = new HashMap<>();

        public static String VULN_ADD_API;
    }

    public static class NmapComm{
        public static String NMAP_DIR = "";
        public static String NMAP_COMM_SCAN = "nmap -O ";
    }
}
