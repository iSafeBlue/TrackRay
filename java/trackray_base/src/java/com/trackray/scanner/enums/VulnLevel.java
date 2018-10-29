package com.trackray.scanner.enums;

public enum VulnLevel {
    INFO(1,"低危"),
    WARNING(2,"中危"),
    DANGER(3,"高危");

    private int level;
    private String name;

    VulnLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
