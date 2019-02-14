
<pre>
 _______             _    _____             
|__   __|           | |  |  __ \            
   | |_ __ __ _  ___| | _| |__) |__ _ _   _ 
   | | '__/ _` |/ __| |/ /  _  // _` | | | |
   | | | | (_| | (__|    | | \ \ (_| | |_| |
   |_|_|  \__,_|\___|_|\_\_|  \_\__,_|\__, |
                                       __/ |
                                      |___/   v2.0
</pre>
![](https://img.shields.io/github/stars/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/forks/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/license/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/issues/iSafeBlue/Trackray.svg)

## 关于溯光

溯光是一个由Java语言编写的服务式插件化渗透测试框架，项目是一个WEB服务，提供了调用插件和扫描的接口，并且使用了 Websocket 技术实现命令行风格交互，启动后只需要通过浏览器即可访问，是其他开源渗透测试框架不具备的特点。

自更新 2.0 版本后，项目数据库采用嵌入式数据库hsqldb，使用 SpringBoot 框架开发，用maven管理依赖，使开发和使用更为简单方便。

如有任何使用上的问题可发送邮件或联系我的私人微信，BUG请提交issue。

如果你具备插件开发的能力，希望你也能一起来维护这个项目。

项目开发不易，你的 star 就是我们坚持的动力。

[官方网站](https://trackray.cn)

## 特点

- 提供WEB服务接口
- 使用只需要一个浏览器
- 第一个使用Java集成MSF & AWVS的开源项目
- 内置插件式扫描器

## 如何使用
* 安装nmap，写入环境变量
* 启动SQLMAP API
* 启动msfrpc服务 ```msfrpcd -U msf -P msf -S -f```
* 启动AWVS
* 在application.properties配置文件中修改 ```database.dir```及以上有变更的配置参数
* 编译并运行SpringBoot启动类WebApplication.java

[详细教程](https://github.com/iSafeBlue/TrackRay/wiki/%E5%AE%89%E8%A3%85%E8%AF%B4%E6%98%8E)



## 功能展示

#### 交互式插件控制台
![](img/netcat.gif)

#### MSF 控制台
![](img/msf.gif)

#### 无交互插件调用
![Image text](img/5.png)
![Image text](img/2.gif)

#### 扫描器展示
![](img/6.gif)


## 插件

[plugin.md](plugin.md)


## 功能简介

[functions.md](functions.md)


## 贡献者
项目由[浅蓝](https://github.com/iSafeBlue)发起并主导开发。

[致谢名单](https://github.com/iSafeBlue/TrackRay/wiki/%E8%87%B4%E8%B0%A2%E5%90%8D%E5%8D%95)


## 赞助

#### 您的捐助将被用于

* 持续开发溯光渗透测试框架
* ```trackray.cn```  域名续费
* 社区活动
* 奖励杰出贡献者

![微信](img/wx.png) ![支付宝](img/ali.png)

## 申明

溯光遵循 GPL 开源协议，请务必了解。

溯光开发的初衷是方便企业的安全研究者检测漏洞以及教育学习使用。

我们严格禁止一切通过本程序进行的违反任何国家法律行为，请在合法范围内使用本程序。

我们不会上传未公开的漏洞插件，也不允许插件中存在破坏性的语句，目前module模块只写了几个有代表性的模块供开发者参考。

使用本程序则默认视为你同意我们的规则，请您务必遵守道德与法律准则。

如不遵守，后果自负，开发者将不承担任何责任！



## 联系作者

email: blue#ixsec.org
