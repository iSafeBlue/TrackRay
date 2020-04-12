package com.trackray.base.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置文件读取
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
                    in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
                    props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("jdbc.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }

        String property = props.getProperty(key);

        if (property.contains("${")){
            property = getPropertyVar(property);
        }

        return property;
    }

    private static String getPropertyVar(String property) {
        for (String var : RegexUtil.getMatchers("\\$\\{([a-zA-Z0-9\\.]+)\\}", property)) {
            String varNmae = "${" + var + "}";
            String varValue = props.getProperty(var);
            if (varValue == null || varValue.isEmpty()) {
                varValue = System.getProperty(var);
            }
            property = property.replace(varNmae, varValue);
        }
        if (property.contains("${")) {
            property = getPropertyVar(property);
        }
        return property;
    }

    public static List<String> extractVar(String regex,String source){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }


    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}