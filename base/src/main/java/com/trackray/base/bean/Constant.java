package com.trackray.base.bean;

import com.trackray.base.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class Constant {

    public static String USER_DIR = System.getProperty("user.dir"); // 当前程序工作目录

    public static String RESOURCES_PATH = PropertyUtil.getProperty("trackray.resource.dir");

    public static String RESOURCES_INCLUDE_PATH = PropertyUtil.getProperty("trackray.plugin.include.dir"); //插件包含资源文件路径

    public static final String WINDOWS = "WINDOWS";
    public static final String LINUX = "LINUX";
    public static String TRACKRAY_SYSTEMOS = WINDOWS;   //溯光操作系统


    public static String SYSTEM_ACCOUNT = PropertyUtil.getProperty("trackray.account");
    public static String SYSTEM_PASSWORD= PropertyUtil.getProperty("trackray.password");

    /* Task任务目标类型 */
    public static final int URL_TYPE = 1;
    public static final int IP_TYPE = 2;
    public static String SQLMAP_HOST = PropertyUtil.getProperty("sqlmap.host");

    public static boolean AVAILABLE_SQLMAP = false;
    public static boolean AVAILABLE_NMAP = false;
    public static boolean AVAILABLE_PYTHON = false;
    public static boolean AVAILABLE_AWVS = false;
    public static boolean AVAILABLE_NESSUS = false;
    public static boolean AVAILABLE_HYDRA = false;
    public static boolean AVAILABLE_METASPLOIT = false;

    public static String LINE = System.getProperty("line.separator");

    public static class Vuln{
        public static final String FILE_READ_VULN_REGEX = "\\?\\S+=(\\S+/\\S+\\.\\S+|\\S+\\.\\S+)";

        public static final Map<String,String> FINGERS_REGEX = new HashMap<>();

        public static String VULN_ADD_API;
    }

}
