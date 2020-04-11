package com.trackray.base.attack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.utils.StrUtils;
import com.trackray.base.utils.SysLog;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.PostConstruct;

/**
 * Awvs实现类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/24 13:07
 */
@Component("awvs")
@Data
public class Awvs {

    public   static class State{
        public static final String ABORTED = "aborted";
        public static final String ABORTING = "aborting";
        public static final String PROCESSING = "processing";
        public static final String FAILED = "failed";
        public static final String COMPLETED = "completed";
    }
    @Value("${awvs.key}")
    private String apikey = "";
    @Value("${awvs.host}")
    private String host = "https://127.0.0.1:3443/";

    public static boolean ok;

    private Header[] headers;

    public void initCheck(){
        headers = new Header[]{
                new BasicHeader("X-Auth",apikey),
                new BasicHeader("content-type","application/json"),
        };

        CrawlerPage page = new CrawlerPage();
        page.getRequest().setUrl(host+"/api/v1/scans");
        page.getRequest().setHttpHeaders(headers);
        page.getRequest().setHttpMethod(HttpMethod.GET);
        f.run(page);
        int statusCode = page.getResponse().getStatus().getStatusCode();
        if (statusCode>=200 && statusCode<300){
            ok = true;
            SysLog.info("[AWVS] AWVS配置正常");
        }else {
            ok = false;
            SysLog.error("[AWVS] AWVS配置异常请检查");
        }

    }

    private static Fetcher f = new Fetcher();

    String send(String url , HttpMethod method){
        return send(url,"",method);
    }
    String send(String url , String data , HttpMethod method){
        CrawlerPage page = new CrawlerPage();
        page.getRequest().setUrl(host+url);
        page.getRequest().setHttpHeaders(headers);
        page.getRequest().setParamStr(data);
        page.getRequest().setHttpMethod(method);
        f.run(page);
        return page.getResponse().getStatus().getContentString();
    }

    public JSONObject scansJSON(){
        String scans = send("/api/v1/scans", HttpMethod.GET);
        return JSONObject.fromObject(scans);
    }

    public List<Scan> scans(){
        List <Scan> list = new ArrayList<>();
        JSONObject obj = scansJSON();
        if (obj.containsKey("scans")){
            JSONArray scans = obj.getJSONArray("scans");
            for (int i = 0; i < scans.size(); i++) {
                JSONObject scan = scans.getJSONObject(i);
                try {
                    Scan s = this.toBean(scan.toString(), Scan. class);
                    list.add(s);
                } catch (IOException e) {
                    continue;
                }
            }
        }
        return list;
    }

    public Scan scan(String scanId){
        String scan = send(String.format("/api/v1/scans/%s",scanId), HttpMethod.GET);
        try {
            return toBean(scan,Scan.class);
        } catch (IOException e) {
            return null;
        }
    }

    public JSONObject targetsJSON(){
        String scans = send("/api/v1/targets", HttpMethod.GET);
        return JSONObject.fromObject(scans);
    }
    protected <T> T toBean (String json ,  Class <T> clazz) throws IOException {
        return new ObjectMapper().readValue(json,clazz);
    }
    public List<Target> targets(){
        JSONObject obj = targetsJSON();
        List <Target> list = new ArrayList<>();
        if (obj.containsKey("targets")){
            JSONArray scans = obj.getJSONArray("targets");
            for (int i = 0; i < scans.size(); i++) {
                JSONObject scan = scans.getJSONObject(i);
                try {
                    Target s = this.toBean(scan.toString(), Target. class);
                    list.add(s);
                } catch (IOException e) {
                    continue;
                }
            }
        }
        return list;
    }

    public String createTarget(String url){
        JSONObject json = new JSONObject();
        json.put("address",url);
        json.put("description",url);
        json.put("criticality","10");

        String resp = send("/api/v1/targets", json.toString(), HttpMethod.POST);
        JSONObject obj = JSONObject.fromObject(resp);
        return obj.getString("target_id");
    }

