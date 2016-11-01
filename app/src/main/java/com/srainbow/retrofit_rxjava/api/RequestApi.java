package com.srainbow.retrofit_rxjava.api;

import com.srainbow.retrofit_rxjava.basedata.FunnyPicData;
import com.srainbow.retrofit_rxjava.basedata.FunnyPicDetail;
import com.srainbow.retrofit_rxjava.basedata.JokeData;
import com.srainbow.retrofit_rxjava.basedata.TouTiaoData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by SRainbow on 2016/9/29.
 */
public interface RequestApi {
    @GET("toutiao/index")
    Observable<TouTiaoData> getTouTiaoData(@Query("type")String type,@Query("key")String key);
    @GET("joke/randJoke.php?type=")
    Observable<JokeData> getJokeData(@Query("key")String key);
    @GET("joke/randJoke.php?type=pic")
    Observable<FunnyPicData> getFunnyPicData(@Query("key")String key);
}
