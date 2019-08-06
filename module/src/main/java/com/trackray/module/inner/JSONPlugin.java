package com.trackray.module.inner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.utils.CheckUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/4 17:51
 */
@Rule
@Plugin(value = "json", title = "鲲鹏JSON插件调用" ,author = "浅蓝")
public class JSONPlugin extends MVCPlugin{

    public static String jsonPath = Constant.RESOURCES_PATH.concat("json/");

    @Override
    public void index() {

        HashMap<String, String> plugins = fuckJsonList();

        model.addObject("plugins",plugins);

        model.setViewName("index");
    }

    @Function
    public void attack(){


        ArrayList<String> result = new ArrayList<>();

        String urls = param.remove("urls").toString();
        for (String url : urls.split("\r\n")) {

            for (String k : fuckJsonList().keySet()) {
                if (!param.containsKey(k))
                    continue;
                try {
                    String code = FileUtils.readFileToString(new File(jsonPath.concat(k)) , "utf-8");

                    JSONVul jsonVul = new ObjectMapper().readValue(code, JSONVul.class);

                    boolean executeResult = this.execute(url, jsonVul);

                    if (executeResult){
                        result.add(url.concat(jsonVul.getRequest().getPath()) + " ---- " +  k + " ---- " + jsonVul.getMeta().getName());
                    }

                } catch (IOException e) {
                    continue;
                }


            }


        }

        model.addObject("results",result);

        model.setViewName("result");

    }

    public boolean execute(String url , JSONVul jsonVul) {
        try {
            Request request = jsonVul.getRequest();

            Verify verify = jsonVul.getVerify();

            HttpResponse req = null;

            if (StringUtils.isNotBlank(request.getPostData())){
                req = requests.url(url.concat(request.getPath()))
                        .data(request.getPostData())
                        .post();
            }else{
                req = requests.url(url.concat(request.getPath()))
                        .get();
            }


            if (verify.getType().contains("string")){
                    return StringUtils.contains(req.body() , verify.getMatch());
            }else if(verify.getType().contains("md5")){
                return matchMd5(req , verify.getMatch());
            }

        }catch (Exception e){
            return false;
        }
        return false;
    }
    @Value("${temp.dir}")
    private String temp;
    private boolean matchMd5(HttpResponse req, String match) {
        if (req.getStatusCode() != 200)
            return false;
        String uuid = UUID.randomUUID().toString();
        String body = req.body();
        File file = new File(temp.concat(uuid));
        try {
            FileUtils.writeStringToFile(file,body);
            String md5 = DigestUtils.md5Hex(new FileInputStream(file));
            return md5.equals(match);
        } catch (IOException e) {
            return false;
        }finally {
            if (file.exists())
                file.delete();
        }
    }

    public HashMap<String, String> fuckJsonList(){
        HashMap<String, String> map = new HashMap<>();
        File file = new File(jsonPath);
        if (file.isDirectory()){
            String[] list = file.list();

            for (File f : file.listFiles()) {

                try {
                    String code = FileUtils.readFileToString(f, "utf-8");
                    if (CheckUtils.isJson(code)){
                        JSONVul jsonVul = new ObjectMapper().readValue(code, JSONVul.class);
                        if (jsonVul!=null){
                            map.put(f.getName() , jsonVul.getMeta().getName());
                        }
                    }
                } catch (IOException e) {
                    continue;
                }

            }


        }
        return map;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "target",
        "meta",
        "request",
        "verify"
})
class JSONVul {

    @JsonProperty("target")
    private String target;
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("request")
    private Request request;
    @JsonProperty("verify")
    private Verify verify;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The target
     */
    @JsonProperty("target")
    public String getTarget() {
        return target;
    }

    /**
     *
     * @param target
     *     The target
     */
    @JsonProperty("target")
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     *
     * @return
     *     The meta
     */
    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @param meta
     *     The meta
     */
    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     *
     * @return
     *     The request
     */
    @JsonProperty("request")
    public Request getRequest() {
        return request;
    }

