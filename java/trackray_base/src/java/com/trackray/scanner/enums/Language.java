package com.trackray.scanner.enums;

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