    public JSONObject vulnsJSON(String sessionid , String scanid){
        String resp = send(String.format("/api/v1/scans/%s/results/%s/vulnerabilities", scanid, sessionid), HttpMethod.GET);
        JSONObject obj = JSONObject.fromObject(resp);
        return obj;
    }
    public List<Vulnerabilities> vulns(String sessionId,String scanId){
        ArrayList<Vulnerabilities> obj = new ArrayList<>();
        JSONObject jsonObject = vulnsJSON(sessionId, scanId);
        if (jsonObject.containsKey("vulnerabilities")){
            JSONArray vulnerabilities = jsonObject.getJSONArray("vulnerabilities");
            for (int i = 0; i < vulnerabilities.size(); i++) {
                JSONObject vul = vulnerabilities.getJSONObject(i);
                try {
                    Vulnerabilities vulnbean = toBean(vul.toString(), Vulnerabilities. class);
                    /*Vulndetail vulndetail = vuln(scanId, sessionId, vulnbean.getVulnId());
                    vulnbean.getVuln().add(vulndetail);*/
                    obj.add(vulnbean);
                } catch (IOException e) {
                    continue;
                }
            }
        }
        return obj;
    }
    public Vulndetail vuln(String scanid,String sessionid ,String vid){
        String resp = send(String.format("/api/v1/scans/%s/results/%s/vulnerabilities/%s", scanid, sessionid , vid), HttpMethod.GET);
        try {
            return toBean(resp,Vulndetail.class);
        } catch (IOException e) {
            return null;
        }
    }
    public Vulndetail vuln(String vulnid){
        String url = String.format("/api/v1/vulnerabilities/%s", vulnid);
        String resp = send( url, HttpMethod.GET);
        try {
            return toBean(resp,Vulndetail.class);
        } catch (IOException e) {
            return null;
        }
    }

    public String startScan(String targetId){
        String data = "{\"target_id\": \"\", \"profile_id\": \"11111111-1111-1111-1111-111111111111\",\n" +
                "                \"schedule\": {\"disable\": false, \"start_date\": null, \"time_sensitive\": false}}";
        JSONObject jsondata = JSONObject.fromObject(data);

        jsondata.put("target_id",targetId);

        String resp = send("/api/v1/scans", jsondata.toString(), HttpMethod.POST);

        JSONObject respJson = JSONObject.fromObject(resp);
        /**
         * {
         "schedule": {
         "start_date": null,
         "disable": false,
         "time_sensitive": false
         },
         "ui_session_id": null,
         "target_id": "01a1bb0a-0cc9-4e62-a653-bcb3beda208d",
         "profile_id": "11111111-1111-1111-1111-111111111111"
         }
         */
        return respJson.getString("target_id");
    }

    public boolean stopScan(String scanid){
        try {
            send(String.format("/api/v1/scans/%s/abort", scanid), HttpMethod.POST);
        }catch (Exception e){
            return false;
        }
        return true;
    }




@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "address",
        "severity_counts",
        "last_scan_date",
        "threat",
        "criticality",
        "manual_intervention",
        "last_scan_session_id",
        "target_id",
        "continuous_mode",
        "last_scan_id",
        "last_scan_session_status",
        "description"
})
public static class Target {

    @JsonProperty("address")
    private String address;
    @JsonProperty("severity_counts")
    private SeverityCounts severityCounts;
    @JsonProperty("last_scan_date")
    private String lastScanDate;
    @JsonProperty("threat")
    private Integer threat;
    @JsonProperty("criticality")
    private Integer criticality;
    @JsonProperty("manual_intervention")
    private Object manualIntervention;
    @JsonProperty("last_scan_session_id")
    private String lastScanSessionId;
    @JsonProperty("target_id")
    private String targetId;
    @JsonProperty("continuous_mode")
    private Boolean continuousMode;
    @JsonProperty("last_scan_id")
    private String lastScanId;
    @JsonProperty("last_scan_session_status")
    private String lastScanSessionStatus;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The address
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The severityCounts
     */
    @JsonProperty("severity_counts")
    public SeverityCounts getSeverityCounts() {
        return severityCounts;
    }

    /**
     * @param severityCounts The severity_counts
     */
    @JsonProperty("severity_counts")
    public void setSeverityCounts(SeverityCounts severityCounts) {
        this.severityCounts = severityCounts;
    }

    /**
     * @return The lastScanDate
     */
    @JsonProperty("last_scan_date")
    public String getLastScanDate() {
        return lastScanDate;
    }

    /**
     * @param lastScanDate The last_scan_date
     */
    @JsonProperty("last_scan_date")
    public void setLastScanDate(String lastScanDate) {
        this.lastScanDate = lastScanDate;
    }

