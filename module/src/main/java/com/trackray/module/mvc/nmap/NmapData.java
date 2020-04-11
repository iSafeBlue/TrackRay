package com.trackray.module.mvc.nmap;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/4/11 16:32
 */
@Entity
@Data
public class NmapData implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String taskid;
    private String filename;
    private String name;

}
