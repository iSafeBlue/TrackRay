package com.trackray.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则提取
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/24 17:27
 */
public class ExtractUtils {

    public final static String TEL = "(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}";

    public final static String DOMAIN = "([a-z0-9--]{1,200})\\.(ac.cn|bj.cn|sh.cn|tj.cn|cq.cn|he.cn|sn.cn|sx.cn|nm.cn|ln.cn|jl.cn|hl.cn|js.cn|zj.cn|ah.cn|fj.cn|jx.cn|sd.cn|ha.cn|hb.cn|hn.cn|gd.cn|gx.cn|hi.cn|sc.cn|gz.cn|yn.cn|gs.cn|qh.cn|nx.cn|xj.cn|tw.cn|hk.cn|mo.cn|xz.cn" +
            "|com.cn|net.cn|org.cn|gov.cn|我爱你|在线|中国|网址|网店|中文网|公司|网络|集团" +
            "|com|cn|me|cc|org|net|xin|xyz|vip|shop|top|club|wang|fun|info|online|tech|store|site|ltd|ink|biz|group|link|work|pro|mobi|ren|kim|name|tv|red" +
            "|cool|team|live|pub|company|zone|today|video|art|chat|gold|guru|show|life|love|email|fund|city|plus|design|social|center|world|auto)";
    public final static String EMAIL = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";

    public final static String IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";


    public static boolean matche(String target , String regex){
        return target.matches(regex);
    }

    public static String extract(String target , String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        while(matcher.find()){
            String result = matcher.group(0);
            return result;
        }

        return RegexUtil.extractStr(target,regex);
    }

}