    /**
     * @return The threat
     */
    @JsonProperty("threat")
    public Integer getThreat() {
        return threat;
    }

    /**
     * @param threat The threat
     */
    @JsonProperty("threat")
    public void setThreat(Integer threat) {
        this.threat = threat;
    }

    /**
     * @return The criticality
     */
    @JsonProperty("criticality")
    public Integer getCriticality() {
        return criticality;
    }

    /**
     * @param criticality The criticality
     */
    @JsonProperty("criticality")
    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    /**
     * @return The manualIntervention
     */
    @JsonProperty("manual_intervention")
    public Object getManualIntervention() {
        return manualIntervention;
    }

    /**
     * @param manualIntervention The manual_intervention
     */
    @JsonProperty("manual_intervention")
    public void setManualIntervention(Object manualIntervention) {
        this.manualIntervention = manualIntervention;
    }

    /**
     * @return The lastScanSessionId
     */
    @JsonProperty("last_scan_session_id")
    public String getLastScanSessionId() {
        return lastScanSessionId;
    }

    /**
     * @param lastScanSessionId The last_scan_session_id
     */
    @JsonProperty("last_scan_session_id")
    public void setLastScanSessionId(String lastScanSessionId) {
        this.lastScanSessionId = lastScanSessionId;
    }

    /**
     * @return The targetId
     */
    @JsonProperty("target_id")
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId The target_id
     */
    @JsonProperty("target_id")
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return The continuousMode
     */
    @JsonProperty("continuous_mode")
    public Boolean getContinuousMode() {
        return continuousMode;
    }

    /**
     * @param continuousMode The continuous_mode
     */
    @JsonProperty("continuous_mode")
    public void setContinuousMode(Boolean continuousMode) {
        this.continuousMode = continuousMode;
    }

    /**
     * @return The lastScanId
     */
    @JsonProperty("last_scan_id")
    public String getLastScanId() {
        return lastScanId;
    }

    /**
     * @param lastScanId The last_scan_id
     */
    @JsonProperty("last_scan_id")
    public void setLastScanId(String lastScanId) {
        this.lastScanId = lastScanId;
    }

    /**
     * @return The lastScanSessionStatus
     */
    @JsonProperty("last_scan_session_status")
    public String getLastScanSessionStatus() {
        return lastScanSessionStatus;
    }

    /**
     * @param lastScanSessionStatus The last_scan_session_status
     */
    @JsonProperty("last_scan_session_status")
    public void setLastScanSessionStatus(String lastScanSessionStatus) {
        this.lastScanSessionStatus = lastScanSessionStatus;
    }

    /**
     * @return The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(address).append(severityCounts).append(lastScanDate).append(threat).append(criticality).append(manualIntervention).append(lastScanSessionId).append(targetId).append(continuousMode).append(lastScanId).append(lastScanSessionStatus).append(description).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Target) == false) {
            return false;
        }
        Target rhs = ((Target) other);
        return new EqualsBuilder().append(address, rhs.address).append(severityCounts, rhs.severityCounts).append(lastScanDate, rhs.lastScanDate).append(threat, rhs.threat).append(criticality, rhs.criticality).append(manualIntervention, rhs.manualIntervention).append(lastScanSessionId, rhs.lastScanSessionId).append(targetId, rhs.targetId).append(continuousMode, rhs.continuousMode).append(lastScanId, rhs.lastScanId).append(lastScanSessionStatus, rhs.lastScanSessionStatus).append(description, rhs.description).append(additionalProperties, rhs.additionalProperties).isEquals();
    }


}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "high",
        "medium",
        "info",
        "low"
})
public static class SeverityCounts {

    @JsonProperty("high")
    private Integer high;
    @JsonProperty("medium")
    private Integer medium;
    @JsonProperty("info")
    private Integer info;
    @JsonProperty("low")
    private Integer low;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The high
     */
    @JsonProperty("high")
    public Integer getHigh() {
        return high;
    }

    /**
     *
     * @param high
     *     The high
     */
    @JsonProperty("high")
    public void setHigh(Integer high) {
        this.high = high;
    }

    /**
     *
     * @return
     *     The medium
     */
    @JsonProperty("medium")
    public Integer getMedium() {
        return medium;
    }

