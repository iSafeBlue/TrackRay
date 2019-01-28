package com.trackray.base.utils;

import com.trackray.base.bean.IPInfo;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * 域名解析类
 * @author fre1Ray
 * */
public class DomainUtils {
	
	/**
	 * 获取域名的全部信息
	 * */
	public static IPInfo get(String domain){
		String b = ReUtils.getDomain(domain);
		if(b == null){
			return null;
		}
		IPInfo ipInfo = new IPInfo();
		
		String ip = getHostAddress(domain);
		IPInfo ipInfo1 = analysisIP(ip);
		if (ipInfo1!=null){
			ipInfo =ipInfo1;
		}
		ipInfo.setIp(ip);
		IPInfo ipInfo2 = getDNS(domain);
		
	//	BeanUtils.copyProperties(ipInfo2, ipInfo);  垃圾工具类
		ipInfo.setDomain(ipInfo2.getDomain());
		ipInfo.setDNS(ipInfo2.getDNS());
		ipInfo.setDNSList(ipInfo2.getDNSList());
		
		
		return ipInfo;
	}
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

	
	/**
	 * 解析ip
	 * */
	public static IPInfo analysisIP(String ip){
		HttpClient client = new HttpClient();
		ResponseStatus responseStatus;
		try {
			responseStatus = client.get("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
			IPInfo analysisIPJSON = analysisIPJSON(responseStatus.getContent());//获取基本信息
			return analysisIPJSON;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ip_json解析
	 * */
	private static IPInfo analysisIPJSON(String data){
		String[] split = data.split("data\"");
		data = split[1];
		data = data.replace("{","");
		data = data.replace("}","");
		data = data.replace("\"","");
		data = data.replace(",", " ,");
		data = data.substring(1);
		String[] split2 = data.split(",");
		IPInfo info = new IPInfo();
		info.setIp(split2[0].split(":")[1].trim());
		info.setCountry(split2[1].split(":")[1].trim());
		info.setArea(split2[2].split(":")[1].trim());
		info.setRegion(split2[3].split(":")[1].trim());
		info.setCity(split2[4].split(":")[1].trim());
		info.setCounty(split2[5].split(":")[1].trim());
		info.setIsp(split2[6].split(":")[1].trim());
		info.setCountryId(split2[7].split(":")[1].trim());
		info.setAreaId(split2[8].split(":")[1].trim());
		info.setRegionId(split2[9].split(":")[1].trim());
		info.setCityId(split2[10].split(":")[1].trim());
		info.setCounty(split2[11].split(":")[1].trim());
		info.setIsp(split2[12].split(":")[1].trim());
		
	
	//	List<IPInfo> parseArray = JSON.parseArray(data, IPInfo.class);
	//	String jsonString = JSON.toJSONString(data);
	//	IPInfo parseObject = JSON.parseObject(jsonString, IPInfo.class);
		
		return info;
	}
	
	
	/**
	 * 根据域名获取DNS服务器
	 * @throws IOException 
	 * 
	 * */
	public static IPInfo getDNS(String domain){
		String b = ReUtils.getDomain(domain);
		if(b == null){
			return null;
		}
		IPInfo ipInfo = new IPInfo();
		String commandStr = "cmd /C nslookup -qt=ns "+b+" 8.8.8.8";
		String execShell = execShell(commandStr);	//获取command命令的返回信息
		
		ipInfo = StrUtils.getDNSDomain(execShell);
		
		return ipInfo;
	}
	
	/**
	 * 执行自定义cmd命令
	 * @return String
	 * */
	public static String execCustomShell(String command){
		if(!command.startsWith("cmd") || command.equals("") || command == null){
			return null;
		}
		String execShell = execShell(command);
		return execShell;
	}
	
	/**
	 * 执行shell
	 * */
	private static String execShell(String commandStr){
		Process process;
		String analysisProcess = null;
		try {
			process = Runtime.getRuntime().exec(commandStr);
			process.waitFor();
			analysisProcess = IOUtils.analysisProcess(process);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return analysisProcess;
	}
	
	/**
	 * 各种测试
	 * */
	public static void main(String[] args) throws UnknownHostException {
		
	//	String hostAddress = getDNSAddress("www.baidu.com");
		IPInfo info = get("baidu.com");
		System.out.println(info);
	//	String[] domainById = getDomainById("14.215.177.38");
	}
}
