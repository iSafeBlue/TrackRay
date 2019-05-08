package com.trackray.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class SenseInfo {

    @JSONField(name="特殊数字")
    private List<String> num = new ArrayList<>();
    @JSONField(name="邮箱")
    private List<String> email = new ArrayList<>();
    @JSONField(name="手机号")
    private List<String> tel = new ArrayList<>();
    @JSONField(name="其他信息")
    private Map<String,Object> other = new HashMap<>();

}
