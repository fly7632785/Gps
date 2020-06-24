package com.jafir.gps;

/**
 * created by jafir on 2020-06-19
 */
public class GpsResultModel {
    /**
     * code : 0
     * msg : success
     * data : {"reportFrequency":10}
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
         * reportFrequency : 10
         */

        private int reportFrequency;

        public int getReportFrequency() {
            return reportFrequency;
        }

        public void setReportFrequency(int reportFrequency) {
            this.reportFrequency = reportFrequency;
        }
    }
}
