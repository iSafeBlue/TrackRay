package com.trackray.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
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

	@JSONField(name="主机名")
	private String host;

	@JSONField(name="IP地址")
	private String ip;		//PING出来的IP
	@JSONField(name="真实IP地址")
	private String realIP;	//真实IP
	@JSONField(name="其他IP地址")
	private Set<String> otherIP = new HashSet<>();//CDN的所有IP

	@JSONField(name="C段")
	private List<String> cSegment = new ArrayList<>();//C段

	@JSONField(name="域名")
	private String domain;	//目标域名
	@JSONField(name="根域名")
	private String rootDomain;	//根域名
	@JSONField(name="域名WHOIS")
	private String whois;	//域名whois
	@JSONField(name="DNS服务器")
	private List<String> dns = new ArrayList<>();;//dns服务器
	@JSONField(name="是否使用CDN")
	private boolean cdn;	//是否使用CDN
	@JSONField(name="是否使用WAF")
	private boolean waf;	//是否使用WAF

}
