package com.trackray.scanner.bean;

public class Rule {

    public boolean crawler = true;
    public boolean sense = true;
    public boolean port = true;
    public boolean finger = true;
    public boolean childdomain = true;
    public boolean fuzzdir = true;
    public boolean attack = true;

    public boolean isCrawler() {
        return crawler;
    }

    public void setCrawler(boolean crawler) {
        this.crawler = crawler;
    }

    public boolean isSense() {
        return sense;
    }

    public void setSense(boolean sense) {
        this.sense = sense;
    }

    public boolean isPort() {
        return port;
    }

    public void setPort(boolean port) {
        this.port = port;
    }

    public boolean isFinger() {
        return finger;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    public boolean isChilddomain() {
        return childdomain;
    }

    public void setChilddomain(boolean childdomain) {
        this.childdomain = childdomain;
    }

    public boolean isFuzzdir() {
        return fuzzdir;
    }

    public void setFuzzdir(boolean fuzzdir) {
        this.fuzzdir = fuzzdir;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }
}
