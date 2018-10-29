package com.trackray.scanner.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式工具类
 * @author fre1Ray
 * */
public class ReUtils {

	private static String TARGET ; //目标字符串
	private static String REG ;	//规则
	private static Pattern PAT ;
	private static Matcher MAT ;
	
	/**
	 * 公共判断
	 * */
	private static boolean judge(){
		PAT = Pattern.compile(REG);
		MAT = PAT.matcher(TARGET);
		boolean rs = MAT.find();
		return rs;
	}
	
	/**
	 * 是否为电话号码
	 * @param target 目标字符串
	 * */
	public static boolean isTel(String s){
		TARGET = s;
		REG = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		return judge();
	}
	
	/**
	 * 是否为ip
	 * @param target 目标字符串
	 * */
	public static boolean isIp(String s){
		TARGET = s;
		REG = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}";
		return judge();
	}
	
	/**
	 * 是否为邮箱
	 * @param target 目标字符串
	 * */
	public static boolean isMail(String s){
		TARGET = s;
		REG = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return judge();
	}
	
	/**
	 * 是否为域名
	 * @param target 目标字符串
	 * */
	public static boolean isDomain(String s){
		TARGET = s;
		REG = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?";
		return judge();
	}

	/**
	 * 是否为数字
	 * @param target 目标字符串
	 * */
	public static boolean isNumber(String s){
		TARGET = s;
		REG = "^-?[1-9]\\\\d*$";
		return judge();
	}

	/**
	 * 是否为url
	 * @param target 目标字符串
	 * */
	public static boolean isURL(String s){
		TARGET = s;
		REG = "^((https|http|ftp|rtsp|mms)?://)"  
		        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@   
		        + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184   
		        + "|" // 允许IP和DOMAIN（域名）   
		        + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.   
		        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名   
		        + "[a-z]{2,6})" // first level domain- .com or .museum   
		        + "(:[0-9]{1,4})?" // 端口- :80 <br>  
		        + "((/?)|" // a slash isn't required if there is no file name   
		        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
		return judge();
	}
	
	//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑校验字符串↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓提取字符串↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	/**
	 * 公共提取方法
	 * */
	private static String publicGet(){
		PAT = Pattern.compile(REG);
		MAT = PAT.matcher(TARGET);
		while(MAT.find()){
			String result = MAT.group(0);
			return result;
		}
		return null;
	}
	
	/**
	 * 提取ip
	 * @param target 操作字符串
	 * */
	public static String getIp(String target){
		TARGET = target;
		REG = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		return publicGet();
	}
	
	/**
	 * 提取电话
	 * @param target 操作字符串
	 * */
	public static String getTel(String target){
		TARGET = target;
		REG = "(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}";
		return publicGet();
	}
	
	/**
	 * 提取邮箱
	 * @param target 操作字符串
	 * */
	public static String getMail(String target){
		TARGET = target;
		REG = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";
		return publicGet();
	}
	
	/**
	 * 提取域名
	 * @param target 操作字符串
	 * */
	public static String getDomain(String target){
		TARGET = target;
		REG = "([a-z0-9--]{1,200})\\.(ac.cn|bj.cn|sh.cn|tj.cn|cq.cn|he.cn|sn.cn|sx.cn|nm.cn|ln.cn|jl.cn|hl.cn|js.cn|zj.cn|ah.cn|fj.cn|jx.cn|sd.cn|ha.cn|hb.cn|hn.cn|gd.cn|gx.cn|hi.cn|sc.cn|gz.cn|yn.cn|gs.cn|qh.cn|nx.cn|xj.cn|tw.cn|hk.cn|mo.cn|xz.cn" +
            "|com.cn|net.cn|org.cn|gov.cn|我爱你|在线|中国|网址|网店|中文网|公司|网络|集团" +
            "|com|cn|me|cc|org|net|xin|xyz|vip|shop|top|club|wang|fun|info|online|tech|store|site|ltd|ink|biz|group|link|work|pro|mobi|ren|kim|name|tv|red" +
            "|cool|team|live|pub|company|zone|today|video|art|chat|gold|guru|show|life|love|email|fund|city|plus|design|social|center|world|auto)";
		return publicGet();
	}
	
	/**
	 * 提取备案号
	 * @param target 操作字符串
	 * */
	public static String getRecord(String target){
		TARGET = target;
		REG = "";
		return publicGet();
	}
	
	public static void main(String[] args) {
		String b = getDomain("www.&&baidu.com && mspaint");
		System.out.println(b);
	}
}
