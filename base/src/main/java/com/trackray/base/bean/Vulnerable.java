package com.trackray.base.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Vulnerable {

    private String title  = "";//漏洞标题
    private String address  = "";//影响地址
    private Integer level = Level.INFO.getLevel();//漏洞等级
    private Integer type = Type.UNKNOWN.getType(); //漏洞类型
    private String payload = "";//攻击载荷
    private String request = "";
    private String response = "";
    private List<String> vulnId ;//漏洞ID
    private List<String> risk;    //存在的风险
    private String repair ;  //修复建议
    private String detail;//漏洞详情
    private List<String>  references;

    public enum Type {
        //id 1=前端层  2=后端层  3=服务应用层  4=非服务类型漏洞(如:弱口令)
        UNKNOWN(0,0,"未知类型"),
        SQL_INJECTION(1,2,"SQL注入"),
        XSS(2,1,"跨站脚本攻击"),
        XXE_INJECTION(3,2,"XML外部实体注入"),
        CSRF(4,1,"跨站请求伪造"),
        UNAUTHORIZED_ACCESS(5,4,"未授权访问漏洞"),
        FILE_OPERATION(6,2,"文件操作"),
        INFO_LEAKAGE(7,4,"敏感信息泄露"),
        DESERIALIZE(8,2,"反序列化"),
        COMMAND_EXECUTION(9,2,"命令执行"),
        CODE_EXECUTION(10,2,"代码执行"),
        LOGIC_BUG(11,4,"逻辑漏洞"),
        OVER_PERMISSIONS(12,2,"越权漏洞"),
        OVERFLOW(13,4,"溢出漏洞"),
        CONFIG_ERROR(14,2,"配置不当"),
        ;

        private int id;
        private int type;
        private String name;

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        Type( int type ,int id, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }
    }

    public enum Level {
        INFO(0,"提示"),
        LOW(1,"低危"),
        MIDDLE(2,"中危"),
        HIGH(3,"高危");

        private int level;
        private String name;

        Level(int level, String name) {
            this.level = level;
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }
    }
}
