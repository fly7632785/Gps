package com.jafir.gps;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * created by jafir on 2020-05-20
 */
public interface MainService {

    @POST("/api/sys/login")
    @Headers("Content-Type:application/json")
    Single<LoginResultModel> login(@Body LoginRequest gson);

    @POST("/api/mobile/save")
    @Headers("Content-Type:application/json")
    Single<GpsResultModel> gps(@Header ("token")String token,@Body GpsRequestModel requestModel);
}
