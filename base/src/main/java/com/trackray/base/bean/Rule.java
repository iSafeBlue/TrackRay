package com.trackray.base.bean;

import lombok.Data;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class Rule {

    public boolean crawler = false;
    public boolean sense = false;
    public boolean port = false;
    public boolean finger = false;
    public boolean childdomain = false;
    public boolean fuzzdir = false;
    public boolean attack = false;
    public boolean thorough = false;
}
