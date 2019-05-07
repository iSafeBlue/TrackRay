package com.trackray.web.dto;

import com.trackray.base.enums.WEBServer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity(name = "Task")
@Data
public class TaskDTO implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String taskMd5;

    private String user = "";

    private String taskName = "";

    private String target = "";

    private Integer thread;

    private Integer spiderMax;

    private Integer spiderDeep;

    private Integer timeMax;

    private String cookie= "";

    private String rule = "";

    private Integer level = 0;  //0 Low 1 Medium 2 HIGH;

    private Integer status = 0; // 0 未开始 1 扫描中 2 已结束

    private Date createTime = new Date();
    @Column(columnDefinition = "LONGVARCHAR")
    private String baseInfo = "";
    @Column(columnDefinition = "LONGVARCHAR")
    private String proxy = "";

}