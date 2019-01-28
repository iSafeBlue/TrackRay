package com.trackray.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private static final Logger logger = LoggerFactory.getLogger(RegexUtil.class);

    public static String extractStr(String source, String regex) {
        return extractStr(source, regex, true);
    }

    public static String extractStr(String source, String regex, boolean isEscapeHtmlTag) {
        if (source == null || regex == null) {
            return null;
        } else {
            Matcher matcher = RegExpUtil.getMatcher(source, regex);
            if (matcher.find()) {
                if (isEscapeHtmlTag) {
                    return HtmlUtils.escapeHtmlTag(matcher.group(1));
                } else {
                    return matcher.group(1);
                }
            } else {
                return null;
            }
        }
    }

    public static double extractDouble(String source, String regex) {
        return extractDouble(source, regex, true);
    }

    public static double extractDouble(String source, String regex, boolean isEscapeHtmlTag) {
        String str = extractStr(source, regex, isEscapeHtmlTag);
        if (StringUtils.isBlank(str)) {
            return 0;
        } else {
            str = str.replaceAll(",", "");
            if (str.matches("\\d+\\.\\d+")) {
                return Double.parseDouble(str);
            } else {
                return 0;
            }
        }
    }

    public static int extractInt(String source, String regex) {
        return extractInt(source, regex, true);
    }

    public static int extractInt(String source, String regex, boolean isEscapeHtmlTag) {
        String str = extractStr(source, regex, isEscapeHtmlTag);
        if (StringUtils.isBlank(str)) {
            return 0;
        } else {
            str = str.replaceAll(",", "");
            if (str.matches("\\d+")) {
                return Integer.parseInt(str);
            } else {
                return 0;
            }
        }
    }

    public static long extractLong(String source, String regex) {
        return extractLong(source, regex, true);
    }

    public static long extractLong(String source, String regex, boolean isEscapeHtmlTag) {
        String str = extractStr(source, regex, isEscapeHtmlTag);
        if (StringUtils.isBlank(str)) {
            return 0;
        } else {
            str = str.replaceAll(",", "");
            if (str.matches("\\d+")) {
                return Long.parseLong(str);
            } else {
                return 0;
            }
        }
    }

    public static Date extractDate(String source, String regex, String pattern) {
        String str = extractStr(source, regex);
        if (StringUtils.isBlank(str)) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                return sdf.parse(str) ;
            } catch (ParseException e) {
                logger.warn("", e);
                return null;
            }
        }
    }

    public static Map<String, String> extractProperties(String source, Map<String, String> regexps) {
        Map<String, String> properties = new HashMap<>();
        for (String key : regexps.keySet()) {
            Matcher matcher = RegExpUtil.getMatcher(source, regexps.get(key));
            if (matcher.find()) {
                String value = HtmlUtils.escapeHtmlTag(matcher.group(1));
                properties.put(key, value);
            }
        }
        return properties;
    }
}

class RegExpUtil {

    /**
     * 是否匹配
     *
     * @param url
     * @param urlRegx
     * @return
     */
    public static boolean matches(String url, String urlRegx) {
        Pattern pattern = Pattern.compile(urlRegx, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
            return true;
        return false;
    }

    /**
     * 获取匹配
     *
     * @param input
     * @param inputPattern
     * @return
     */
    public static Matcher getMatcher(String input, String inputPattern) {
        Matcher matcher = null;
        try {
            Pattern pattern = Pattern.compile(inputPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matcher;
    }

    public static String getRegContent(String input ,String inputPattern){
        Matcher matcher = getMatcher(input, inputPattern);
        if(matcher.find()){
            return matcher.group();
        }else{
            return "";
        }

    }

    public static String getRegContent(String input,String regex,int index){
        if(org.apache.commons.lang.StringUtils.isBlank(input)){
            return "";
        }

        Pattern pa = Pattern.compile(regex);
        Matcher ma = pa.matcher(input);
        if(ma.find()){
            return ma.group(index);
        }else{
            return "";
        }
    }


}