    /**
     *
     * @param medium
     *     The medium
     */
    @JsonProperty("medium")
    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    /**
     *
     * @return
     *     The info
     */
    @JsonProperty("info")
    public Integer getInfo() {
        return info;
    }

    /**
     *
     * @param info
     *     The info
     */
    @JsonProperty("info")
    public void setInfo(Integer info) {
        this.info = info;
    }

    /**
     *
     * @return
     *     The low
     */
    @JsonProperty("low")
    public Integer getLow() {
        return low;
    }

    /**
     *
     * @param low
     *     The low
     */
    @JsonProperty("low")
    public void setLow(Integer low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(high).append(medium).append(info).append(low).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SeverityCounts) == false) {
            return false;
        }
        SeverityCounts rhs = ((SeverityCounts) other);
        return new EqualsBuilder().append(high, rhs.high).append(medium, rhs.medium).append(info, rhs.info).append(low, rhs.low).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "event_level",
        "progress",
        "start_date",
        "severity_counts",
        "status",
        "threat",
        "scan_session_id"
})
public static class CurrentSession {

    @JsonProperty("event_level")
    private Integer eventLevel;
    @JsonProperty("progress")
    private Integer progress;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("severity_counts")
    private SeverityCounts severityCounts;
    @JsonProperty("status")
    private String status;
    @JsonProperty("threat")
    private Integer threat;
    @JsonProperty("scan_session_id")
    private String scanSessionId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The eventLevel
     */
    @JsonProperty("event_level")
    public Integer getEventLevel() {
        return eventLevel;
    }

    /**
     *
     * @param eventLevel
     *     The event_level
     */
    @JsonProperty("event_level")
    public void setEventLevel(Integer eventLevel) {
        this.eventLevel = eventLevel;
    }

    /**
     *
     * @return
     *     The progress
     */
    @JsonProperty("progress")
    public Integer getProgress() {
        return progress;
    }

    /**
     *
     * @param progress
     *     The progress
     */
    @JsonProperty("progress")
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     *
     * @return
     *     The startDate
     */
    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     *     The start_date
     */
    @JsonProperty("start_date")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     *     The severityCounts
     */
    @JsonProperty("severity_counts")
    public SeverityCounts getSeverityCounts() {
        return severityCounts;
    }

    /**
     *
     * @param severityCounts
     *     The severity_counts
     */
    @JsonProperty("severity_counts")
    public void setSeverityCounts(SeverityCounts severityCounts) {
        this.severityCounts = severityCounts;
    }

    /**
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The threat
     */
    @JsonProperty("threat")
    public Integer getThreat() {
        return threat;
    }

    /**
     *
     * @param threat
     *     The threat
     */
    @JsonProperty("threat")
    public void setThreat(Integer threat) {
        this.threat = threat;
    }

    /**
     *
     * @return
     *     The scanSessionId
     */
    @JsonProperty("scan_session_id")
    public String getScanSessionId() {
        return scanSessionId;
    }

    /**
     *
     * @param scanSessionId
     *     The scan_session_id
     */
    @JsonProperty("scan_session_id")
    public void setScanSessionId(String scanSessionId) {
        this.scanSessionId = scanSessionId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventLevel).append(progress).append(startDate).append(severityCounts).append(status).append(threat).append(scanSessionId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CurrentSession) == false) {
            return false;
        }
        CurrentSession rhs = ((CurrentSession) other);
        return new EqualsBuilder().append(eventLevel, rhs.eventLevel).append(progress, rhs.progress).append(startDate, rhs.startDate).append(severityCounts, rhs.severityCounts).append(status, rhs.status).append(threat, rhs.threat).append(scanSessionId, rhs.scanSessionId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "next_run",
        "current_session",
        "target_id",
        "profile_name",
        "profile_id",
        "criticality",
        "report_template_id",
        "scan_id"
})
public static class Scan {

    @JsonProperty("next_run")
    private Object nextRun;
    @JsonProperty("current_session")
    private CurrentSession currentSession;
    @JsonProperty("target_id")
    private String targetId;
    @JsonProperty("profile_name")
    private String profileName;
    @JsonProperty("profile_id")
    private String profileId;
    @JsonProperty("criticality")
    private Integer criticality;
    @JsonProperty("report_template_id")
    private Object reportTemplateId;
    @JsonProperty("scan_id")
    private String scanId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The nextRun
     */
    @JsonProperty("next_run")
    public Object getNextRun() {
        return nextRun;
    }

