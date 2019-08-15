package com.trackray.module.inner;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.InnerPlugin;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/12 10:37
 */
@Plugin(title = "文件上传漏洞Fuzz字典生成内部插件" ,value = "uploadFuzzInner" , link = "https://github.com/c0ny1/upload-fuzz-dic-builder" , author = "浅蓝")
@Rule(params = {
        @Param(key = "upload_file_name"),
        @Param(key = "allow_suffix"),
        @Param(key = "language"),
        @Param(key = "middleware"),
        @Param(key = "os"),
        @Param(key = "double_suffix"),
})
@Data
public class UploadFuzzInner extends InnerPlugin <Collection<String>>{

    public static String[] language_choices = {"asp","php","jsp","all"};
    public static String[] middleware_choices = {"iis","apache","tomcat","all"};
    public static String[] os_choices = {"win","linux","all"};

    
    public static String [] html_parse_suffix = {"html","htm","phtml","pht","Html","Htm","pHtml"};
    public static String [] asp_parse_suffix = {"asp","aspx","asa","asax","ascx","ashx","asmx","cer","aSp","aSpx","aSa","aSax","aScx","aShx","aSmx","cEr"};
    public static String [] php_parse_suffix = {"php","php5","php4","php3","php2","pHp","pHp5","pHp4","pHp3","pHp2"};
    public static String [] jsp_parse_suffix = {"jsp","jspa","jspx","jsw","jsv","jspf","jtml","jSp","jSpx","jSpa","jSw","jSv","jSpf","jHtml"};


    public static List<String> windows_os = new ArrayList<String>(){{
        addAll(Arrays.asList(" ", ".", "/", "::$DATA", "<", ">", ">>>", "%20", "%00"));
        addAll(str_81_to_ff());
    }};
    public List<String> windows_suffix_creater(String[] suffix){
        List<String> arr = new ArrayList<>();
        for (String s : suffix) {
            for (String w : windows_os) {
                String str = String.format("%s%s",s,w);
                arr.add(str);
            }
        }
        return arr;
    }

    public static List str_81_to_ff(){
        List arr =new ArrayList<String>();
        for (int i = 129; i <256 ; i++) {
            String str = String.format("%x",i);
            str = "%" + str;
            arr.add(URLDecoder.decode(str));
        }
        return arr;
    }
    private String upload_file_name ;
    private String allow_suffix ;
    private String language ;
    private String middleware ;
    private String os;
    private boolean double_suffix ;

    public static String[] win_tomcat = {"%20","::$DATA","/"};
    public List<String> tomcat_suffix_creater(String[] suffix) {
        List<String> arr = new ArrayList<>();
        for (String l : suffix) {
            for (String t : win_tomcat) {
                String str = String.format("%s%s" ,l,t);
                arr.add(str);
            }
        }
        return arr;
    }

    public List<String> iis_suffix_creater(String[] suffix){
        List<String> arr = new ArrayList<>();
        for (String l : suffix) {
            String str = String.format("%s;.%s",l,allow_suffix);
            arr.add(str);
        }
        return arr;
    }

    public List<String> apache_suffix_creater(String[] suffix){
        List<String> arr = new ArrayList<>();
        for (String l : suffix) {
            String str = String.format("%s.xxx",l);
            arr.add(str);
            str = String.format("%s%s",l,URLDecoder.decode("%0a")) ;//CVE-2017-15715
            arr.add(str);
        }
        return arr;
    }


    public List<String> str_00_truncation(String[] suffix,String allow_suffix){
        List<String> arr = new ArrayList<>();
        for (String i : suffix) {
            String str = String.format("%s%s.%s",i,"%00",allow_suffix);
            arr.add(str);
            str = String.format("%s%s.%s",i,URLDecoder.decode("%00"),allow_suffix);
            arr.add(str);
        }
        return arr;
    }

