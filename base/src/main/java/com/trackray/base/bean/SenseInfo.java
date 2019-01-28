package com.trackray.base.bean;

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

    private List<String> num = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> tel = new ArrayList<>();
    private Map<String,Object> other = new HashMap<>();

}
