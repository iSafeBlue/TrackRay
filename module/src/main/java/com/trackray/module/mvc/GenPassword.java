package com.trackray.module.mvc;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.module.inner.FuckPassword;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/9 15:05
 */
@Rule(
        params = {
                @Param(key = "namejp" , desc = "姓名简拼" ,defaultValue = "" ),
                @Param(key = "nameqp" , desc = "姓名全拼" ,defaultValue = "" ),
                @Param(key = "english" , desc = "英文ID" ,defaultValue = "" ),
                @Param(key = "username" , desc = "常用用户名" ,defaultValue = "" ),
                @Param(key = "tel" , desc = "手机号" ,defaultValue = "" ),
                @Param(key = "email" , desc = "邮箱前缀" ,defaultValue = "" ),
                @Param(key = "number" , desc = "常用数字" ,defaultValue = "" ),
                @Param(key = "qq" , desc = "qq号" ,defaultValue = "" ),
                @Param(key = "friendjp" , desc = "伴侣姓名简拼" ,defaultValue = "" ),
                @Param(key = "friendqp" , desc = "伴侣姓名全拼" ,defaultValue = "" ),
                @Param(key = "birthday" , desc = "生日(yyyyMMdd)" ,defaultValue = "" ),
        },
        type = CommonPlugin.Type.TEXT
)
@Plugin(title = "弱密码生成" , author = "浅蓝")
public class GenPassword extends CommonPlugin<String> {
    @Override
    public boolean check(Map param) {
        return true;
    }
    @Autowired
    private FuckPassword fuckPassword;

    @Override
    public String start() {
        fuckPassword.setParam(this.param);
        Set<String> result = fuckPassword.executor().result();
        StringBuffer buff = new StringBuffer();
        for (String s : result) {
            buff.append(s).append("\n");
        }
        return buff.toString();
    }
}
