package com.trackray.module.mvc.mindMap;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/9/12 21:52
 */
@Entity
@Data
public class MindMapData  {
    @Id
    @GeneratedValue
    private long id;

    private String title;

    @Column(columnDefinition = "LONGVARCHAR" , length = 65536)
    private String data;
    private Date date = new Date();


}
