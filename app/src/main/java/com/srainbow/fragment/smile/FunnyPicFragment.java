package com.srainbow.fragment.smile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.srainbow.activity.R;
import com.srainbow.custom.SubscriberByTag;
import com.srainbow.custom.adapter.FunnyPicRecyclerViewAdapter;
import com.srainbow.custom.adapter.FunnyPicRecyclerViewAdapter.OnRecyclerViewItemClickListener;
import com.srainbow.custom.transform.GlideRoundTransform;
import com.srainbow.retrofit_rxjava.RetrofitThing;
import com.srainbow.retrofit_rxjava.basedata.FunnyPicData;
import com.srainbow.retrofit_rxjava.basedata.FunnyPicDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class FunnyPicFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,
            OnRecyclerViewItemClickListener,SubscriberByTag.onSubscriberByTagListener{

    private boolean isLoadData=false;
    private boolean isVisiable=false;
    private boolean isCreateView=false;
    private boolean isLoadMoreing=false;
    private boolean isFirstShowLoadLayout=true;
    private boolean isLoadMoreingShow=false;
    private boolean isLoadMoreShow=false;
    private int lastVisibleItemPosition;
    private LinearLayoutManager mLinearLayoutManager;
    private List<FunnyPicDetail> funnyPicDetailList;
    private FunnyPicRecyclerViewAdapter mFunnyPicRecyclerViewAdapter;
    private GlideRoundTransform roundTransform;

    @Bind(R.id.onlyloadpage_swiperefreshlayout_slayout)public SwipeRefreshLayout mFunnyPicSwipeRefreshLayout;
    @Bind(R.id.onlyloadpage_recyclerview_rv)public RecyclerView mFunnyPicRecyclerView;
    @Bind(R.id.onlyloadpage_progressbar_pb)public ProgressBar mFunnyPicProgressBar;
    @Bind(R.id.onlyloadpage_loadfailed_llayout)public LinearLayout mFunnyPicLoadFailed;
    @Bind(R.id.onlyloadpage_showbigpic_iv)public ImageView mFunnyPicShowBigPicture;
    @Bind(R.id.onlyloadpage_showbigpic_sv)public ScrollView mFunnyPicShowBig;
    @Bind(R.id.onlyloadpage_loadmore_llayout)public LinearLayout mFunnyPicLoadMore;
    @Bind(R.id.onlyloadpage_loading_llayout)public LinearLayout mFunnyPicLoading;
    public FunnyPicFragment() {

    }

    public static FunnyPicFragment newInstance() {

        return new FunnyPicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_onlyloadpagebyrecyclerview, container, false);
        ButterKnife.bind(this,view);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        lastVisibleItemPosition=mLinearLayoutManager.findLastVisibleItemPosition();
        mFunnyPicSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1);
        mFunnyPicSwipeRefreshLayout.setOnRefreshListener(this);
        mFunnyPicLoadFailed.setOnClickListener(this);
        mFunnyPicShowBigPicture.setOnClickListener(this);
        mFunnyPicRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)&&isLoadData){
                    if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                            lastVisibleItemPosition+1 == mFunnyPicRecyclerViewAdapter.getItemCount()){
                        if(isFirstShowLoadLayout){
                            isFirstShowLoadLayout=false;
                            mFunnyPicLoadMore.setVisibility(View.VISIBLE);
                            isLoadMoreShow=true;
                        }else{
                            if(isLoadMoreShow){
                                mFunnyPicLoadMore.setVisibility(View.VISIBLE);
                                isLoadMoreShow=true;
                            }
                            if(isLoadMoreingShow){
                                mFunnyPicLoading.setVisibility(View.VISIBLE);
                                isLoadMoreingShow=true;
                            }
                            loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if(!recyclerView.canScrollVertically(1)){
                    if(isLoadMoreShow){
                        isFirstShowLoadLayout=true;
                        mFunnyPicLoadMore.setVisibility(View.VISIBLE);
                    }
                    if(isLoadMoreingShow){
                        mFunnyPicLoading.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(isLoadMoreShow){
                        isFirstShowLoadLayout=true;
                        mFunnyPicLoadMore.setVisibility(View.GONE);
                    }
                    if(isLoadMoreingShow){
                        mFunnyPicLoading.setVisibility(View.GONE);
                    }
                }
            }
        });
        mFunnyPicRecyclerViewAdapter=new FunnyPicRecyclerViewAdapter(getActivity(),funnyPicDetailList);
        mFunnyPicRecyclerViewAdapter.setOnItemClickListener(FunnyPicFragment.this);
        mFunnyPicRecyclerView.setAdapter(mFunnyPicRecyclerViewAdapter);
        mFunnyPicRecyclerView.setLayoutManager(mLinearLayoutManager);
        isCreateView=true;
        lazyLoad();
        return view;
    }

    public void lazyLoad(){
        if(getUserVisibleHint()&&isCreateView){
            mFunnyPicProgressBar.setVisibility(View.VISIBLE);
            if(!isLoadData){
                RetrofitThing.getInstance().onFunnyPicResponse(new SubscriberByTag("lazyLoad",FunnyPicFragment.this));
            }else{
                mFunnyPicRecyclerViewAdapter=new FunnyPicRecyclerViewAdapter(getActivity(),funnyPicDetailList);
                mFunnyPicRecyclerViewAdapter.setOnItemClickListener(FunnyPicFragment.this);
                mFunnyPicRecyclerViewAdapter.notifyDataSetChanged();
                mFunnyPicRecyclerView.setAdapter(mFunnyPicRecyclerViewAdapter);
                mFunnyPicRecyclerView.setLayoutManager(mLinearLayoutManager);
                mFunnyPicProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void onRefreshing(){
        mFunnyPicLoadFailed.setVisibility(View.GONE);
        RetrofitThing.getInstance().onFunnyPicResponse(new SubscriberByTag("onRefreshing",FunnyPicFragment.this));

    }

    public void loadMore(){
        mFunnyPicLoadMore.setVisibility(View.GONE);
        isLoadMoreShow=false;
        if(!isLoadMoreing){
            isLoadMoreing=true;
            mFunnyPicLoading.setVisibility(View.VISIBLE);
            isLoadMoreingShow=true;
            RetrofitThing.getInstance().onFunnyPicResponse(new SubscriberByTag("loadMore",FunnyPicFragment.this));
        }else{
            Toast.makeText(getActivity(),"已经在努力加载了...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onlyloadpage_loadfailed_llayout:
                mFunnyPicLoadFailed.setVisibility(View.GONE);
                lazyLoad();
                break;
            case R.id.onlyloadpage_showbigpic_iv:
                mFunnyPicShowBig.setVisibility(View.GONE);
                mFunnyPicSwipeRefreshLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(View view,String url) {
        mFunnyPicSwipeRefreshLayout.setRefreshing(false);
        mFunnyPicSwipeRefreshLayout.setVisibility(View.GONE);
        roundTransform=new GlideRoundTransform(getActivity());
        if(MimeTypeMap.getFileExtensionFromUrl(url).equals("gif")){
            Glide.with(getActivity()).load(url).asGif().error(R.drawable.ic_loadfailed_with_words).placeholder(R.drawable.ic_loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(mFunnyPicShowBigPicture);
        }else{
            Glide.with(getActivity()).load(url).asBitmap().error(R.drawable.ic_loadfailed_with_words).placeholder(R.drawable.ic_loading).transform(roundTransform).diskCacheStrategy(DiskCacheStrategy.ALL).into(mFunnyPicShowBigPicture);
        }
        mFunnyPicShowBig.setVisibility(View.VISIBLE);
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
                mFunnyPicProgressBar.setVisibility(View.GONE);
                mFunnyPicRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "onRefreshing":
                isLoadData=true;
                mFunnyPicSwipeRefreshLayout.setRefreshing(false);
                mFunnyPicRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "loadMore":
                isLoadMoreing=false;
                isFirstShowLoadLayout=true;
                mFunnyPicLoading.setVisibility(View.GONE);
                isLoadMoreingShow=false;
                break;
        }

    }

    @Override
    public void onError(String tag, Throwable e) {
        Log.e("funnypic",e.getMessage());
        switch (tag){
            case "lazyLoad":
                isLoadData=false;
                mFunnyPicProgressBar.setVisibility(View.GONE);
                mFunnyPicRecyclerView.setVisibility(View.GONE);
                mFunnyPicLoadFailed.setVisibility(View.VISIBLE);
                Log.e("funnypic",e.getMessage());
                break;
            case "onRefreshing":
                isLoadData=false;
                mFunnyPicSwipeRefreshLayout.setRefreshing(false);
                mFunnyPicRecyclerView.setVisibility(View.GONE);
                mFunnyPicLoadFailed.setVisibility(View.VISIBLE);
                break;
            case "loadMore":
                isLoadMoreing=false;
                isFirstShowLoadLayout=true;
                mFunnyPicLoading.setVisibility(View.GONE);
                isLoadMoreingShow=false;
                mFunnyPicLoadMore.setVisibility(View.VISIBLE);
                isLoadMoreShow=true;
                Toast.makeText(getActivity(),"网络似乎开小差了...",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNext(String tag, Object o) {
        switch (tag){
            case "lazyLoad":
                if(o==null||((FunnyPicData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    funnyPicDetailList=((FunnyPicData)o).result;
                    mFunnyPicRecyclerViewAdapter=new FunnyPicRecyclerViewAdapter(getActivity(),funnyPicDetailList);
                    mFunnyPicRecyclerViewAdapter.setOnItemClickListener(FunnyPicFragment.this);
                    mFunnyPicRecyclerViewAdapter.notifyDataSetChanged();
                    mFunnyPicRecyclerView.setAdapter(mFunnyPicRecyclerViewAdapter);
                    mFunnyPicRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                break;
            case "onRefreshing":
                if(o==null||((FunnyPicData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    List<FunnyPicDetail> funnyPicDetails=((FunnyPicData)o).result;
                    if(funnyPicDetailList!=null){
                        for(int i=0;i<funnyPicDetailList.size();i++){
                            funnyPicDetails.add(funnyPicDetailList.get(i));
                        }
                    }
                    funnyPicDetailList=funnyPicDetails;
                    mFunnyPicRecyclerViewAdapter=new FunnyPicRecyclerViewAdapter(getActivity(),funnyPicDetailList);
                    mFunnyPicRecyclerViewAdapter.setOnItemClickListener(FunnyPicFragment.this);
                    mFunnyPicRecyclerViewAdapter.notifyDataSetChanged();
                    mFunnyPicRecyclerView.setAdapter(mFunnyPicRecyclerViewAdapter);
                    mFunnyPicRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                break;
            case "loadMore":
                if(o==null||((FunnyPicData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    List<FunnyPicDetail> funnyPicDetails=((FunnyPicData)o).result;
                    for(int i=0;i<funnyPicDetails.size();i++){
                        funnyPicDetailList.add(funnyPicDetails.get(i));
                    }
                    mFunnyPicRecyclerViewAdapter.notifyItemInserted(mFunnyPicRecyclerViewAdapter.getItemCount());
                }
                break;
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("destory","");
    }

}
