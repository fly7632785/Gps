package com.jafir.gps;

/**
 * created by jafir on 2020-05-28
 */
public class RequestModel
{
    /**
     * userId : 3593332170201113
     * data : {"lat":"","lng":""}
     */

    private String userId;
    private DataBean data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lat :
         * lng :
         */

        private String lat;
        private String lng;

        public DataBean(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
