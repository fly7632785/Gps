package com.jafir.gps;

/**
 * created by jafir on 2020-05-20
 */
public class LoginResult {

    /**
     * code : 200
     * message : success
     * data : {"user_id":1,"nickname":"手持1"}
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
