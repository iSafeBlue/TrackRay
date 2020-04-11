package com.trackray.base.bean;

import lombok.Data;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class Rule {

    public boolean crawler = false; //爬虫
    public boolean crawlergo = false;//crawlergo爬虫
    public boolean sense = false;   //信息泄露
    public boolean port = false;    //端口
    public boolean finger = false;  //指纹识别
    public boolean childdomain = false; //资产
    public boolean fuzzdir = false;     //目录扫描
    public boolean attack = false;      //插件扫描
    public boolean thorough = false;    //深度扫描 （awvs/nessus)
}