    /**
     *
     * @param nextRun
     *     The next_run
     */
    @JsonProperty("next_run")
    public void setNextRun(Object nextRun) {
        this.nextRun = nextRun;
    }

    /**
     *
     * @return
     *     The currentSession
     */
    @JsonProperty("current_session")
    public CurrentSession getCurrentSession() {
        return currentSession;
    }

    /**
     *
     * @param currentSession
     *     The current_session
     */
    @JsonProperty("current_session")
    public void setCurrentSession(CurrentSession currentSession) {
        this.currentSession = currentSession;
    }

    /**
     *
     * @return
     *     The targetId
     */
    @JsonProperty("target_id")
    public String getTargetId() {
        return targetId;
    }

    /**
     *
     * @param targetId
     *     The target_id
     */
    @JsonProperty("target_id")
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     *
     * @return
     *     The profileName
     */
    @JsonProperty("profile_name")
    public String getProfileName() {
        return profileName;
    }

    /**
     *
     * @param profileName
     *     The profile_name
     */
    @JsonProperty("profile_name")
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     *
     * @return
     *     The profileId
     */
    @JsonProperty("profile_id")
    public String getProfileId() {
        return profileId;
    }

    /**
     *
     * @param profileId
     *     The profile_id
     */
    @JsonProperty("profile_id")
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     *
     * @return
     *     The criticality
     */
    @JsonProperty("criticality")
    public Integer getCriticality() {
        return criticality;
    }

    /**
     *
     * @param criticality
     *     The criticality
     */
    @JsonProperty("criticality")
    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    /**
     *
     * @return
     *     The reportTemplateId
     */
    @JsonProperty("report_template_id")
    public Object getReportTemplateId() {
        return reportTemplateId;
    }

    /**
     *
     * @param reportTemplateId
     *     The report_template_id
     */
    @JsonProperty("report_template_id")
    public void setReportTemplateId(Object reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    /**
     *
     * @return
     *     The scanId
     */
    @JsonProperty("scan_id")
    public String getScanId() {
        return scanId;
    }

    /**
     *
     * @param scanId
     *     The scan_id
     */
    @JsonProperty("scan_id")
    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nextRun).append(currentSession).append(targetId).append(profileName).append(profileId).append(criticality).append(reportTemplateId).append(scanId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Scan) == false) {
            return false;
        }
        Scan rhs = ((Scan) other);
        return new EqualsBuilder().append(nextRun, rhs.nextRun).append(currentSession, rhs.currentSession).append(targetId, rhs.targetId).append(profileName, rhs.profileName).append(profileId, rhs.profileId).append(criticality, rhs.criticality).append(reportTemplateId, rhs.reportTemplateId).append(scanId, rhs.scanId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "affects_url",
        "vuln_id",
        "last_seen",
        "severity",
        "criticality",
        "vt_id",
        "target_id",
        "vt_name",
        "tags",
        "loc_id",
        "status",
        "affects_detail"
})
public static class Vulnerabilities {

    @JsonProperty("affects_url")
    private String affectsUrl;
    @JsonProperty("vuln_id")
    private String vulnId;
    @JsonProperty("last_seen")
    private Object lastSeen;
    @JsonProperty("severity")
    private Integer severity;
    @JsonProperty("criticality")
    private Integer criticality;
    @JsonProperty("vt_id")
    private String vtId;
    @JsonProperty("target_id")
    private String targetId;
    @JsonProperty("vt_name")
    private String vtName;
    @JsonProperty("tags")
    private List<String> tags = new ArrayList<String>();
    @JsonProperty("loc_id")
    private Integer locId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("affects_detail")
    private String affectsDetail;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private Vulndetail vuln ;


    public Vulndetail getVuln() {
        return vuln;
    }

    public void setVuln(Vulndetail vuln) {
        this.vuln = vuln;
    }

    /**
     *
     * @return
     *     The affectsUrl
     */
    @JsonProperty("affects_url")
    public String getAffectsUrl() {
        return affectsUrl;
    }

    /**
     *
     * @param affectsUrl
     *     The affects_url
     */
    @JsonProperty("affects_url")
    public void setAffectsUrl(String affectsUrl) {
        this.affectsUrl = affectsUrl;
    }

