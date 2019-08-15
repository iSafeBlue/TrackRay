package com.trackray.base.utils;


import com.alibaba.fastjson.JSON;

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
        try {
            JSON.parse(inputJsonStr);
        }catch (Exception e){
            return false;
        }
        return true;

    }

}
