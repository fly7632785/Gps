package com.jafir.gps.model;

/**
 * created by jafir on 2020-06-29
 */
public class UploadResult {
    /**
     * code : 200
     * data : 5000
     * msg : 请求成功
     */

    private int code;
    private int data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
