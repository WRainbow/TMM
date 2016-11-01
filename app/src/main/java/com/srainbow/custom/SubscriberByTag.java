package com.srainbow.custom;

import rx.Subscriber;

/**
 * Created by SRainbow on 2016/10/13.
 */
public class SubscriberByTag extends Subscriber {

    private String mTag;
    private onSubscriberByTagListener mSubscriberListener;

    public SubscriberByTag(String tag,onSubscriberByTagListener listener){
        this.mTag=tag;
        this.mSubscriberListener=listener;
    }

    @Override
    public void onCompleted() {
        mSubscriberListener.onCompleted(mTag);
    }

    @Override
    public void onError(Throwable e) {
        mSubscriberListener.onError(mTag,e);
    }

    @Override
    public void onNext(Object o) {
        mSubscriberListener.onNext(mTag,o);
    }

    public interface onSubscriberByTagListener{
        void onCompleted(String tag);
        void onError(String tag,Throwable e);
        void onNext(String tag,Object o);
    }
}
