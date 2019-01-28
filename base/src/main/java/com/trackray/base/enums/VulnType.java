package com.trackray.base.enums;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public enum VulnType {
    //id 1=前端层  2=后端层  3=服务应用层  4=非服务类型漏洞(如:弱口令)
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
    OVERFLOW(13,4,"溢出漏洞")
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

    VulnType( int id ,int type , String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
