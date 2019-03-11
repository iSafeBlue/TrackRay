package com.trackray.base.bean;

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
    private Map<String,String> childDomain = new HashMap<>();
    //域名持有者的其他网站
    private List<String> holdSite = new ArrayList<String>();
    //同服务器的其他网站
    private List<String> serverSite = new ArrayList<String>();
    //备案者所持有的其他域名 <Domain , Ttile>
    private Map<String,String> icpSite = new HashMap<>();


}
