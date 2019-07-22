
<div align="center">


![](docs/img/logo.png)

<br/>

![](https://img.shields.io/github/stars/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/forks/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/license/iSafeBlue/Trackray.svg)
![](https://img.shields.io/github/issues/iSafeBlue/Trackray.svg)    

<br/>

[TRACKRAY.CN](https://trackray.cn)

</div>


## 关于溯光

**溯光**，英文名“**TrackRay**”，意为逆光而行，追溯光源。同时致敬安全圈前辈开发的“溯雪”，“流光”。

溯光是一个开源的插件化渗透测试框架，框架自身实现了漏洞扫描功能，集成了知名安全工具：Metasploit、Nmap、Sqlmap、AWVS等。

溯光使用 Java 编写，SpringBoot 作为基础框架，JPA + HSQLDB嵌入式数据库做持久化，Maven 管理依赖，Jython 实现 Python 插件调用，quartz 做任务调度，freemarker 做视图层，Websocket 实现命令行式插件交互。

框架可扩展性高，支持 Java、Python、JSON 等方式编写插件，有“漏洞扫描插件”、“爬虫插件”、“MVC插件”、“内部插件”、“无交互插件”和“可交互插件” 等插件类型。

如有任何使用上的问题请提交 issue。

如果你具备插件开发的能力，希望你也能一起来维护这个项目。

项目开发不易，如果可以的话请留下你的 star 表示对我的鼓励。

## 特点

- 提供 WEB 服务接口
- 使用只需要一个浏览器
- 集成知名安全工具
- 内置漏洞扫描器
- 强大、易用、方便、开源



## 功能展示


#### 主页

![][1]

#### 登录

![][2]

#### 任务创建

![][3]

#### 任务列表

![][4]

#### 任务详情


![1557573022.jpg][6]

#### 无交互接口插件调用

![][7]

#### MVC插件示例

![1557573113(1).jpg][8]

#### 交互式插件控制台

![05.gif][9]

#### MSF 控制台

![](/docs/img/msf.gif)


## 文档

[安装说明](/docs/安装说明.md)

[插件开发](/docs/扩展开发.md)

[功能介绍](/docs/功能.md)

## 注意
- 提问前请 **务必!务必!务必!** 阅读一遍[《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way/blob/master/README-zh_CN.md)。
- **本项目未做安全防护**，部分代码会存在安全漏洞。
- **MSF控制台**和**交互式插件控制台**，尽量使用 Firefox 浏览器访问。
- 开发插件建议使用 `Intellij IDEA`，需要安装 lombok 插件。
- 登录密码在 `application.properties` 中修改 `trackray.account` 和 `trackray.password`。
- 如果没有修改 maven 为国内源，在通过`package.sh/bat`编译溯光下载依赖时会很慢，**建议修改为国内的阿里云仓库**。


## ChangeLog
.

| 日期 | 描述  |
| ---- | ---- |
| 2019-05-14 | 溯光3更新 |
| 2019-03-11 | 新增jython支持，可通过`PyScript.java`插件调用python脚本 |
| 2019-02-02 | 修复在linux环境下相关bug |
| 2019-01-30 | 溯光2更新 |
| 2018-10-29 | 溯光渗透测试框架开源 |


## 贡献者

项目由[浅蓝](https://github.com/iSafeBlue)发起并主导开发。

[致谢名单](https://github.com/iSafeBlue/TrackRay/wiki/%E8%87%B4%E8%B0%A2%E5%90%8D%E5%8D%95)


## 赞助

#### 您的捐助将被用于

* 持续开发溯光渗透测试框架
* ```trackray.cn```  域名续费
* 社区活动
* 奖励杰出贡献者

![微信](docs/img/wx.png) ![支付宝](docs/img/ali.png)

## 申明

溯光遵循 **GPL 开源协议**，请务必了解。

本项目禁止用于**商业用途**

溯光开发的初衷是方便企业的安全研究者**检测漏洞以及教育学习使用**。

我们**严格禁止一切通过本程序进行的违反任何国家法律行为**，请在合法范围内使用本程序。

我们不会上传未公开的漏洞插件，也不允许插件中存在破坏性的语句，目前module模块只写了几个有代表性的模块供开发者参考。

使用本程序则**默认视为**你**同意**我们的规则，请您务必遵守**道德与法律准则**。

如不遵守，**后果自负**，开发者将不承担任何责任！

## 溯光交流群


> 交流群已满员，请添加作者微信(Xsec_Te4m)邀请入群。

## 联系作者

Email: blue#ixsec.org

  [1]: /docs/img/3999579642.png
  [2]: /docs/img/242398485.png
  [3]: /docs/img/4052103405.png
  [4]: /docs/img/3017849620.png
  [5]: /docs/img/4059228044.png
  [6]: /docs/img/4094571871.png
  [7]: /docs/img/1587610634.png
  [8]: /docs/img/1141028461.png
  [9]: /docs/img/2882579563.gif
  [10]: /docs/img/group.jpg
  [11]: /docs/img/wechat.jpg