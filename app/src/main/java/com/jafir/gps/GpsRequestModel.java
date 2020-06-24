package com.jafir.gps;

/**
 * created by jafir on 2020-06-19
 */
public class GpsRequestModel {
    /**
     * imei : 3593332170201113
     * data : {"latitude":"31.163559","longitude":"121.363268"}
     */

    private String imei;
    private DataBean data;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        public DataBean(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * latitude : 31.163559
         * longitude : 121.363268
         */

        private String latitude;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