    /**
     *
     * @return
     *     The vulnId
     */
    @JsonProperty("vuln_id")
    public String getVulnId() {
        return vulnId;
    }

    /**
     *
     * @param vulnId
     *     The vuln_id
     */
    @JsonProperty("vuln_id")
    public void setVulnId(String vulnId) {
        this.vulnId = vulnId;
    }

    /**
     *
     * @return
     *     The lastSeen
     */
    @JsonProperty("last_seen")
    public Object getLastSeen() {
        return lastSeen;
    }

    /**
     *
     * @param lastSeen
     *     The last_seen
     */
    @JsonProperty("last_seen")
    public void setLastSeen(Object lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     *
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     *
     * @return
     *     The criticality
     */
    @JsonProperty("criticality")
    public Integer getCriticality() {
        return criticality;
    }

    /**
     *
     * @param criticality
     *     The criticality
     */
    @JsonProperty("criticality")
    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    /**
     *
     * @return
     *     The vtId
     */
    @JsonProperty("vt_id")
    public String getVtId() {
        return vtId;
    }

    /**
     *
     * @param vtId
     *     The vt_id
     */
    @JsonProperty("vt_id")
    public void setVtId(String vtId) {
        this.vtId = vtId;
    }

    /**
     *
     * @return
     *     The targetId
     */
    @JsonProperty("target_id")
    public String getTargetId() {
        return targetId;
    }

    /**
     *
     * @param targetId
     *     The target_id
     */
    @JsonProperty("target_id")
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     *
     * @return
     *     The vtName
     */
    @JsonProperty("vt_name")
    public String getVtName() {
        return vtName;
    }

    /**
     *
     * @param vtName
     *     The vt_name
     */
    @JsonProperty("vt_name")
    public void setVtName(String vtName) {
        this.vtName = vtName;
    }

    /**
     *
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     *     The locId
     */
    @JsonProperty("loc_id")
    public Integer getLocId() {
        return locId;
    }

    /**
     *
     * @param locId
     *     The loc_id
     */
    @JsonProperty("loc_id")
    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    /**
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The affectsDetail
     */
    @JsonProperty("affects_detail")
    public String getAffectsDetail() {
        return affectsDetail;
    }

    /**
     *
     * @param affectsDetail
     *     The affects_detail
     */
    @JsonProperty("affects_detail")
    public void setAffectsDetail(String affectsDetail) {
        this.affectsDetail = affectsDetail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vulnId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vulnerabilities) == false) {
            return false;
        }
        Vulnerabilities rhs = ((Vulnerabilities) other);
        return new EqualsBuilder().append(vulnId, rhs.vulnId).isEquals();
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "vuln_id",
        "severity",
        "criticality",
        "vt_id",
        "cvss2",
        "tags",
        "affects_detail",
        "affects_url",
        "cvss3",
        "cvss_score",
        "target_id",
        "vt_name",
        "loc_id",
        "source",
        "status",
        "request"
})
public static class Vulndetail {

    @JsonProperty("vuln_id")
    private String vulnId;
    @JsonProperty("severity")
    private Integer severity;
    @JsonProperty("criticality")
    private Integer criticality;
    @JsonProperty("vt_id")
    private String vtId;
    @JsonProperty("cvss2")
    private String cvss2;
    @JsonProperty("tags")
    private List<String> tags = new ArrayList<String>();
    @JsonProperty("affects_detail")
    private String affectsDetail;
    @JsonProperty("affects_url")
    private String affectsUrl;
    @JsonProperty("cvss3")
    private String cvss3;
    @JsonProperty("cvss_score")
    private Double cvssScore;
    @JsonProperty("target_id")
    private String targetId;
    @JsonProperty("vt_name")
    private String vtName;
    @JsonProperty("loc_id")
    private Integer locId;
    @JsonProperty("source")
    private String source;
    @JsonProperty("status")
    private String status;
    @JsonProperty("request")
    private String request;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The vulnId
     */
    @JsonProperty("vuln_id")
    public String getVulnId() {
        return vulnId;
    }

    /**
     *
     * @param vulnId
     *     The vuln_id
     */
    @JsonProperty("vuln_id")
    public void setVulnId(String vulnId) {
        this.vulnId = vulnId;
    }

    /**
     *
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     *
     * @return
     *     The criticality
     */
    @JsonProperty("criticality")
    public Integer getCriticality() {
        return criticality;
    }

