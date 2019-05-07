package com.trackray.base.utils;

import java.util.Date;

public class TaskUtils {

    /**
     * 生成任务ID
     * @param target
     * @return
     */
    public static String genTaskKey(String target){
        return MD5.getMD5Code(new Date().getTime()+target);
    }

    public static String genFingerKey(String f){
        String s = MD5.getMD5Code(f).toUpperCase();
        return "$"+s.substring(0,6);
    }

}
