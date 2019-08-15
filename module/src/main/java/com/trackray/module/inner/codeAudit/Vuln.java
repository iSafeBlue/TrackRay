package com.trackray.module.inner.codeAudit;

import com.trackray.module.mvc.gps.GpsData;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/14 15:10
 */
@Data
@Entity
public class Vuln implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String ruleid;
    private String name;
    @Column(columnDefinition = "LONGVARCHAR" , length = 65536)
    private String code;
    private String description;
    private String filename;
}
