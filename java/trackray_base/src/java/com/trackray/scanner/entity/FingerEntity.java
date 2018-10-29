package com.trackray.scanner.entity;

import com.trackray.scanner.enums.Finger;

public class FingerEntity {

    private Finger finger;

    private String name;

    public FingerEntity() {
    }

    public FingerEntity(Finger finger, String name) {
        this.finger = finger;
        this.name = name;
    }

    public Finger getFinger() {
        return finger;
    }

    public void setFinger(Finger finger) {
        this.finger = finger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
