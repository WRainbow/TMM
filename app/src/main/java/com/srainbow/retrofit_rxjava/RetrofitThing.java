package com.srainbow.retrofit_rxjava;

import com.srainbow.constant.Constant;
import com.srainbow.custom.SubscriberByTag;
import com.srainbow.retrofit_rxjava.api.RequestApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SRainbow on 2016/9/29.
 */
public class RetrofitThing {
    public static RequestApi requestApi;

    public RequestApi getRequestApi(){
        if(requestApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(Constant.URL_VJUHE)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            requestApi=retrofit.create(RequestApi.class);
        }
        return requestApi;
    }

    public void onTouTiaoResponse(String type, SubscriberByTag subscriber){
        getRequestApi().getTouTiaoData(type,Constant.APPKEY_TOUTIAO)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onJokeResponse(SubscriberByTag subscriber){
        getRequestApi().getJokeData(Constant.APPKEY_XIAOHUA)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onFunnyPicResponse(SubscriberByTag subscriber){
        getRequestApi().getFunnyPicData(Constant.APPKEY_XIAOHUA)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RetrofitThing INSTANCE = new RetrofitThing();
    }

    //获取单例
    public static RetrofitThing getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
