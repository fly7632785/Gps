package com.jafir.gps.model;

/**
 * created by jafir on 2020-05-28
 */
public class ResultModel {
    /**
     * code : 200
     * message : success
     * userId : 1321702401113
     * uploadSpace : 600
     */

    private String code;
    private String message;
    private String userId;
    private String uploadSpace;

    public ResultModel(String uploadSpace) {
        this.uploadSpace = uploadSpace;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUploadSpace() {
        return uploadSpace;
    }

    public void setUploadSpace(String uploadSpace) {
        this.uploadSpace = uploadSpace;
    }
}
