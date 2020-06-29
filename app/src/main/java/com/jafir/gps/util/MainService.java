package com.jafir.gps.util;

import com.jafir.gps.model.LoginResult;
import com.jafir.gps.model.RequestModel;
import com.jafir.gps.model.UploadResult;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * created by jafir on 2020-05-20
 */
public interface MainService {

    @POST("/login")
    @FormUrlEncoded
    Single<LoginResult> login(@Field("username") String username, @Field("password") String password);


    @POST("/gps")
    Single<UploadResult> gps(@Header ("token")String token, @Body RequestModel model);
}
