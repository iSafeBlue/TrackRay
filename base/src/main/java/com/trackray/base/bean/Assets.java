package com.trackray.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class Assets {
    //子域名  <Domain , IP>
    @JSONField(name="子域名")
    private Map<String,String> childDomain = new HashMap<>();
    //域名持有者的其他网站
    @JSONField(name="注册者持有域名")
    private List<String> holdSite = new ArrayList<String>();
    //同服务器的其他网站
    @JSONField(name="同服务器域名")
    private List<String> serverSite = new ArrayList<String>();
    //备案者所持有的其他域名 <Domain , Ttile>
    @JSONField(name="备案者持有域名")
    private Map<String,String> icpSite = new HashMap<>();


}
