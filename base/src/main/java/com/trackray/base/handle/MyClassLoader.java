package com.trackray.base.handle;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/7 22:03
 */
public class MyClassLoader extends URLClassLoader {
    public MyClassLoader(){
        super(new URL[0]);
    }

    public MyClassLoader(URL[] urls){
        super(urls);
    }

    public void addJar(URL url){
        this.addURL(url);
    }

    public void loadJar(String strUrl){
        try{
            URL url = new URL(strUrl);
            this.addURL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<?> findClass(String name) throws ClassNotFoundException{
        return super.findClass(name);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException{
        return super.loadClass(name);
    }
}