package com.trackray.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/6 18:10
 */
public class ServletUtils {

    public static Object getSession(HttpServletRequest req , String p){
        HttpSession session = req.getSession();
        return session.getAttribute(p);
    }

    public static boolean containSession(HttpServletRequest req , String p){
        return getSession(req,p)!=null;
    }

    public static boolean isLogged(HttpServletRequest req){
        return containSession(req,"user");
    }
}
