package com.trackray.web.api;

import com.trackray.base.bean.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/9 13:23
 */
@Controller
public class HtmlController {

    @Autowired
    private Banner banner;
    @GetMapping("/")
    public String index(Model model){
        String banner = this.banner.generate();
        model.addAttribute("banner",banner);
        return "index";
    }

    @GetMapping("/api")
    public String api(){return "api";}


    @GetMapping("/console")
    public String console(){return "socket";}

    @GetMapping("/msf")
    public String msf(){return "msf";}


    @GetMapping("/scan")
    public String scan(){return "scan";}
}
