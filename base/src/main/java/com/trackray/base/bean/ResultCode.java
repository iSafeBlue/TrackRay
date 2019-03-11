package com.trackray.base.bean;


import net.sf.json.JSONObject;

public class ResultCode {

    public final static ResultCode SUCCESS = getInstance(200,"正常");
    public final static ResultCode ERROR = getInstance(500,"异常");


    private int code;
    private String msg;
    private Object data;

    public static ResultCode getInstance(int code, String msg, Object data){
        return new ResultCode(code,msg,data);
    }

    public static ResultCode getInstance(int code, String msg){
        return new ResultCode(code,msg,null);
    }
    public static ResultCode SUCCESS(String obj){
        return getInstance(200,obj);
    }
    public static ResultCode ERROR(String obj){
        return getInstance(500,obj);
    }
    public static ResultCode WARN(String obj){
        return getInstance(400,obj);
    }
    public static ResultCode SUCCESS(Object obj){
        return getInstance(200,"正常",obj);
    }
    public static ResultCode ERROR(Object obj){
        return getInstance(500,"错误",obj);
    }
    public static ResultCode WARN(Object obj){
        return getInstance(400,"异常",obj);
    }
    private ResultCode(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON(){
        return JSONObject.fromObject(this);
    }
}
