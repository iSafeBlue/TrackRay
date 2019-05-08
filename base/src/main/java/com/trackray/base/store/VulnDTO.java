package com.trackray.base.store;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class VulnDTO implements Serializable {
    @Id
    @GeneratedValue
    private Integer id; //数据库ID

    private String taskMd5 = ""; //任务ID

    private String address = ""; //风险地址

    private String title = "";   //漏洞标题

    private Integer level =0;  //漏洞等级

    @Column(columnDefinition = "LONGVARCHAR" , length = 65536)
    private String payload = ""; //攻击载荷
    @Column(columnDefinition = "LONGVARCHAR" , length = 65536)
    private String message = ""; //响应信息

    private String repair  = "";  //修复建议

    private String risk[];    //存在的风险

    private String vulnId[];  //漏洞ID 可以是CNVD、CVE、WOOYUN等ID类型，多个

    private String reference[]; //参考链接

    private Integer type = 0;   //漏洞类型

    private Date createTime = new Date();

}