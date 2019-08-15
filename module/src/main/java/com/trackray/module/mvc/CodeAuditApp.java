package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;

import java.io.*;
import java.util.*;

import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.codeAudit.CodeAuditInner;
import com.trackray.module.inner.codeAudit.Vuln;
import com.trackray.module.inner.codeAudit.CodeVulnRepository;
import org.apache.commons.io.FileUtils;
import org.javaweb.core.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/14 15:32
 */
@Plugin(value = "codeAudit",title = "代码审计" , author = "浅蓝")
@Rule(auth = true)
public class CodeAuditApp extends MVCPlugin {

    @Autowired
    private CodeAuditInner codeAuditInner;

    @Value("${temp.dir}")
    private String tempdir;
    private File tempfile;

    @Override
    public boolean check(Map<String, Object> param) {
        tempfile = new File(tempdir,"codeAudit");
        if (!tempfile.exists())
            tempfile.mkdirs();
        return true;
    }

    @Override
    public void index() {
        HashSet<String> names = new HashSet<>();
        for (Vuln vuln : codeVulnRepository.findAll()) {
            names.add(vuln.getName());
        }
        HashSet<String> rules = new HashSet<>();
        String rulePath = CodeAuditInner.RULE_PATH;
        File file = new File(rulePath);
        File[] files = file.listFiles();
        for (File f: files) {
            rules.add(f.getName());
        }
        model.addObject("rules",rules);
        model.addObject("names",names);
        model.setViewName("index");
    }

    private void unZip(File zipFileName, String outputDirectory)
            throws Exception {
        ZipFile zipFile = new ZipFile(zipFileName);
        try {

            Enumeration<?> e = zipFile.entries();

            ZipEntry zipEntry = null;

            createDirectory(outputDirectory, "");

            while (e.hasMoreElements()) {

                zipEntry = (ZipEntry) e.nextElement();


                if (zipEntry.isDirectory()) {

                    String name = zipEntry.getName();

                    name = name.substring(0, name.length() - 1);

                    File f = new File(outputDirectory + File.separator + name);

                    f.mkdir();


                } else {

                    String fileName = zipEntry.getName();

                    fileName = fileName.replace('\\', '/');

                    if (fileName.indexOf("/") != -1) {

                        createDirectory(outputDirectory, fileName.substring(0,
                                fileName.lastIndexOf("/")));

                        fileName = fileName.substring(
                                fileName.lastIndexOf("/") + 1,
                                fileName.length());

                    }

                    File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());

                    f.createNewFile();

                    InputStream in = zipFile.getInputStream(zipEntry);

                    FileOutputStream out = new FileOutputStream(f);

                    byte[] by = new byte[1024];

                    int c;

                    while ((c = in.read(by)) != -1) {

                        out.write(by, 0, c);

                    }

                    in.close();

                    out.close();

                }

            }

        } catch (Exception ex) {

            log.error(ex.getMessage());

        } finally {
            zipFile.close();
        }

    }
    private  void createDirectory(String directory, String subDirectory) {

        String dir[];

        File fl = new File(directory);

        try {

            if (subDirectory == "" && fl.exists() != true) {

                fl.mkdir();

            } else if (subDirectory != "") {

                dir = subDirectory.replace('\\', '/').split("/");

                for (int i = 0; i < dir.length; i++) {

                    File subFile = new File(directory + File.separator + dir[i]);

                    if (subFile.exists() == false)

                        subFile.mkdir();

                    directory += File.separator + dir[i];

                }

            }

        } catch (Exception ex) {

            log.error(ex.getMessage());

        }

    }
    @Function
    public void getRule() throws IOException {
        String rulePath = CodeAuditInner.RULE_PATH;
        File file = new File(rulePath);
        String filename = param.get("file").toString();
        for (File f : file.listFiles()) {
            if (f.getName().equals(filename)){
                write(FileUtils.readFileToString(f,"UTF-8"));
                return;
            }
        }
        write("文件不存在");
    }
    @Function
    public void saveRule() throws IOException {
        String rulePath = CodeAuditInner.RULE_PATH;
        File file = new File(rulePath);
        String filename = param.get("file").toString();
        String content = param.get("content").toString();
        for (File f : file.listFiles()) {
            if (f.getName().equals(filename)){
                FileUtils.writeStringToFile(f , content,"UTF-8");
                write("success");
                return;
            }
        }
        write("error");
    }

    @Autowired
    private CodeVulnRepository codeVulnRepository;
    @Function
    public void upload(){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) this.request;
        List<MultipartFile> files = request.getFiles("file");
        model.setViewName("../common/default");
        File upload = new File(tempfile, "upload");
        File output = new File(tempfile, "output");
        if (!upload.exists())
            upload.mkdirs();
        if (!output.exists())
            output.mkdirs();
        String uuid = UUID.randomUUID().toString();
        if (files!=null && !files.isEmpty()) {
            MultipartFile multipartFile = files.get(0);
            String name = param.getOrDefault("name",multipartFile.getOriginalFilename()).toString();
            if (!codeVulnRepository.findAllByName(name).isEmpty()) {
                model.addObject("msg", "<h1>任务名已存在</h1><a href='javascript:history.back()'>GO BACK</a>");
                return;
            }
            File uploadDest = new File(upload, uuid+".zip");

            try {
                multipartFile.transferTo(uploadDest);

                if (uploadDest.exists() && uploadDest.isFile()){
                    File outputDir = new File(output, uuid);
                    try {
                        unZip(uploadDest , outputDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("解压失败",e);
                        errorMsg = "解压失败";
                        return;
                    }
                    if (outputDir.exists() && outputDir.list().length>0){

                        codeAuditInner.setRootPath(outputDir);
                        codeAuditInner.setLang(CodeAuditInner.Lang.JAVA);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                AbstractPlugin<List<Vuln>> executor = codeAuditInner.executor();
                                List<Vuln> result = executor.result();
                                for (Vuln vuln : result) {
                                    vuln.setName(name);
                                    try {
                                        codeVulnRepository.save(vuln);
                                    }catch (Exception e){
                                        log.error("保存codevuln失败 "+vuln.getCode(),e);
                                    }
                                }
                                uploadDest.delete();
                                outputDir.delete();
                            }
                        };
                        new Thread(runnable).start();
                        //另起线程扫描代码
                        model.addObject("msg","<h1>正在检测代码</h1><a href='javascript:history.back()'>GO BACK</a>");
                        return;
                    }

                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                errorMsg = e.getMessage();
                return;
            }

        }


    }
    @Function
    public void delete(){
        String name = param.get("name").toString();
        List<Vuln> vulns = codeVulnRepository.findAllByName(name);
        codeVulnRepository.deleteAll(vulns);
        write("删除成功");
    }
    @Function
    public void view(){
        String name = param.get("name").toString();
        List<Vuln> vulns = codeVulnRepository.findAllByName(name);
        model.addObject("vulns",vulns);
        model.setViewName("view");
    }

}
