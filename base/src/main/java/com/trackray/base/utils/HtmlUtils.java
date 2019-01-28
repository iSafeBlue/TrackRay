package com.trackray.base.utils;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;


public class HtmlUtils {

    /**
     *
     * @Title: clearHtmlTagString
     * @Description: TODO(去除HTML标签)
     * @param @param sourceContent
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String escapeHtmlTag(String sourceContent) {
        sourceContent = escapeSpecialTag(sourceContent);
        if (StringUtils.isNotEmpty(sourceContent)) {
            sourceContent = sourceContent.replaceAll("<\\/", "</").replaceAll("<\\\\/", "</");
            sourceContent = sourceContent.replaceAll("\\\\t", "");
            sourceContent = sourceContent.replaceAll("\\\\r", "");
            sourceContent = sourceContent.replaceAll("\\\\n", "");
        }
        return Jsoup.parse((sourceContent)).text().trim();
    }
    /**
     *
     * @Title: escapeSpecialTag
     * @Description: TODO(HTML特殊标签替换)
     * @param @param source
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String escapeSpecialTag(String source) {
        source = source.replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&apos;", "\'")
                .replaceAll("&quot;", "\"")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&copy;", "©")
                .replaceAll("&reg;", "®");
        return source;
    }

    public static Map<String, String> formatCookie(String cookieString) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(cookieString)) {
            String[] cookies = cookieString.split(";");
            for (String cookie : cookies) {
                int index = cookie.indexOf("=");
                if(index!=-1){
                    map.put(cookie.substring(0, index).trim(), cookie.substring(index + 1).trim());
                }
            }
        }
        return map;
    }



    /**
     * 提取网页中的表单数据
     * @param source 网页源代码
     * @return 表单数据
     */
    public static Map<String, String> extractForm(String source) {
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isNotBlank(source)) {
            Document doc = Jsoup.parse(source);
            Elements elements = doc.select("input");
            for (Element element : elements) {
                String key;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(element.attr("name"))) {
                    key = element.attr("name");
                } else if (org.apache.commons.lang3.StringUtils.isNotBlank(element.id())) {
                    key = element.id();
                } else {
                    continue;
                }
                map.put(key, element.val());
            }
        }

        return map;
    }



}
