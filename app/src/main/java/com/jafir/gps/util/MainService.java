package com.jafir.gps.util;

import com.jafir.gps.model.ResultModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * created by jafir on 2020-05-20
 */
public interface MainService {

    @POST("/train")
    Single<ResultModel> gps(@Body String gson);
}
