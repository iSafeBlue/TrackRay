package com.trackray.web.module;

import net.sf.json.JSONObject;

import java.util.List;

public class TaskData<E> {

    private String task;
    private String data;
    private List<E> vulns;

    public TaskData() {
    }

    public TaskData(String task, String data, List<E> vulns) {
        this.task = task;
        this.data = data;
        this.vulns = vulns;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<E> getVulns() {
        return vulns;
    }

    public void setVulns(List<E> vulns) {
        this.vulns = vulns;
    }
}
