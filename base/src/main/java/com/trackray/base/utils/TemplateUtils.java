package com.trackray.base.utils;

import com.trackray.base.bean.Vulnerable;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/7 17:14
 */
public class TemplateUtils {

    public static String getVulnNameByType(Object id){
        if (id==null)
            return Vulnerable.Type.UNKNOWN.getName();
        Vulnerable.Type[] values = Vulnerable.Type.values();
        for (Vulnerable.Type value : values) {
            if (Integer.parseInt(id.toString()) == value.getType())
                return value.getName();
        }
        return Vulnerable.Type.UNKNOWN.getName();
    }

}
