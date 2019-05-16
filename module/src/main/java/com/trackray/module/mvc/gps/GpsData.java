package com.trackray.module.mvc.gps;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/15 14:51
 */
@Entity
@Data
public class GpsData  implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String ip = "";
    private Date date = new Date();
    private String userAgent = "";
    private String latitude = "";
    private String longitude = "";
    private String msg = "";
    private String remark = "";

}
