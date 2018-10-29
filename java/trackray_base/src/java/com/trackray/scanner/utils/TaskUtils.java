package com.trackray.scanner.utils;

import java.util.Date;
import java.util.UUID;

public class TaskUtils {


    public static String genTaskKey(String target){
        return MD5.getMD5Code(new Date().getTime()+target);
    }

    public static String genFingerKey(String f){
        String s = MD5.getMD5Code(f).toUpperCase();
        return "$"+s.substring(0,6);
    }

}
