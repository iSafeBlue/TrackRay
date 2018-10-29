package com.trackray.scanner.bean;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {

    public static String RESOURCES_PATH ="";

    /* command命令前缀 */
    public static final String COMM_PREFIX = "cmd /c ";

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
        public static String NMAP_DIR = "D:/nmap/";
        public static String NMAP_COMM_SCAN = "nmap -O ";
    }
}
