package com.trackray.base.configuration;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.controller.DispatchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/7 20:16
 */
//@Configuration
@Order(HIGHEST_PRECEDENCE)
public class ExtensionsConfiguration {

    @Value("${extensions.path}")
    public String extensions;

    private static URLClassLoader loader = null;

    public static final Logger log = LoggerFactory.getLogger(ExtensionsConfiguration.class);

    @Autowired
    private DispatchController dispatchController;

    @Bean
    public void initClassloader(){



        File file = new File(extensions);
        if (file.exists() && file.isDirectory() && file.list().length>0){
            if(loader == null){

                String fileNames[] = file.list();
                if(fileNames != null && fileNames.length > 0){
                    URL urls[] = new URL[fileNames.length];
                    for(int i = 0;i < fileNames.length;i++){
                        try {
                            urls[i] = new URL(file.toURL() +"/"+fileNames[i]);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException("加载lib目录下jar文件出错！",e);
                        }
                    }
                    loader = new URLClassLoader(urls);
                }
            }
        }

    }


    @Bean
    public void loadExtensions(){
        if (loader==null)
            return;
        log.info("开始加载扩展插件库，当前扩展目录为["+extensions+"]");
        int count = 0;
        for (URL url : loader.getURLs()) {

            String path = url.getPath();
            try {
                JarFile jarFile = new JarFile(path);
                Enumeration<JarEntry> en = jarFile.entries();
                while (en.hasMoreElements()) {
                    JarEntry je = en.nextElement();
                    String name = je.getName();
                    if (name.endsWith(".class")) {
                        String className = name.replace(".class", "").replaceAll("/", ".");
                        try {
                            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url},
                                    Thread.currentThread().getContextClassLoader());
                            Class aClass = Class.forName(className, true, urlClassLoader);
                            if (aClass.getAnnotation(Plugin.class)!=null){
                                ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) dispatchController.getAppContext();

                                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);

                                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

                                BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

                                beanFactory.registerBeanDefinition(((Plugin) aClass.getAnnotation(Plugin.class)).value(), beanDefinition);

                                count++;
                                log.info("扩展[" + jarFile.getName() +"] Plugin ["+((Plugin) aClass.getAnnotation(Plugin.class)).title()+"] 注册成功");
                            }else if (aClass.getAnnotation(Component.class)!=null){
                                ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) dispatchController.getAppContext();

                                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);

                                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

                                BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
                                beanFactory.registerBeanDefinition(((Component) aClass.getAnnotation(Component.class)).value(), beanDefinition);
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                }


                } catch (IOException e) {
                e.printStackTrace();
            }

        }

        log.info("扩展插件库加载完成，共加载["+count+"]个插件");

    }


}
