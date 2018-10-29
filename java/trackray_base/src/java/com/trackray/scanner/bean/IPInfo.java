package com.trackray.scanner.bean;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * IP实体类
 * 包含基本ip信息
 * @author fre1Ray
 * */
public class IPInfo {

	private String ip;//真实IP
	private String country;
	private String area;
	private String region;
	private String city;
	private String county;
	private String isp;
	private String countryId;
	private String areaId;
	private String regionId;
	private String cityId;
	private String countyId;
	private String ispId;
	
	//-----temp
	private String domain;	//域名
	private String host;	//主机名
	private String DNS;
	private List<String> DNSList;	//DNS域名集合

	private String icp;//备案号
	//c段
	private List<JSONObject> cList = new ArrayList<JSONObject>();
	//域名whois
	private String whois;

	public String getIcp() {
		return icp;
	}

	public void setIcp(String icp) {
		this.icp = icp;
	}

	public String getWhois() {
		return whois;
	}

	public void setWhois(String whois) {
		this.whois = whois;
	}

	public List<JSONObject> getcList() {
		return cList;
	}

	public void setcList(List<JSONObject> cList) {
		this.cList = cList;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getIsp() {
		return isp;
	}
	public void setIsp(String isp) {
		this.isp = isp;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getIspId() {
		return ispId;
	}
	public void setIspId(String ispId) {
		this.ispId = ispId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDNS() {
		return DNS;
	}
	public void setDNS(String dNS) {
		DNS = dNS;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public List<String> getDNSList() {
		return DNSList;
	}
	public void setDNSList(List<String> dNSList) {
		DNSList = dNSList;
	}
	@Override
	public String toString() {
		return "IPInfo [ip=" + ip + ", country=" + country + ", area=" + area
				+ ", region=" + region + ", city=" + city + ", county="
				+ county + ", isp=" + isp + ", countryId=" + countryId
				+ ", areaId=" + areaId + ", regionId=" + regionId + ", cityId="
				+ cityId + ", countyId=" + countyId + ", ispId=" + ispId
				+ ", domain=" + domain + ", host=" + host + ", DNS=" + DNS
				+ ", DNSList=" + DNSList + "]";
	}
	
	
	
}
