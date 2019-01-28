package com.trackray.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/9 13:23
 */
@Controller
public class HtmlController {

    @GetMapping("/")
    public String index(){return "index";}

    @GetMapping("/api")
    public String api(){return "api";}


    @GetMapping("/console")
    public String console(){return "socket";}

    @GetMapping("/msf")
    public String msf(){return "msf";}


    @GetMapping("/scan")
    public String scan(){return "scan";}
}
