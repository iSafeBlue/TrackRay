
<pre>
 _______             _    _____             
|__   __|           | |  |  __ \            
   | |_ __ __ _  ___| | _| |__) |__ _ _   _ 
   | | '__/ _` |/ __| |/ /  _  // _` | | | |
   | | | | (_| | (__|    | | \ \ (_| | |_| |
   |_|_|  \__,_|\___|_|\_\_|  \_\__,_|\__, |
                                       __/ |
                                      |___/ 
</pre>

```
欢迎各位提交插件
```
## 介绍
做这个初衷只是为了实现自己在朋友圈立下的Flag，供自己在渗透测试过程中提升工作效率，方便于记录与分析。该框架使用Java语言编写，数据库使用MySQL。项目为接口式服务，可以通过调用WEB接口或Websocket等方式完成模块调用，此为最初版本。
该项目分为三个模块base、rest、module。rest与module需继承自base。



### 如何使用
* 运行SQL文件并修改数据库配置
* 安装NMAP
* 启动SQLMAP API
* 修改config.properties中的NMAP路径、sqlmapAPI的URL、本项目的地址、CENSYS的secret
* 编译项目并运行

### 插件
插件分为"基于爬虫的插件"与"普通插件"。
基于爬虫的插件在扫描任务时的爬虫阶段执行，需继承CrawlerPlugin类，并使用@Plugin注解，具体可参考：SQLinjectAndXss类

普通插件可以通过Websocket界面或直接调用http接口的方式执行，分别需要继承WebSocketPlugin和CommonPlugin类，并使用@Plugin与@Rule注解，具体可参考：NetCat与ECShop001类

### EXP
exploit在任务基本信息探测完成后执行，用于对已知漏洞完成自动化攻击，需继承AbstractExploit类，并使用@Exploit与@Rule注解，具体可参考：DedecmsSQLi类


### 截图
![Image text](https://github.com/iSafeBlue/Trackray/blob/master/demo/4.png)

![Image text](https://github.com/iSafeBlue/Trackray/blob/master/demo/5.png)

![Image text](https://github.com/iSafeBlue/Trackray/blob/master/demo/1.gif)

![Image text](https://github.com/iSafeBlue/Trackray/blob/master/demo/2.gif)

![Image text](https://github.com/iSafeBlue/Trackray/blob/master/demo/3.gif)

## 功能
### 基本检测
* 扫描端口
* 扫描同服网站
* 扫描子域名
* 扫描C段
* 网页爬虫

### 指纹识别
* 识别WAF
* 识别CDN
* 识别CMS&框架&系统
* 识别语言

### 暴力破解(不支持初版)
* 暴力破解(21,22等)根据端口识别结果
* 扫描敏感文件(根据WEB端口&子域)

### 信息收集
* EMAIL & 手机号
* WHOIS信息
* 域名历史解析IP
* 域名在FOFA、zoomeye等平台上的结果
* 域名注册者邮箱
* 域名注册者注册的其他域名

### 攻击检测
* SQL注入检测（基于爬虫）
* XSS检测（基于爬虫）
* exploit

## 贡献者
项目由[浅蓝](https://github.com/iSafeBlue)发起并主导
核心开发者：
* [浅蓝](https://github.com/iSafeBlue)
* ...

## 写在最后
如果对你有一定帮助可以star、follow一下，感谢支持。
在使用上遇到问题可发送邮件到blue@ixsec.org。

本项目只可用于安全研究，禁止进行未授权攻击行为。

