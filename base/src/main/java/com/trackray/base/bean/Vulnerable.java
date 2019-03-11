package com.trackray.base.bean;

import com.trackray.base.enums.VulnLevel;
import com.trackray.base.enums.VulnType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Vulnerable {

    private String vulName ;//漏洞标题
    private String affectsUrl ;
    private Integer level;
    private Integer vulType;
    private String request;
    private String affectsDetail;
    private String recommendation;
    private String description;
    private List<String>  references;


}
