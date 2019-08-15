package com.trackray.module.inner.codeAudit;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/14 10:27
 */
@Plugin(value = "codeAuditInner" , title = "代码审计内部插件" , author = "浅蓝" , desc = "根据CAFJE修改")
public class CodeAuditInner extends InnerPlugin<List<Vuln>> {

    public enum Lang{
        PHP("php"),
        JAVA("jsp","java","html","xml","jspx");
        private List<String> suffix;
        Lang(String ... str){
            suffix = Arrays.asList(str);
        }
        public List<String> getSuffix() {
            return suffix;
        }

        public void setSuffix(List<String> suffix) {
            this.suffix = suffix;
        }
    }
    public static final String RULE_PATH = BASE + "/codeAudit/rules/";
    private Lang lang;
    private File rootPath;

    @Override
    public boolean check(Map param) {
        if (rootPath!=null && rootPath.exists() && rootPath.isDirectory()){
            return true;
        }
        errorMsg = "路径不存在";
        return false;
    }

    @Override
    public void process() {
        result = new ArrayList<Vuln>();
        switch (lang) {
            case PHP:
                break;
            case JAVA:
                fuckJava(lang.getSuffix());
                break;
        }

    }

    private void fuckJava(List<String> suffix) {
        analysisCode(rootPath, suffix);
    }

    public void analysisCode(File filePath, List<String> suffix){
        File[] files = filePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                analysisCode(file , suffix);
            }else if (suffixFile(file ,suffix )){
                List<Rule> rules = rulePaser();
                for (Rule rule : rules) {
                    try {
                        StringBuffer string = regexpParser(file, rule);
                        if (string.length()==0)
                            continue;
                        Vuln vuln = new Vuln();
                        vuln.setCode(string.toString());
                        String rulePath = rootPath.getCanonicalPath().replaceAll("\\\\","/");;
                        String filename = file.getCanonicalPath().replaceAll("\\\\","/");
                        filename = filename.replaceAll(rulePath,"");
                        vuln.setFilename(filename);
                        vuln.setDescription(rule.getDecription());
                        vuln.setRuleid(rule.getId());
                        result.add(vuln);
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                        continue;
                    }
                }

            }
        }
    }

    public StringBuffer regexpParser(File file, Rule rule) throws FileNotFoundException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        if (rule.isWhere()){
            String path = file.getPath();
            String contentReg = rule.getContentReg();
            String filenameReg = rule.getFilenameReg();
            if (contentReg!=null){
                String content = FileUtils.readFileToString(file);
                if (content.matches(contentReg))
                    return stringBuffer;
            }
            if (filenameReg!=null){
                if (path.matches(filenameReg))
                    return stringBuffer;
            }
        }
        Pattern pattern = Pattern.compile(rule.getRegexp());
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line = null;
        while ((line = lnr.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                stringBuffer.append(lnr.getLineNumber() + ":" + line + "\n");
            }
        }
        lnr.close();
        return stringBuffer;
    }

    private List<Rule> rulePaser(){
        String[] suffix = { "xml" };
        File file = new File(RULE_PATH);
        File[] files = file.listFiles();
        List<Rule> list = new ArrayList<>();
        for (File f : files) {
            if (suffixFile(f, Arrays.asList(suffix))) {
                list.addAll(xml2Bean(f));
            }
        }
        return list;
    }

    private List<Rule> xml2Bean(File file) {
        SAXReader reader = new SAXReader();
        Document document = null;
        List<Rule> list = new ArrayList<>();
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            return list;
        }
        Element root = document.getRootElement();
        List<Element> elements = root.elements("vuln");
        for (Element element : elements) {
            Rule rule = new Rule();
            Element where = element.element("where");
            if (where!=null){
                String filenameReg = where.elementText("filename");
                String contentReg = where.elementText("content");
                if (filenameReg!=null)
                    rule.setFilenameReg(filenameReg);
                if (contentReg!=null)
                    rule.setContentReg(contentReg);
                rule.setWhere(true);
            }
            rule.setRegexp(element.elementText("regexp"));
            rule.setName(element.attributeValue("name"));
            rule.setId(element.attributeValue("id"));
            rule.setDecription(element.elementText("decription"));
            rule.setRecommendation(element.elementText("recommendation"));
            rule.setReference(element.elementText("reference"));
            list.add(rule);
        }
        return list;
    }

    public static boolean suffixFile(File file, List<String> suffix) {
        if (suffix == null || suffix.size() == 0) {
            return true;
        }
        boolean flag = false;
        for (String suf : suffix) {
            if (file.getName().endsWith(suf)) {
                flag = true;
            }
        }
        return flag;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
    public void setRootPath(File rootPath) {
        this.rootPath = rootPath;
    }
}
