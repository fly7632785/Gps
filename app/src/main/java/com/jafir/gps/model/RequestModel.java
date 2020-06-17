package com.jafir.gps.model;

/**
 * created by jafir on 2020-05-28
 */
public class RequestModel
{
    private Double lat;
    private Double lng;
    private Long time;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
