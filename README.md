
<div align="center">


![](docs/img/logo.png)

<br/>

![](https://img.shields.io/badge/KCon-%E5%85%B5%E5%99%A8%E8%B0%B1-red)
![](https://img.shields.io/badge/version-3.2.1-success)
![](https://img.shields.io/github/stars/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/forks/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/license/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/issues/iSafeBlue/Trackray.svg)    

<br/>

</div>


## ✨关于溯光

**溯光**，英文名“**TrackRay**”，意为逆光而行，追溯光源。同时致敬安全圈前辈开发的“溯雪”，“流光”。

溯光是一个开源渗透测试框架，框架自身实现了漏洞扫描功能，集成了知名安全工具：Metasploit、Nmap、Sqlmap、AWVS、Burpsuite等。

溯光使用 Java 编写，SpringBoot 作为基础框架，JPA + HSQLDB嵌入式数据库做持久化，Maven 管理依赖，Jython 管理 Python 插件，quartz 做任务调度，freemarker 做视图层，Websocket 实现命令行式插件交互。

如有任何使用上的问题请提交 issue。如果你具备插件开发的能力，希望你也能一起来维护这个项目。

项目开发不易，如果对你有帮助请下 star 以示鼓励。

## ⭐️特点 

- 使用只需要一个浏览器
- 集成 AWVS、SQLMap、NMap、Metasploit、Burpsuite、Crawlergo、Kunpeng、XRay 等安全工具
- 内置多个渗透测试辅助插件
- 方便、开源

## ❗️申明 

本项目禁止用于**商业用途**

溯光开发的初衷是方便安全研究者**检测漏洞以及教育学习使用**。

溯光**严格禁止一切通过本程序进行的违反任何国家法律行为**，请在合法范围内使用本程序。

我们不会上传未公开的漏洞插件，也不允许插件中存在破坏性的语句，目前module模块只写了几个有代表性的模块供开发者参考。

使用本程序则**默认视为**你**同意**我们的规则，请您务必遵守**道德与法律准则**。

如不遵守，**后果自负**，开发者将不承担任何责任！

## ⚡️快速使用 

#### docker启动
1. `git clone --depth=1 https://github.com/iSafeBlue/TrackRay.git` 或下载 [releases](https://github.com/iSafeBlue/TrackRay/releases) 最新版本源码
2. `docker build -t trackray_beta .` 构建镜像
3. `docker run -dit -p 80:8002 --name trackray_v3 trackray_beta` 启动容器，可根据需求自行设定参数
4. `docker exec -it trackray_v3 /bin/bash` 进入溯光工作目录
5. 启动有需要的服务，如 AWVS、msfprc、SQLMap 等。并根据自己的需求修改`application.properties`配置文件
6. 下载溯光依赖资源文件，将文件移动到 `/release/` 目录
7. 进入发布目录 `cd /root/trackray-framework/release/`，运行溯光`nohup java -jar trackray.jar &` 或 `java -jar trackray.jar`，若提示未找到文件请先执行一遍`mvn clean package`
8. 访问`http://127.0.0.1`

#### 本地启动
1. `git clone --depth=1 https://github.com/iSafeBlue/TrackRay.git` 或下载 [releases](https://github.com/iSafeBlue/TrackRay/releases) 最新版本源码
2. 启动有需要的服务，如AWVS、msfprc、SQLMap等。并根据自己的需求修改`application.properties`配置文件或直接通过启动参数配置
3. 下载溯光依赖资源文件，将文件移动到 `/release/` 目录
4. 执行 Maven 编译命令 `mvn clean package` 
5. 进入发布目录 `cd release/`，运行溯光`nohup java -jar trackray.jar &` 或 `java -jar trackray.jar`
6. 访问`http://127.0.0.1`


#### 溯光依赖资源

> 链接: https://pan.baidu.com/s/1lVAcB1r4gLJxzYXWtSs5xg 提取码: eexr

下载资源文件解压后将对应版本的“resource”目录移动到“release”目录下。

## ❤️注意

- 提问前请阅读一遍[《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way/blob/master/README-zh_CN.md)。
- **本项目未做安全防护**，部分代码会存在安全漏洞。
- **MSF控制台**和**交互式插件控制台**，尽量使用 Firefox 浏览器访问。
- 开发插件建议使用 `Intellij IDEA`，需要安装 lombok 插件。
- 登录密码在 `application.properties` 中修改 `trackray.account` 和 `trackray.password`。


## ✏️ChangeLog 

| 日期 | 描述  |
| ---- | ---- |
| 2020-04-11 | 溯光3.2.1更新 |
| 2020-04-11 | 溯光3.2.0更新 |
| 2019-08-16 | 溯光3.1.0更新 |
| 2019-05-14 | 溯光3更新 |
| 2019-03-11 | 新增jython支持，可通过`PyScript.java`插件调用python脚本 |
| 2019-02-02 | 修复在linux环境下相关bug |
| 2019-01-30 | 溯光2更新 |
| 2018-10-29 | 溯光渗透测试框架开源 |


## 😎贡献者

项目由[浅蓝](https://github.com/iSafeBlue)发起并主导开发。

[致谢名单](https://github.com/iSafeBlue/TrackRay/wiki/%E8%87%B4%E8%B0%A2%E5%90%8D%E5%8D%95)


## 🚀功能展示 

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

## 💴赞助

**您的捐助将被用于**

* 持续开发溯光渗透测试框架
* 社区活动
* 奖励杰出贡献者

![微信](docs/img/wx.png) ![支付宝](docs/img/ali.png)


## ✉️交流反馈

Email: blue#ixsec.org

> 交流群已满员，请添加作者微信邀请入群（备注溯光）。

![][10]

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
    