    /**
     *
     * @param criticality
     *     The criticality
     */
    @JsonProperty("criticality")
    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    /**
     *
     * @return
     *     The vtId
     */
    @JsonProperty("vt_id")
    public String getVtId() {
        return vtId;
    }

    /**
     *
     * @param vtId
     *     The vt_id
     */
    @JsonProperty("vt_id")
    public void setVtId(String vtId) {
        this.vtId = vtId;
    }

    /**
     *
     * @return
     *     The cvss2
     */
    @JsonProperty("cvss2")
    public String getCvss2() {
        return cvss2;
    }

    /**
     *
     * @param cvss2
     *     The cvss2
     */
    @JsonProperty("cvss2")
    public void setCvss2(String cvss2) {
        this.cvss2 = cvss2;
    }

    /**
     *
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     *     The affectsDetail
     */
    @JsonProperty("affects_detail")
    public String getAffectsDetail() {
        return affectsDetail;
    }

    /**
     *
     * @param affectsDetail
     *     The affects_detail
     */
    @JsonProperty("affects_detail")
    public void setAffectsDetail(String affectsDetail) {
        this.affectsDetail = affectsDetail;
    }

    /**
     *
     * @return
     *     The affectsUrl
     */
    @JsonProperty("affects_url")
    public String getAffectsUrl() {
        return affectsUrl;
    }

    /**
     *
     * @param affectsUrl
     *     The affects_url
     */
    @JsonProperty("affects_url")
    public void setAffectsUrl(String affectsUrl) {
        this.affectsUrl = affectsUrl;
    }

    /**
     *
     * @return
     *     The cvss3
     */
    @JsonProperty("cvss3")
    public String getCvss3() {
        return cvss3;
    }

    /**
     *
     * @param cvss3
     *     The cvss3
     */
    @JsonProperty("cvss3")
    public void setCvss3(String cvss3) {
        this.cvss3 = cvss3;
    }

    /**
     *
     * @return
     *     The cvssScore
     */
    @JsonProperty("cvss_score")
    public Double getCvssScore() {
        return cvssScore;
    }

    /**
     *
     * @param cvssScore
     *     The cvss_score
     */
    @JsonProperty("cvss_score")
    public void setCvssScore(Double cvssScore) {
        this.cvssScore = cvssScore;
    }

    /**
     *
     * @return
     *     The targetId
     */
    @JsonProperty("target_id")
    public String getTargetId() {
        return targetId;
    }

    /**
     *
     * @param targetId
     *     The target_id
     */
    @JsonProperty("target_id")
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     *
     * @return
     *     The vtName
     */
    @JsonProperty("vt_name")
    public String getVtName() {
        return vtName;
    }

    /**
     *
     * @param vtName
     *     The vt_name
     */
    @JsonProperty("vt_name")
    public void setVtName(String vtName) {
        this.vtName = vtName;
    }

    /**
     *
     * @return
     *     The locId
     */
    @JsonProperty("loc_id")
    public Integer getLocId() {
        return locId;
    }

    /**
     *
     * @param locId
     *     The loc_id
     */
    @JsonProperty("loc_id")
    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    /**
     *
     * @return
     *     The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The request
     */
    @JsonProperty("request")
    public String getRequest() {
        return request;
    }

    /**
     *
     * @param request
     *     The request
     */
    @JsonProperty("request")
    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vulnId).append(severity).append(criticality).append(vtId).append(cvss2).append(tags).append(affectsDetail).append(affectsUrl).append(cvss3).append(cvssScore).append(targetId).append(vtName).append(locId).append(source).append(status).append(request).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vulndetail) == false) {
            return false;
        }
        Vulndetail rhs = ((Vulndetail) other);
        return new EqualsBuilder().append(vulnId, rhs.vulnId).append(severity, rhs.severity).append(criticality, rhs.criticality).append(vtId, rhs.vtId).append(cvss2, rhs.cvss2).append(tags, rhs.tags).append(affectsDetail, rhs.affectsDetail).append(affectsUrl, rhs.affectsUrl).append(cvss3, rhs.cvss3).append(cvssScore, rhs.cvssScore).append(targetId, rhs.targetId).append(vtName, rhs.vtName).append(locId, rhs.locId).append(source, rhs.source).append(status, rhs.status).append(request, rhs.request).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}

}
