package com.trackray.web.api;

import com.trackray.base.bean.Banner;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.web.service.PluginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/9 13:23
 */
@Controller
public class HtmlController {
    @Autowired
    private PluginService pluginService;
    @Autowired
    private Banner banner;
    @GetMapping("/")
    public String index(Model model){
        String banner = this.banner.generate();
        model.addAttribute("banner",banner);
        return "index";
    }

    @GetMapping("/api")
    public String api(){
        return "manage/api";
    }


    @GetMapping("/console")
    public String console(){return "socket";}

    @GetMapping("/msf")
    public String msf(){return "msf";}


    @GetMapping("/scan")
    public String scan(){return "scan";}

    @GetMapping("/login")
    public String login(){return "/manage/login";}

    @PostMapping("/doLogin")
    public String doLogin(String account , String password , HttpSession session){
        String systemAccount = Constant.SYSTEM_ACCOUNT;
        String systemPassword = Constant.SYSTEM_PASSWORD;

        if (StringUtils.equals(account,systemAccount) && StringUtils.equals(password,systemPassword))
        {
            session.setAttribute("user",systemAccount);
            return "redirect:/manage";
        }
        return "redirect:/login";
    }



    @GetMapping("/manage")
    public String manage(Model model,HttpSession session){
        session.setAttribute("username","admin");

        Map<String, MVCPlugin> mvcPlugins = pluginService.findMVCPlugins();
        HashMap<String, String> param = new HashMap<>();
        for (Map.Entry<String, MVCPlugin> entry : mvcPlugins.entrySet()) {
            param.put(entry.getKey(),entry.getValue().currentPlugin().title());
        }
        session.setAttribute("menu",param);
        return "manage/index";
    }


}
