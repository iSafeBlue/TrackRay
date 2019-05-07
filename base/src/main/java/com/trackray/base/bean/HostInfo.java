package com.trackray.base.bean;

import lombok.Data;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * IP实体类
 * 包含基本ip信息
 * @author fre1Ray
 * */
@Data
public class HostInfo {

	private String host;
	private String ip;		//PING出来的IP
	private String realIP;	//真实IP
	private Set<String> otherIP = new HashSet<>();//CDN的所有IP
	private List<String> cSegment = new ArrayList<>();//C段

	private String domain;	//目标域名
	private String rootDomain;	//根域名

	private String whois;	//域名whois
	private List<String> dns = new ArrayList<>();;//dns服务器

	private boolean cdn;	//是否使用CDN
	private boolean waf;	//是否使用WAF


}
