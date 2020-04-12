package com.trackray;

import com.trackray.base.configuration.BurpSuiteConfiguration;
import com.trackray.base.configuration.TrackrayConfiguration;
import com.trackray.base.utils.PropertyUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.loader.LaunchedURLClassLoader;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.ExplodedArchive;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class WebApplication {


	public static void main(String[] args) {

		ApplicationArguments applicationArguments = new DefaultApplicationArguments(
				args);


		String releaseDir = PropertyUtil.getProperty("trackray.release.dir");
		String root = System.getProperty("user.dir");

		if (!root.contains(releaseDir)){
			System.err.println("请检查工作目录是否在配置文件指定的 "+releaseDir+" 发布目录");
			System.exit(0);
		}

		if (applicationArguments.containsOption("help")){
			help(applicationArguments);
		}else{
			run(args,applicationArguments);
		}

	}

	private static void run(String[] args, ApplicationArguments applicationArguments) {

		if (!applicationArguments.getNonOptionArgs().isEmpty()){
			List<String> funcArgs = applicationArguments.getNonOptionArgs();
			for (String f : TrackrayConfiguration.Function.names()) {
				if (funcArgs.contains(f)){
					//TrackrayConfiguration.sysVariable.put(f,true);
					TrackrayConfiguration.Function.valueOf(f).setEnable(true);
				}else{
					//TrackrayConfiguration.sysVariable.put(f,false);
					TrackrayConfiguration.Function.valueOf(f).setEnable(false);
				}
			}
		}


		if (!applicationArguments.getOptionNames().isEmpty()){

			if (applicationArguments.containsOption("burp.local")){
				BurpSuiteConfiguration.mode = BurpSuiteConfiguration.Mode.LOCAL;
			}else if (applicationArguments.containsOption("burp.remote")){
				BurpSuiteConfiguration.mode = BurpSuiteConfiguration.Mode.REMOTE;
			}
		}

		loadPlugins(applicationArguments);

		SpringApplication.run(WebApplication.class, args);

	}

	private static void loadPlugins(ApplicationArguments applicationArguments){
		String pluginDir = null;
		if (applicationArguments.containsOption("plugin")){
			List<String> plugin = applicationArguments.getOptionValues("plugin");
			pluginDir = (plugin.get(0));
		}else{
			pluginDir = ("resources"+File.separator+"plugin");
		}

		String root = System.getProperty("user.dir");

		List<Archive> archives = new ArrayList<>();

		File resourcesDir = new File(root, pluginDir);

		if (new File(pluginDir).exists()&&new File(pluginDir).isDirectory()){
			resourcesDir = new File(pluginDir);
		}

		System.out.println("当前插件加载目录:"+resourcesDir.getAbsolutePath());

		if (resourcesDir.exists()){
			for (File file : resourcesDir.listFiles()) {
				if (file.getName().toLowerCase().endsWith("jar")){
					try {
						System.out.println("已加载插件:"+file.getName());
						JarFileArchive entries = new JarFileArchive(file);
						archives.add(entries);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}


		List<URL> urls = new ArrayList(archives.size());

		for (Archive archive : archives) {
			try {
				urls.add(archive.getUrl());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		URL[] urlarr = urls.toArray(new URL[0]);

		LaunchedURLClassLoader launchedURLClassLoader = new LaunchedURLClassLoader(urlarr, Thread.currentThread().getContextClassLoader());

		Thread.currentThread().setContextClassLoader(launchedURLClassLoader);


	}

	private static void help(ApplicationArguments applicationArguments) {
		System.out.println(" _______             _    _____\n" +
				"|__   __|           | |  |  __ \\\n" +
				"   | |_ __ __ _  ___| | _| |__) |__ _ _   _\n" +
				"   | | '__/ _` |/ __| |/ /  _  // _` | | | |\n" +
				"   | | | | (_| | (__|    | | \\ \\ (_| | |_| |\n" +
				"   |_|_|  \\__,_|\\___|_|\\_\\_|  \\_\\__,_|\\__, |\n" +
				"                                       __/ |\n" +
				"                                      |___/     ");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java -jar trackray.jar [functions] [options]");
		System.out.println("  java -jar trackray.jar ");
		System.out.println("  java -jar trackray.jar none");
		System.out.println("  java -jar trackray.jar --plugin=resource/plugin");
		System.out.println("  java -jar trackray.jar sqlmap --sqlmap.host=http://127.0.0.1:8887 burpsuite --burp.local --burp.local.loader=true --burp.local.headless=true");;
		System.out.println();
		System.out.println("Tips:");
		System.out.println("  命令参数均可在application.properties文件中配置，如不指定则默认从配置文件取值");
		System.out.println("  带有\"*\"符号的参数需手动设置，不可在配置文件设置");
		System.out.println();
		System.out.println("Functions: [burpsuite,awvs,msf,sqlmap,xray,none]");
		System.out.println();
		System.out.println("Options:");
		System.out.println("  --help: 查看帮助提示");
		System.out.println("  --plugin: 指定插件加载目录，默认/resources/plugin/");
		System.out.println("  --server.port <1-65535>: 溯光WEB服务端口");
		System.out.println("  [burpsuite]: remote/local模式只可选其一，默认local");
		System.out.println("  --burp.console.log <boolean>: 是否开启控制台日志输出，默认不输出详细日志");
		System.out.println("  --burp.remote*: 启用远程模式，使用此参数会调用远程主机接口操作burpsuite");
		System.out.println("  --burp.remote.host <string>: burpsuite远程接口主机地址");
		System.out.println("  --burp.remote.port <int>: burpsuite远程接口主机端口");
		System.out.println("  --burp.local*: 启用本地模式，使用此参数会在本地启动burpsuite进程");
		System.out.println("  --burp.local.loader <boolean>: 是否使用burpsuite加载器");
		System.out.println("  --burp.local.headless <boolean>: 是否使用无头模式加载");
		System.out.println();
		System.out.println("  [awvs]");
		System.out.println("  --awvs.host <string>: AWVS接口URL");
		System.out.println("  --awvs.key <string>: AWVS接口TOKEN[X-Auth]");
		System.out.println();
		System.out.println("  [msf]");
		System.out.println("  --metasploit.host <string>: msf远程主机地址");
		System.out.println("  --metasploit.user <string>: msf远程主机用户名");
		System.out.println("  --metasploit.pass <string>: msf远程主机密码");
		System.out.println();
		System.out.println("  [sqlmap]");
		System.out.println("  --sqlmap.host <string>: sqlmap接口url");
		System.out.println();
		System.out.println("  [xray]");
		System.out.println("  --xray.console.log <boolean>: 是否开启控制台日志输出，默认不输出日志");
		System.out.println("  --xray.remote.host <string>: xray远程主机地址");
		System.out.println("  --xray.remote.port <int>: xray远程主机端口");
		System.exit(0);


	}

	protected static Archive createArchive(Class clazz) throws Exception {
		ProtectionDomain protectionDomain = clazz.getProtectionDomain();
		CodeSource codeSource = protectionDomain.getCodeSource();
		URI location = codeSource != null ? codeSource.getLocation().toURI() : null;
		String path = location != null ? location.getSchemeSpecificPart() : null;
		if (path == null) {
			throw new IllegalStateException("Unable to determine code source archive");
		} else {
			File root = new File(path);
			if (!root.exists()) {
				throw new IllegalStateException("Unable to determine code source archive from " + root);
			} else {
				return (Archive)(root.isDirectory() ? new ExplodedArchive(root) : new JarFileArchive(root));
			}
		}
	}

	protected static boolean isNestedArchive(Archive.Entry entry) {
		return entry.isDirectory() ? entry.getName().equals("BOOT-INF/classes/") : entry.getName().startsWith("BOOT-INF/lib/");
	}

}

