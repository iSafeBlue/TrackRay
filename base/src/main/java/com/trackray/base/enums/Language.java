package com.trackray.base.enums;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public enum Language {
    PHP("php"),
    JAVA("jsp"),
    NET("asp"),
    OTHER;


    private String suffix;
    Language(){}
    Language(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
