package com.trackray.web.api;

import com.trackray.base.bean.Banner;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.quartz.QuartzManager;
import com.trackray.base.store.VulnDTO;
import com.trackray.base.store.VulnRepository;
import com.trackray.web.dto.TaskDTO;
import com.trackray.web.repository.TaskRepository;
import com.trackray.web.service.PluginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private VulnRepository vulnRepository;

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

            Map<String, MVCPlugin> mvcPlugins = pluginService.findMVCPlugins();
            HashMap<String, String> param = new HashMap<>();
            for (Map.Entry<String, MVCPlugin> entry : mvcPlugins.entrySet()) {
                param.put(entry.getKey(),entry.getValue().currentPlugin().title());
            }
            session.setAttribute("menu",param);
            session.setAttribute("user",systemAccount);
            return "redirect:/manage";
        }
        return "redirect:/login";
    }



    @GetMapping("/manage")
    public String manage(){
        return "manage/index";
    }


    @GetMapping("/apps/{plugin}")
    public String mvc(@PathVariable String plugin,
                      Model model){
        model.addAttribute("plugin",plugin);
        return "manage/mvcplugin";
    }

    @GetMapping("/manage/create")
    public String create(){
        return "manage/create";
    }

    @GetMapping("/manage/task/{task}")
    public String view(@PathVariable String task , Model model){
        TaskDTO t = taskRepository.findTaskDTOByTaskMd5(task);
        List<VulnDTO> vulns = vulnRepository.findAllByTaskMd5(task);
        if (t!=null && vulns!=null) {
            model.addAttribute("task", task);
            model.addAttribute("vulns", vulns);
        }
        return "manage/view";
    }

    @GetMapping("/manage/tasks")
    public String tasks(Model model , HttpSession session){
        String user = session.getAttribute("user").toString();
        List<TaskDTO> tasks = taskRepository.findAllByUserEquals(user);

        ArrayList<TaskDTO> todo = new ArrayList<>();
        ArrayList<TaskDTO> scanning = new ArrayList<>();
        ArrayList<TaskDTO> done = new ArrayList<>();

        for (TaskDTO task : tasks) {
            Integer status = task.getStatus();
            switch (status){
                case 0:
                    todo.add(task);
                    break;
                case 1:
                    scanning.add(task);
                    break;
                case 2:
                    done.add(task);
                    break;
            }
        }
        tasks.clear();
        model.addAttribute("todo",todo);
        model.addAttribute("scanning",scanning);
        model.addAttribute("done",done);
        return "manage/tasks";
    }


}
