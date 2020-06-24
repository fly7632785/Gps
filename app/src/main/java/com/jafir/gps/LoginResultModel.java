package com.jafir.gps;

/**
 * created by jafir on 2020-06-19
 */
public class LoginResultModel {
    /**
     * code : 0
     * msg : success
     * data : {"expire":43200,"token":"eyJ0eXAiOiJKV1QiLCJsb2dUeXAiOiJVc2VyIiwiZnJvbVR5cCI6IndlYiIsInRva2VuSWQiOjI4MywiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNDEwNTg0LCJleHAiOjE1OTI0NTM3ODR9.E43JKAWvGuTJAur1k4dXFUt2Pdw93ZXI24v9VzFCGqlPrB9oICM_I_OTbcsaWGuL0EQcOb7MrmBKXPytGJeQsA"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * expire : 43200
         * token : eyJ0eXAiOiJKV1QiLCJsb2dUeXAiOiJVc2VyIiwiZnJvbVR5cCI6IndlYiIsInRva2VuSWQiOjI4MywiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNDEwNTg0LCJleHAiOjE1OTI0NTM3ODR9.E43JKAWvGuTJAur1k4dXFUt2Pdw93ZXI24v9VzFCGqlPrB9oICM_I_OTbcsaWGuL0EQcOb7MrmBKXPytGJeQsA
         */

        private int expire;
        private String token;

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
