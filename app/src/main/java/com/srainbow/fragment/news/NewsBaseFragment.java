package com.srainbow.fragment.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.activity.R;
import com.srainbow.activity.WebViewActivity;
import com.srainbow.custom.SubscriberByTag;
import com.srainbow.custom.adapter.NewsRecyclerViewAdapter;
import com.srainbow.retrofit_rxjava.RetrofitThing;
import com.srainbow.retrofit_rxjava.basedata.TouTiaoData;
import com.srainbow.retrofit_rxjava.basedata.TouTiaoDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class NewsBaseFragment extends Fragment implements NewsRecyclerViewAdapter.OnRecyclerViewItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,SubscriberByTag.onSubscriberByTagListener{

    private static Context mContext;
    private boolean isLoadData=false;
    private boolean isVisiable=false;
    private boolean isCreateView=false;
    private String mType_EN;
    private String mType_CN;
    private String mType_CN_noTab;
    private NewsRecyclerViewAdapter mRecyclerViewAdapter;
    private List<TouTiaoDetail>touTiaoDetails;

    @Bind(R.id.onlyloadpage_recyclerview_rv)public RecyclerView mRecyclerView;
    @Bind(R.id.onlyloadpage_swiperefreshlayout_slayout)public SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.onlyloadpage_progressbar_pb)public ProgressBar mProgressBar;
    @Bind(R.id.onlyloadpage_loadfailed_llayout)public LinearLayout mLlayoutLoadFailed;
    @Bind(R.id.onlyloadpage_loadover_llayout)public LinearLayout mLlayoutLoadOver;
    @Bind(R.id.onlyloadpage_loadover_tv)public TextView mTvLoadOver;
    public NewsBaseFragment() {

    }

    public void setTypeData(String type_en,String type_cn,String type_cn_notab){
        this.mType_EN=type_en;
        this.mType_CN=type_cn;
        this.mType_CN_noTab=type_cn_notab;
    }

    public static NewsBaseFragment newInstance() {
        NewsBaseFragment fragment = new NewsBaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate",mType_EN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_onlyloadpagebyrecyclerview, container, false);
        ButterKnife.bind(this,view);
        Log.e("onCreateView",mType_EN);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLlayoutLoadFailed.setOnClickListener(this);
        //先设置一个空的adapter
        mRecyclerViewAdapter=new NewsRecyclerViewAdapter(getActivity(),touTiaoDetails);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //RecyclerView.OnscrollListener()方法是一个抽象类，并不是方法，所以不能用implement而是extend，
        // 这里继承了Fragment，所以就直接在这写了
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    mTvLoadOver.setText(String.format(getResources().getString(R.string.newsfornowover),mType_CN_noTab));
                    mLlayoutLoadOver.setVisibility(View.VISIBLE);
                }else{
                    mLlayoutLoadOver.setVisibility(View.GONE);
                }
            }
        });
        isCreateView=true;
        lazyLoad();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            Log.e(mType_EN,"isvisiable");
            isVisiable=true;
            lazyLoad();
        }else{

        }
    }

    public void lazyLoad(){
        if(isVisiable&&isCreateView){
            mProgressBar.setVisibility(View.VISIBLE);
            if(!isLoadData){
                Log.e(mType_EN,"未加载");
                RetrofitThing.getInstance().onTouTiaoResponse(mType_EN, new SubscriberByTag("lazyLoad",NewsBaseFragment.this));
                Log.e(mType_EN,"加载完毕");
            }else{
                mProgressBar.setVisibility(View.GONE);
//                Log.e(mType_EN,"已加载");
            }
        }

    }

    public void onRefreshing(){
        mLlayoutLoadFailed.setVisibility(View.GONE);
        RetrofitThing.getInstance().onTouTiaoResponse(mType_EN, new SubscriberByTag("onRefreshing",NewsBaseFragment.this));
    }

    @Override
    public void onItemClick(View v, String data) {
        Intent intent=new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url",data);
        intent.putExtra("type",mType_CN);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onlyloadpage_loadfailed_llayout:
                mLlayoutLoadFailed.setVisibility(View.GONE);
                lazyLoad();
                break;
        }
    }

    @Override
    public void onRefresh() {
        onRefreshing();
    }

    @Override
    public void onCompleted(String tag) {
        switch (tag){
            case "lazyLoad":
                isLoadData=true;
                mLlayoutLoadFailed.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "onRefreshing":
                isLoadData=true;
                mLlayoutLoadFailed.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
                Log.e("news","completed");
                break;
        }

    }

    @Override
    public void onError(String tag, Throwable e) {
        switch (tag){
            case "lazyLoad":
                isLoadData=false;
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mLlayoutLoadFailed.setVisibility(View.VISIBLE);
                Log.e(mType_EN+"Error",e.getMessage());
                break;
            case "onRefreshing":
                isLoadData=false;
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.GONE);
                mLlayoutLoadFailed.setVisibility(View.VISIBLE);
                Log.e(mType_EN+"Error",e.getMessage());
                break;
        }

    }

    @Override
    public void onNext(String tag, Object o) {
        switch (tag){
            case "lazyLoad":
                if(o==null||((TouTiaoData)o).result.data.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    touTiaoDetails=((TouTiaoData)o).result.data;
                    mRecyclerViewAdapter=new NewsRecyclerViewAdapter(getActivity(),touTiaoDetails);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerViewAdapter.setItemClickListener(NewsBaseFragment.this);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                break;
            case "onRefreshing":
                if(o==null||((TouTiaoData)o).result.data.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    touTiaoDetails=((TouTiaoData)o).result.data;
                    mRecyclerViewAdapter=new NewsRecyclerViewAdapter(getActivity(),touTiaoDetails);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerViewAdapter.setItemClickListener(NewsBaseFragment.this);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                break;
        }
    }

}
