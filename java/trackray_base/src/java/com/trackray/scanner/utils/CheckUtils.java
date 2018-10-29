package com.trackray.scanner.utils;

import com.google.gson.Gson;

public class CheckUtils {

    public static boolean isJson(String inputJsonStr){

        if(org.apache.commons.lang3.StringUtils.isBlank(inputJsonStr)){
            return false;
        }

        Gson gson = new Gson();
        try {
            gson.fromJson(inputJsonStr, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }

    }

}
