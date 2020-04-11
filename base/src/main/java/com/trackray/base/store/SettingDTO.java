package com.trackray.base.store;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/4 15:23
 */
@Data
@Entity
public class SettingDTO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id; //数据库ID

    private String pluginKey = ""; //插件唯一标识
    private String formName = ""; //配置名
    private String formValue = ""; //配置值


}
