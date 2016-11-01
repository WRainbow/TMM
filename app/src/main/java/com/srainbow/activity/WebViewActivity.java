package com.srainbow.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.util.NetWorkUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{

    private String URL;
    private String Type;

    @Bind(R.id.webview_showdetail_wv)public WebView mWvShowDetail;
    @Bind(R.id.webview_title_tv)public TextView mTvShowTitle;
    @Bind(R.id.webview_backicon_iv)public ImageView mIvBackIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initViews();
        initData();
        mTvShowTitle.setText(Type);
        onLoadUrl();
    }

    public void  initViews(){
        mIvBackIcon.setOnClickListener(this);
    }

    public void initData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        URL=bundle.getString("url");
        Type=bundle.getString("type");
    }

//    public void netWorkState(){
//        if(NetWorkUtil.isNetworkConnected(this)){
//            onLoadUrl();
//        }else{
//            mLlayoutLoadFailed.setVisibility(View.VISIBLE);
//        }
//    }

    public void onLoadUrl(){
        webViewSetting();
        mWvShowDetail.loadUrl(URL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.webview_backicon_iv:
                WebViewActivity.this.finish();
                break;
        }
    }

    public void webViewSetting(){
        WebSettings webSettings=mWvShowDetail.getSettings();
        mWvShowDetail.requestFocusFromTouch();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //支持js打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持缩放
        webSettings.setSupportZoom(true);
    }

    @Override
    protected void onDestroy(){
//        if(mWvShowDetail!=null){
//            ((ViewGroup)mWvShowDetail.getParent()).removeView(mWvShowDetail);
//            mWvShowDetail.destroy();
//            mWvShowDetail=null;
//        }
        super.onDestroy();
    }

}