    /**
     *
     * @param request
     *     The request
     */
    @JsonProperty("request")
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     *
     * @return
     *     The verify
     */
    @JsonProperty("verify")
    public Verify getVerify() {
        return verify;
    }

    /**
     *
     * @param verify
     *     The verify
     */
    @JsonProperty("verify")
    public void setVerify(Verify verify) {
        this.verify = verify;
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
        return new HashCodeBuilder().append(target).append(meta).append(request).append(verify).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JSONVul) == false) {
            return false;
        }
        JSONVul rhs = ((JSONVul) other);
        return new EqualsBuilder().append(target, rhs.target).append(meta, rhs.meta).append(request, rhs.request).append(verify, rhs.verify).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "remarks",
        "level",
        "type",
        "author",
        "references"
})
class Meta {

    @JsonProperty("name")
    private String name;
    @JsonProperty("remarks")
    private String remarks;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("type")
    private String type;
    @JsonProperty("author")
    private String author;
    @JsonProperty("references")
    private References references;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The remarks
     */
    @JsonProperty("remarks")
    public String getRemarks() {
        return remarks;
    }

    /**
     *
     * @param remarks
     *     The remarks
     */
    @JsonProperty("remarks")
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     *
     * @return
     *     The level
     */
    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    /**
     *
     * @param level
     *     The level
     */
    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     *
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The author
     */
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     *     The author
     */
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     *     The references
     */
    @JsonProperty("references")
    public References getReferences() {
        return references;
    }

    /**
     *
     * @param references
     *     The references
     */
    @JsonProperty("references")
    public void setReferences(References references) {
        this.references = references;
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
        return new HashCodeBuilder().append(name).append(remarks).append(level).append(type).append(author).append(references).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return new EqualsBuilder().append(name, rhs.name).append(remarks, rhs.remarks).append(level, rhs.level).append(type, rhs.type).append(author, rhs.author).append(references, rhs.references).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "cve",
        "kpid"
})
class References {

    @JsonProperty("url")
    private String url;
    @JsonProperty("cve")
    private String cve;
    @JsonProperty("kpid")
    private String kpid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     *     The cve
     */
    @JsonProperty("cve")
    public String getCve() {
        return cve;
    }

    /**
     *
     * @param cve
     *     The cve
     */
    @JsonProperty("cve")
    public void setCve(String cve) {
        this.cve = cve;
    }

    /**
     *
     * @return
     *     The kpid
     */
    @JsonProperty("kpid")
    public String getKpid() {
        return kpid;
    }

    /**
     *
     * @param kpid
     *     The kpid
     */
    @JsonProperty("kpid")
    public void setKpid(String kpid) {
        this.kpid = kpid;
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
        return new HashCodeBuilder().append(url).append(cve).append(kpid).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof References) == false) {
            return false;
        }
        References rhs = ((References) other);
        return new EqualsBuilder().append(url, rhs.url).append(cve, rhs.cve).append(kpid, rhs.kpid).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "path",
        "postData"
})
class Request {

    @JsonProperty("path")
    private String path;
    @JsonProperty("postData")
    private String postData;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     *     The postData
     */
    @JsonProperty("postData")
    public String getPostData() {
        return postData;
    }

    /**
     *
     * @param postData
     *     The postData
     */
    @JsonProperty("postData")
    public void setPostData(String postData) {
        this.postData = postData;
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
        return new HashCodeBuilder().append(path).append(postData).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Request) == false) {
            return false;
        }
        Request rhs = ((Request) other);
        return new EqualsBuilder().append(path, rhs.path).append(postData, rhs.postData).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "match"
})
class Verify {

    @JsonProperty("type")
    private String type;
    @JsonProperty("match")
    private String match;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The match
     */
    @JsonProperty("match")
    public String getMatch() {
        return match;
    }

    /**
     *
     * @param match
     *     The match
     */
    @JsonProperty("match")
    public void setMatch(String match) {
        this.match = match;
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
        return new HashCodeBuilder().append(type).append(match).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Verify) == false) {
            return false;
        }
        Verify rhs = ((Verify) other);
        return new EqualsBuilder().append(type, rhs.type).append(match, rhs.match).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}