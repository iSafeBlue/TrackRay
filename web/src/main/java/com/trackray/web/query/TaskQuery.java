package com.trackray.web.query;

import com.trackray.base.bean.Rule;
import lombok.Data;

@Data
public class TaskQuery {

    private String name = "";
    private String target = "";
    private int thread = 0;
    private int spiderMax = 0 ;
    private int spiderDeep = 0;
    private int timeMax = 0;
    private String cookie = "";
    private String proxy = "";
    private Rule rule = new Rule();

}
