
<div align="center">


![](docs/img/logo.png)

<br/>

![](https://img.shields.io/badge/KCon-%E5%85%B5%E5%99%A8%E8%B0%B1-red)
![](https://img.shields.io/badge/version-3.1.0-success)
![](https://img.shields.io/github/stars/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/forks/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/license/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/issues/iSafeBlue/Trackray.svg)    

<br/>

[TRACKRAY.CN](https://trackray.cn)

</div>


## 关于溯光 :sparkles:

**溯光**，英文名“**TrackRay**”，意为逆光而行，追溯光源。同时致敬安全圈前辈开发的“溯雪”，“流光”。

溯光是一个开源的插件化渗透测试框架，框架自身实现了漏洞扫描功能，集成了知名安全工具：Metasploit、Nmap、Sqlmap、AWVS等。

溯光使用 Java 编写，SpringBoot 作为基础框架，JPA + HSQLDB嵌入式数据库做持久化，Maven 管理依赖，Jython 实现 Python 插件调用，quartz 做任务调度，freemarker 做视图层，Websocket 实现命令行式插件交互。

框架可扩展性高，支持 Java、Python、JSON 等方式编写插件，有“漏洞扫描插件”、“爬虫插件”、“独立应用插件”、“内部插件”、“无交互插件”和“可交互插件” 等插件类型。

如有任何使用上的问题请提交 issue。

如果你具备插件开发的能力，希望你也能一起来维护这个项目。

项目开发不易，如果可以的话请留下你的 star 表示对我的鼓励。

## 特点 :star:

- 提供 WEB 服务接口
- 使用只需要一个浏览器
- 集成 AWVS、SQLMap、NMap、Metasploit、Kunpeng、XRay 等安全工具
- 内置漏洞扫描器
- 强大、易用、方便、开源

## 申明 :exclamation:

本项目禁止用于**商业用途**

溯光开发的初衷是方便安全研究者**检测漏洞以及教育学习使用**。

溯光**严格禁止一切通过本程序进行的违反任何国家法律行为**，请在合法范围内使用本程序。

我们不会上传未公开的漏洞插件，也不允许插件中存在破坏性的语句，目前module模块只写了几个有代表性的模块供开发者参考。

使用本程序则**默认视为**你**同意**我们的规则，请您务必遵守**道德与法律准则**。

如不遵守，**后果自负**，开发者将不承担任何责任！

## 快速使用 :zap:

可以通过源码编译为jar包后运行，或直接运行已编译好的jar包

#### 源代码版本
1. 在 [releases](https://github.com/iSafeBlue/TrackRay/releases) 下载`trackray-x.x.x.src.zip`解压
2. 如已安装 maven、jdk 等溯光编译所必备环境可忽略第2-5条
3. `docker build -t trackray_beta .` 构建镜像
4. `docker run -dit -p 8080:8080 --name trackray_runtime t trackray_beta` 启动容器，可根据需求自行设定参数
5. `docker exec -it trackray_runtime /bin/bash` 进入溯光工作目录
6. 启动有需要的服务，如AWVS、msfprc、SQLMap等。并根据自己的需求修改`application.properties`配置文件
7. 执行`nohup java -jar trackray.jar &` 或 `java -jar trackray.jar`，若提示未找到文件请先执行一遍`mvn clean package`
8. 访问`http://127.0.0.1:8080`

#### 已编译版本
1. 在 [releases](https://github.com/iSafeBlue/TrackRay/releases) 下载`trackray-x.x.x.bin.zip`解压
2. 启动有需要的服务，如AWVS、msfprc、SQLMap等。并根据自己的需求修改`application.properties`配置文件
3. 执行`nohup java -jar trackray.jar &` 或 `java -jar trackray.jar`
4. 访问`http://127.0.0.1:8080`

## 功能展示 :fire:


#### 登录

![][2]


#### 任务列表

![][4]

#### 任务详情

![1557573022.jpg][6]

#### 无交互接口插件

![][7]

#### 部分独立插件示例

![][11]

![][12]

![][13]

![][14]

#### 交互式插件控制台

![][9]

#### MSF 控制台

![](/docs/img/msf.gif)


## 文档 :blue_book:

[安装说明](/docs/安装说明.md)

[插件开发](/docs/扩展开发.md)

[功能介绍](/docs/功能.md)

## 注意 :heart:

- 提问前请阅读一遍[《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way/blob/master/README-zh_CN.md)。
- **本项目未做安全防护**，部分代码会存在安全漏洞。
- **MSF控制台**和**交互式插件控制台**，尽量使用 Firefox 浏览器访问。
- 开发插件建议使用 `Intellij IDEA`，需要安装 lombok 插件。
- 登录密码在 `application.properties` 中修改 `trackray.account` 和 `trackray.password`。


## ChangeLog :pencil2:

| 日期 | 描述  |
| ---- | ---- |
| 2019-08-16 | 溯光3.1.0更新 |
| 2019-05-14 | 溯光3更新 |
| 2019-03-11 | 新增jython支持，可通过`PyScript.java`插件调用python脚本 |
| 2019-02-02 | 修复在linux环境下相关bug |
| 2019-01-30 | 溯光2更新 |
| 2018-10-29 | 溯光渗透测试框架开源 |


## 贡献者 :sunglasses:

项目由[浅蓝](https://github.com/iSafeBlue)发起并主导开发。

[致谢名单](https://github.com/iSafeBlue/TrackRay/wiki/%E8%87%B4%E8%B0%A2%E5%90%8D%E5%8D%95)


## 赞助 :yen:

**您的捐助将被用于**

* 持续开发溯光渗透测试框架
* ```trackray.cn```  域名续费
* 社区活动
* 奖励杰出贡献者

![微信](docs/img/wx.png) ![支付宝](docs/img/ali.png)


## 溯光交流群 :flags:

> 交流群已满员，请添加作者微信邀请入群（备注溯光）。

![][10]

## 联系作者 :email:

Email: blue#ixsec.org

  [1]: /docs/img/3999579642.png
  [2]: /docs/img/242398485.png
  [3]: /docs/img/4052103405.png
  [4]: /docs/img/3017849620.png
  [5]: /docs/img/4059228044.png
  [6]: /docs/img/4094571871.png
  [7]: /docs/img/1587610634.png
  [9]: /docs/img/2882579563.gif
  [10]: /docs/img/wechat.png
  [11]: /docs/img/1565838022.jpg
  [12]: /docs/img/1565838023.png
  [13]: /docs/img/1565838152.jpg
  [14]: /docs/img/1565924910.png
    