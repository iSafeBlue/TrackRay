package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/9 14:15
 */
@Plugin(value = "fuckPassword",title = "弱口令生成内部插件" ,author = "浅蓝")
public class FuckPassword extends InnerPlugin<Set<String>> {
    public String filename = BASE + "/fuckPassword/template.txt";

    @Override
    public boolean check(Map<String, Object> param) {
        return new File(filename).exists();
    }
    private String templates;
    @Override
    public void before() {
        try {
            templates = FileUtils.readFileToString(new File(filename));
        } catch (IOException e) {
            templates = "";
            return;
        }

        String namejp = param.getOrDefault("namejp","").toString();
        String nameqp = param.getOrDefault("nameqp","").toString();
        String english = param.getOrDefault("english","").toString();
        String username = param.getOrDefault("username","").toString();
        String tel = param.getOrDefault("tel","").toString();
        String email = param.getOrDefault("email","").toString();
        String number = param.getOrDefault("number","").toString();
        String qq = param.getOrDefault("qq","").toString();
        String friendjp = param.getOrDefault("friendjp","").toString();
        String friendqp = param.getOrDefault("friendqp","").toString();
        String birthday = param.getOrDefault("birthday","").toString();

        birthday = birthday.length()!=8?"19990101":birthday;

        String year = birthday.substring(0,4);
        String year_2 = birthday.substring(2,4);
        String moon = birthday.substring(4,6);
        String day = birthday.substring(6,8);

        templates = templates.replaceAll("\\{NAME_JP\\}",namejp).
                replaceAll("\\{NAME_QP\\}",nameqp).
                replaceAll("\\{ENGLISH\\}",english).
                replaceAll("\\{USERNAME\\}",username).
                replaceAll("\\{TEL\\}" , tel).
                replaceAll("\\{EMAIL\\}",email).
                replaceAll("\\{NUMBER\\}" , number).
                replaceAll("\\{QQ\\}" , qq).
                replaceAll("\\{YEAR\\}",year).
                replaceAll("\\{YEAR_2\\}",year_2).
                replaceAll("\\{MOON\\}" , moon).
                replaceAll("\\{DAY\\}" , day).
                replaceAll("\\{FRIEND_JP\\}" , friendjp).
                replaceAll("\\{FRIEND_QP\\}" , friendqp);


    }
    private Set<String> pwds = new HashSet<>();
    @Override
    public void process() {
        if (templates.isEmpty())
            return;
        for (String s : templates.split("\r\n")) {
            pwds.add(s);
        }
    }

    @Override
    public void after() {
        result = pwds;
    }
}