    public Collection<String> str_case_mixing(String word){
        word = word.toLowerCase();
        String tempWord = word.intern();
        List<String> plist = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < tempWord.length(); i++) {
            char c = word.charAt(i);
            plist.add(String.valueOf(c));
        }
        int num = plist.size();
        for (int i = 0; i < num; i++) {
            for (int j = i; j < num + 1; j++) {
                String sContent = String.join("",plist.subList(0,i).toArray(new String[]{}));
                String mContent = String.join("",plist.subList(i,j).toArray(new String[]{}));;
                mContent = mContent.toUpperCase();
                String eContent = String.join("",plist.subList(j,plist.size()).toArray(new String[]{}));
                String content = String.format("%s%s%s",sContent,mContent,eContent);
                set.add(content);
            }
        }
        return set;
    }

    public List<String> list_case_mixing(String[] li){
        List<String> arr = new ArrayList<>();
        for (String l : li) {
            arr.addAll( str_case_mixing(l) );
        }

        return arr;
    }

    public List<String> str_double_suffix_creater(String suffix){
        List<String> arr = new ArrayList<>();
        for (int i = 1; i < suffix.length(); i++) {
            ArrayList<String> strs = new ArrayList<>();
            for (char c : suffix.toCharArray()) {
                strs.add(String.valueOf(c));
            }
            strs.set(i,suffix);
            String join = String.join("", strs.toArray(new String[]{}));
            arr.add(join);
        }
        return arr;
    }

    public Collection<String> duplicate_removal(Collection<String> list){
        HashSet<String> set = new HashSet<>();
        set.addAll(list);
        return set;
    }
    public List<String> list_double_suffix_creater(Collection<String> list_suffix){
        List<String> arr = new ArrayList<>();
        for (String l : list_suffix) {
            arr.addAll(str_double_suffix_creater(l));
        }
        return arr;
    }

    @Override
    public void before() {
        if ("iis".equals(middleware))
            os = "win";

    }

    private Collection<String> parse_suffix = new ArrayList<>();
    private Collection<String> case_parse_suffix = new ArrayList<>();
    private Collection<String> middleware_parse_suffix = new ArrayList<>();
    private Collection<String> htaccess_suffix = new ArrayList<>();
    private Collection<String> os_parse_suffix = new ArrayList<>();
    private Collection<String> double_parse_suffix = new ArrayList<>();

    @Override
    public void process() {
        if (language.equals("asp") ){
            parse_suffix.addAll(Arrays.asList(asp_parse_suffix));
        }else if(language.equals("php")){
            parse_suffix.addAll(Arrays.asList(html_parse_suffix));
            parse_suffix.addAll(Arrays.asList(php_parse_suffix));
        }else if(language.equals("jsp")){
            parse_suffix.addAll(Arrays.asList(jsp_parse_suffix));
        }else{
            parse_suffix.addAll(Arrays.asList(asp_parse_suffix));
            parse_suffix.addAll(Arrays.asList(html_parse_suffix));
            parse_suffix.addAll(Arrays.asList(php_parse_suffix));
            parse_suffix.addAll(Arrays.asList(jsp_parse_suffix));
        }

        List<String> case_html_parse_suffix;
        List<String> case_asp_parse_suffix;
        List<String> case_php_parse_suffix;
        List<String> case_jsp_parse_suffix;

        if (os.equals("win") || os.equals("all")){
            case_html_parse_suffix = list_case_mixing(html_parse_suffix);
            case_asp_parse_suffix = list_case_mixing(asp_parse_suffix);
            case_php_parse_suffix = list_case_mixing(php_parse_suffix);
            case_jsp_parse_suffix = list_case_mixing(jsp_parse_suffix);
            case_parse_suffix = list_case_mixing(parse_suffix.toArray(new String[]{}));
        }else{
            case_html_parse_suffix = Arrays.asList(html_parse_suffix);
            case_asp_parse_suffix = Arrays.asList(asp_parse_suffix);
            case_php_parse_suffix = Arrays.asList(php_parse_suffix);
            case_jsp_parse_suffix = Arrays.asList(jsp_parse_suffix);
            case_parse_suffix = parse_suffix;
        }

        if (middleware.equals("iis")){
            List<String > case_asp_php_jsp_parse_suffix = new ArrayList<String>();
            case_asp_php_jsp_parse_suffix.addAll(case_asp_parse_suffix);
            case_asp_php_jsp_parse_suffix.addAll(case_php_parse_suffix);
            case_asp_php_jsp_parse_suffix.addAll(case_jsp_parse_suffix);
            middleware_parse_suffix = iis_suffix_creater(case_asp_php_jsp_parse_suffix.toArray(new String[]{}));
        }else if (middleware.equals("apache")){
            List<String > case_asp_php_html_parse_suffix = new ArrayList<String>();
            case_asp_php_html_parse_suffix.addAll(case_asp_parse_suffix);
            case_asp_php_html_parse_suffix.addAll(case_php_parse_suffix);
            case_asp_php_html_parse_suffix.addAll(case_html_parse_suffix);
            middleware_parse_suffix = apache_suffix_creater(case_asp_php_html_parse_suffix.toArray(new String[]{}));
        }else if(middleware.equals("tomcat") && os.equals("linux")){
            middleware_parse_suffix.addAll(case_php_parse_suffix);
            middleware_parse_suffix.addAll(case_jsp_parse_suffix);
        }else if(middleware.equals("tomcat") && (os.equals("win")||os.equals("all"))){
            List<String > case_php_jsp_parse_suffix = new ArrayList<String>();
            case_php_jsp_parse_suffix.addAll(case_php_parse_suffix) ;
            case_php_jsp_parse_suffix.addAll(case_jsp_parse_suffix) ;
            middleware_parse_suffix = tomcat_suffix_creater(case_php_jsp_parse_suffix.toArray(new String[]{}));
        }else{
            List<String > case_asp_php_parse_suffix = new ArrayList<String>();
            case_asp_php_parse_suffix.addAll(case_asp_parse_suffix );
            case_asp_php_parse_suffix.addAll(case_php_parse_suffix );
            List<String > iis_parse_suffix = iis_suffix_creater(case_asp_php_parse_suffix.toArray(new String[]{}));
            List<String > case_asp_php_html_parse_suffix = new ArrayList<String>();
            case_asp_php_html_parse_suffix.addAll(case_asp_parse_suffix );
            case_asp_php_html_parse_suffix.addAll(case_php_parse_suffix );
            case_asp_php_html_parse_suffix.addAll(case_html_parse_suffix );
            List<String> apache_parse_suffix = apache_suffix_creater(case_asp_php_html_parse_suffix.toArray(new String[]{}));
            List<String> case_php_jsp_parse_suffix  = new ArrayList<String>();
            case_php_jsp_parse_suffix.addAll(case_php_parse_suffix);
            case_php_jsp_parse_suffix.addAll(case_jsp_parse_suffix);
            List<String> tomcat_parse_suffix = tomcat_suffix_creater(case_php_jsp_parse_suffix.toArray(new String[]{}));
            middleware_parse_suffix.addAll(iis_parse_suffix );
            middleware_parse_suffix.addAll(apache_parse_suffix );
            middleware_parse_suffix.addAll(tomcat_parse_suffix );

        }
        middleware_parse_suffix = duplicate_removal(middleware_parse_suffix);

        if ((middleware.equals("apache") || middleware .equals("all")) && (os.equals("win") || os.equals("all"))){
            htaccess_suffix = str_case_mixing(".htaccess");
        }else if((middleware.equals("apache") || middleware.equals("all")) &&  os.equals("linux")){
            htaccess_suffix.add(".htaccess");
        }

        if (os.equals("win")){
            os_parse_suffix = windows_suffix_creater(case_parse_suffix.toArray(new String[]{}));
        }else if(os.equals("linux")){
            os_parse_suffix = parse_suffix;
        }else{
            List<String> win_suffix  = windows_suffix_creater(case_parse_suffix.toArray(new String[]{}));
            os_parse_suffix.addAll(win_suffix);
            os_parse_suffix.addAll(parse_suffix);
        }
        os_parse_suffix = duplicate_removal(os_parse_suffix);
        List<String> language_parse_suffux= str_00_truncation(case_parse_suffix.toArray(new String[]{}), allow_suffix);

        if (double_suffix) {
            double_parse_suffix = list_double_suffix_creater(case_parse_suffix);
        }
        HashSet<String> all_parse_suffix = new HashSet<>();
        all_parse_suffix.addAll(case_parse_suffix );
        all_parse_suffix.addAll(middleware_parse_suffix );
        all_parse_suffix.addAll(os_parse_suffix );
        all_parse_suffix.addAll(language_parse_suffux );
        all_parse_suffix.addAll(double_parse_suffix);
        List<String> result = new ArrayList<String>();
        for (String s : all_parse_suffix) {
            if (StringUtils.isEmpty(s)||StringUtils.isBlank(s))
                continue;
            s = String.format("%s.%s",upload_file_name,s);
            result.add(s);
        }
        super.result = result;
    }

    public static void main(String[] args) {

        UploadFuzzInner fuzzInner = new UploadFuzzInner();
        fuzzInner.setLanguage("all");
        fuzzInner.setOs("all");
        fuzzInner.setMiddleware("all");
        fuzzInner.setAllow_suffix("jpg");
        fuzzInner.setUpload_file_name("123123");
        fuzzInner.setDouble_suffix(true);
        AbstractPlugin<Collection<String>> executor = fuzzInner.executor();
        Collection<String> result = executor.result();
        for (String s : result) {
            System.out.println(s);
        }

    }

}
