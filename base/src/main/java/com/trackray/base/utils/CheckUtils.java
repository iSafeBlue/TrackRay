package com.trackray.base.utils;


/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class CheckUtils {

    public static boolean isJson(String inputJsonStr){

        if(org.apache.commons.lang3.StringUtils.isBlank(inputJsonStr)){
            return false;
        }

        return true;

    }

}
