package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.MVCPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/22 12:54
 */
@Plugin(title = "Java 在线反编译" ,value = "javaDecompile", author = "浅蓝" )
@Rule(auth = true)
public class JavaDecompile extends MVCPlugin{

    public static final String LINUX = BASE + "/javaDecompile/linux/jad";
    public static final String WINDOWS = BASE + "/javaDecompile/windows/jad.exe";

    @Value("${temp.dir}")
    private String tmpdir;

    @Override
    public void index() {

        model.setViewName("index");
    }

    @Function
    public void dirs(){

        String path = tmpdir.concat("trackJad");


        File file = new File(path,"output");

        if (!file.exists()) {
            model.setViewName("dirs");
            return ;
        }

        File[] filelist = file.listFiles();

        ArrayList<String> files = new ArrayList<>();

        for (File f1 : filelist) {
            if (f1.isDirectory()){
                files.add(f1.getName());
            }
        }
        model.addObject("dirs",files);
        model.setViewName("dirs");
    }

    @Function
    public void view(){
        String path = tmpdir.concat("trackJad");

        File file = new File(path,"output");

        if (file.exists())
        {

            String uuid = param.get("uuid").toString();

            if (StringUtils.containsAny(uuid, ".","/","\\","%2f")){
                return;
            }

            File outDir = new File(file, uuid);

            if (outDir.exists()){

                ArrayList<File> files = new ArrayList<>();

                getFiles(outDir , files);
                HashMap<String, String> codes = new HashMap<>();
                for (File e : files) {

                    String key = e.getPath();

                    if (key.contains(uuid)){
                        key = key.substring(key.indexOf(uuid));
                    }

                    try {
                        String content = FileUtils.readFileToString(e);
                        codes.put(key,content);
                    } catch (IOException e1) {
                    }

                }

                model.addObject("codes",codes);


            }

        }
        model.setViewName("result");
    }


    public  void getFiles(File file , List<File> fileList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    getFiles(files[i] , fileList);
                } else {
                    fileList.add(files[i]);
                }

            }
        } else {
            fileList.add(file);
        }
    }

    @Function
    public void upload(){

        String path = tmpdir.concat("trackJad");

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        File file = new File(path);

        if (!file.exists())
            file.mkdir();

        File outfile = new File(file, "output");
        if (!outfile .exists())
            outfile.mkdir();

        String uuid = UUID.randomUUID().toString();


        if (files!=null && !files.isEmpty()){

            MultipartFile multipartFile = files.get(0);


            String fileName = multipartFile.getOriginalFilename();

            File outDir = new File(outfile, uuid);

            try {
                if (fileName.endsWith("class")){
                    File dest = new File(file , fileName);

                    multipartFile.transferTo(dest);
                    if (!outDir.exists())
                        outDir.mkdir();
                    String absolutePath = outDir.getAbsolutePath().replaceAll("\\\\","/");
                    String classFilename = dest.getAbsolutePath().replaceAll("\\\\", "/");

                    switch (Constant.TRACKRAY_SYSTEMOS) {
                        case Constant.WINDOWS:
                            shell().target(WINDOWS)
                                    .block(true)
                                    .exec("-o", "-d"+absolutePath , "-sjava",classFilename);
                            break;
                        case Constant.LINUX:
                            shell().block(true)
                                    .exec("chmod +x "+LINUX );
                            shell().block(true)
                                    .exec(LINUX +" -o -d" + absolutePath + " -sjava "+ classFilename);

                            break;

                    }
                    Thread.sleep(1500);

                }else if (fileName.endsWith("jar")){

                    if (!outDir.exists())
                        outDir.mkdir();

                    File jarDir = new File(file, "jars/"+uuid);
                    if (!jarDir.exists())
                        jarDir.mkdirs();

                    File dest = new File(jarDir,fileName);

                    multipartFile.transferTo(dest);

                    Shell exec = shell();

                    String syspath = System.getenv().get("Path");

                    exec.getRuntime().exec(new String[]{"jar","-xvf",fileName}, new String[]{"Path="+syspath} , jarDir);

                    Thread.sleep(1500);

                    dest.delete();

                    String absolutePath = outDir.getAbsolutePath().replaceAll("\\\\","/");
                    String jarPath = jarDir.getAbsolutePath().replaceAll("\\\\","/");


                    switch (Constant.TRACKRAY_SYSTEMOS) {
                        case Constant.WINDOWS:
                            shell().target(WINDOWS)
                                    .block(true)
                                    .exec("-r", "-ff" , "-d" , absolutePath , "-sjava",jarPath+"/**/*.class");
                            break;
                        case Constant.LINUX:
                            shell().block(true)
                                    .exec("chmod +x "+LINUX );
                            shell().block(true)
                                    .exec(LINUX +" -r -ff -d" + absolutePath + " -sjava "+ jarPath +"/**/*.class");

                            break;

                    }

                    Thread.sleep(1500);

                }

                if (outDir.exists()){
                    File[] filelist = outDir.listFiles();

                    if (filelist.length>0){
                        model.setViewName("../common/default");
                        model.addObject("msg","<h1>反编译成功，UUID="+uuid+"</h1><a href='javascript:history.back()'>GO BACK</a>");
                    }else{
                        model.setViewName("../common/default");
                        model.addObject("msg","<h1>反编译失败请检查权限问题</h1><a href='javascript:history.back()'>GO BACK</a>");
                    }

                }else{
                    model.setViewName("../common/default");
                    model.addObject("msg","<h1>输出文件夹未成功创建</h1><a href='javascript:history.back()'>GO BACK</a>");
                }

            } catch (Exception e) {
                model.setViewName("../common/default");
                model.addObject("msg",e.getMessage());

            }


        }

    }

}