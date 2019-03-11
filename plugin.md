### 交互式插件

主要在有参数交互的场景下使用，需要继承```WebSocketPlugin```，并使用```@Plugin``` ```@Rule```注解

- check方法校验参数合法
- start方法处理主要功能
- onClose方法在插件销毁时调用


e.g:

    @Plugin(title = "资产扫描" , author = "blue")
    @Rule(params = {
                    @Param(key = "domain",defaultValue = "baidu.com" , desc = "目标域名"),
                    @Param(key = "port",defaultValue = "true" , desc = "是否扫描端口"),
                    @Param(key = "thread",defaultValue = "3" , desc = "线程数"),}
                    , websocket = true)
    public class AssetsScan extends WebSocketPlugin{
        private String domain;
        private boolean isScanPort;
        private List<Asset> assets  = new ArrayList<>();
        private ExecutorService service;
        @Override
        public boolean check(Map param) {
            domain = param.get("domain").toString();
            String port = param.get("port").toString();
            isScanPort = Boolean.parseBoolean(port);
            int thread = Integer.parseInt(param.get("thread").toString());
            if (thread >0 && thread<6)
            {
                service = Executors.newFixedThreadPool(thread);
            }else{
                send("thread不可以大于5");
                return false;
            }
            return true;
        }
    
        @Override
        public Object start() {
            String root = ReUtils.getDomain(domain);
            send("扫描初始化中，你的目标是："+root);
            //....
            return ...;
        }
    
        @Override
        public void onClose() {
            service.shutdownNow();
        }
    
    }


### 接口式插件

通过http请求来调用此类插件，需要一次性填写好参数，可选择多重响应类型，如：JSON、XML、TEXT、HTML等。

要继承```CommonPlugin```类，并使用```@Plugin``` ```@Rule```注解。

e.g

    @Rule(params = {@Param(key = "target", desc = "目标地址"),} , type = AbstractPlugin.Type.HTML)
    @Plugin(title = "TEST TITLE " , link = "http://www.baidu.com/xxx", author = "blue" )
    public class PasswordRead extends CommonPlugin{
        @Override
        public boolean check(Map param) {
            //...
            if (...){
                return true;
            }else {
                return false;
            }
        }
    
        @Override
        public Object start() {
            //...
            return ...;
        }
    }


### 扫描器插件

该类型插件会在扫描网站时调用，需要继承```AbstractExploit```类，使用```@Exploit```注解。


e.g

        @Exploit(value = "dedecmstest" , title = "DEDECMS 2.1 sql注入" , author = "blue", desc = "测试测试测试" )
        public class DedecmsSQLi extends AbstractExploit {
        
            @Override
            public boolean check(Result result) {
                return ...;
            }
        
            @Override
            public void init(Task task) {
                //..此方法可重写 也可以不用重写，主要用于初始化
            }
        
            @Override
            public void attack(Task task) {
                String target = task.getTargetStr();
                crawlerPage.getRequest().setUrl(target.concat("/123.php?id=1'"));
                fetcher.run(crawlerPage);
                String content = PageUtils.getContent(crawlerPage);
                if (content.contains("mysql_error")) {
                    addVul(Vulnerable.builder().description("存在dedecms漏洞")
                            .vulType(VulnType.SQL_INJECTION.getType())
                            .level(VulnLevel.DANGER.getLevel()).affectsUrl(target).build());
                }
            }
        
        }

### 爬虫插件

该插件在扫描时勾选“网页爬虫”时调用，需要继承```CrawlerPlugin```类，使用```@Plugin```注解
e.g

    @Plugin(title = "系统指纹信息探测",author = "blue")
    public class FingerProbe extends CrawlerPlugin {
        @Override
        public boolean check() {
            //...
            return true;
        }
    
        @Override
        public void process() {
            Task task = crawlerPage.getTask();
            String content = crawlerPage.getResponse().getStatus().getContent();
            //...
        }
    }

### python 插件

1. 通过系统命令调用python脚本

可参考`MS17010.java`

2. 通过jython调用python脚本

首先在`application.properties`中配置如下参数

`python.script.path` python脚本存放目录

`python.package.path` python第三方库site-packages目录

`maven.repository.path` maven仓库根目录

然后在python脚本存放目录中编写如下格式的脚本

```python
import requests

def params():
    return {"url":"请求地址"}

def verify(dict):
    text = requests.get(dict["url"]).text
    return text
```

> 必须有 params() 和 verify() 函数
> params() 函数必须返回字典类型对象 {"参数名":"参数说明"}
> verify() 函数必须有一个参数，为字典类型，用于接收从trackray中传入的参数。
> verify() 函数要写返回值
> 尽量使用 python2.7 原生模块