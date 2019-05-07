package com.trackray.base.utils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * 域名解析类
 * @author fre1Ray
 * */
public class DomainUtils {
	
	public static String getHost(String url){
		try {
			return new URL(url).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 获取指定域名的真实IP地址
	 * */
	public static String getHostAddress(String domain){
		String hostAddress = "";
		try {
			hostAddress = InetAddress.getByName(domain).getHostAddress();
		} catch (UnknownHostException e) {
			return "";
		}
		return hostAddress;
	}
	public static String getHostAddressThrow(String domain) throws UnknownHostException {
		return  InetAddress.getByName(domain).getHostAddress();
	}


}
