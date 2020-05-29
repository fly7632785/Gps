package com.jafir.gps;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * created by jafir on 2020-05-20
 */
public interface MainService {
    @POST("/train/login.html")
    @FormUrlEncoded
    Single<LoginResult> login(@Field("username") String username, @Field("password") String password);

    @POST("/train/gps.html")
    @FormUrlEncoded
    Single<ResponseBody> gps(@Field("user_id") String userId, @Header("token") String token, @Field("lng") double lng, @Field("lat") double lat);
}
