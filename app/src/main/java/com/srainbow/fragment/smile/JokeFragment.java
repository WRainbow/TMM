package com.srainbow.fragment.smile;


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
import android.widget.Toast;

import com.srainbow.activity.R;
import com.srainbow.custom.SubscriberByTag;
import com.srainbow.custom.adapter.JokeRecyclerViewAdapter;
import com.srainbow.retrofit_rxjava.RetrofitThing;
import com.srainbow.retrofit_rxjava.basedata.JokeData;
import com.srainbow.retrofit_rxjava.basedata.JokeDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JokeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,
            SubscriberByTag.onSubscriberByTagListener{

    //进入界面时/刷新时是否加载过数据
    private boolean isLoadData=false;
    //fragment是否可见
    private boolean isVisiable=false;
    //是否执行了onCreateView（）方法
    private boolean isCreateView=false;
    //是否正在加载
    private boolean isLoadMoreing=false;
    //是否第一次显示“上拉加载更多”界面
    private boolean isFirstShowLoadLayout=true;
    //是否“正在加载”界面正在显示
    private boolean isLoadMoreingShow=false;
    //是否“上拉加载更多”界面正在显示
    private boolean isLoadMoreShow=false;
    //屏幕最后一个item位置
    private int lastVisibleItemPosition;
    private LinearLayoutManager mLinearLayoutManager;
    private List<JokeDetail> jokeDetailList;
    private JokeRecyclerViewAdapter mJokeRecyclerViewAdapter;

    @Bind(R.id.onlyloadpage_swiperefreshlayout_slayout)public SwipeRefreshLayout mJokeSwipeRefreshLayout;
    @Bind(R.id.onlyloadpage_recyclerview_rv)public RecyclerView mJokeRecyclerView;
    @Bind(R.id.onlyloadpage_progressbar_pb)public ProgressBar mJokeProgressBar;
    @Bind(R.id.onlyloadpage_loadfailed_llayout)public LinearLayout mJokeLoadFailed;
    @Bind(R.id.onlyloadpage_loadmore_llayout)public LinearLayout mJokeLoadMore;
    @Bind(R.id.onlyloadpage_loading_llayout)public LinearLayout mJokeLoading;
    public JokeFragment() {

    }

    public static JokeFragment newInstance() {

        return new JokeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_onlyloadpagebyrecyclerview, container, false);
        ButterKnife.bind(this,view);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        lastVisibleItemPosition=mLinearLayoutManager.findLastVisibleItemPosition();
        mJokeSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1);
        mJokeSwipeRefreshLayout.setOnRefreshListener(this);
        mJokeLoadFailed.setOnClickListener(this);
        mJokeRecyclerViewAdapter=new JokeRecyclerViewAdapter(getActivity(),jokeDetailList);
        mJokeRecyclerView.setAdapter(mJokeRecyclerViewAdapter);
        mJokeRecyclerView.setLayoutManager(mLinearLayoutManager);
        mJokeRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                    RecyclerView可以向下滚动时：
                        如果“上拉加载更多”显示过（isFirstShowLoadLayout=false，isLoadMoreShow=true）
                            下方所有提示都隐藏，并置isFirstShowLoadLayout=true；
                    RecyclerView不可以向下滚动时：
                        如果“上拉加载更多”未显示过（isFirstShowLoadLayout=true，isLoadMoreShow=false）:
                            显示“上拉加载更多”，并置isLoadMoreShow=true，isFirstShowLoadLayout=false;
                        如果“上拉加载更多”显示过（isFirstShowLoadLayout=false）:
                            如果“正在加载”未显示过（isLoadMoreingShow=false）：

                 */
                if((!recyclerView.canScrollVertically(1))&&isLoadData){
                    if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                            lastVisibleItemPosition+1 == mJokeRecyclerViewAdapter.getItemCount()){
                        if(isFirstShowLoadLayout){
                            isFirstShowLoadLayout=false;
                            mJokeLoadMore.setVisibility(View.VISIBLE);
                            isLoadMoreShow=true;
                        }else{
                            if(isLoadMoreShow){
                                mJokeLoadMore.setVisibility(View.VISIBLE);
                                isLoadMoreShow=true;
                            }
                            if(isLoadMoreingShow){
                                mJokeLoading.setVisibility(View.VISIBLE);
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
                if((!recyclerView.canScrollVertically(1))&&isLoadData){
                    if(isLoadMoreShow){
                        isFirstShowLoadLayout=true;
                        mJokeLoadMore.setVisibility(View.VISIBLE);
                    }
                    if(isLoadMoreingShow){
                        mJokeLoading.setVisibility(View.VISIBLE);
                    }
                }else if(recyclerView.canScrollVertically(1)&&isLoadData){
                    if(isLoadMoreShow){
                        isFirstShowLoadLayout=true;
                        mJokeLoadMore.setVisibility(View.GONE);
                    }
                    if(isLoadMoreingShow){
                        mJokeLoading.setVisibility(View.GONE);
                    }
                }
            }
        });
        isCreateView=true;
        lazyLoad();
        return view;
    }

    public void lazyLoad(){
        if(getUserVisibleHint()&&isCreateView){
            mJokeProgressBar.setVisibility(View.VISIBLE);
            if(!isLoadData){
                RetrofitThing.getInstance().onJokeResponse(new SubscriberByTag("lazyLoad",JokeFragment.this));
            }else{
                mJokeRecyclerViewAdapter=new JokeRecyclerViewAdapter(getActivity(),jokeDetailList);
                mJokeRecyclerViewAdapter.notifyDataSetChanged();
                mJokeRecyclerView.setAdapter(mJokeRecyclerViewAdapter);
                mJokeRecyclerView.setLayoutManager(mLinearLayoutManager);
                mJokeProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void onRefreshing(){
        mJokeLoadFailed.setVisibility(View.GONE);
        RetrofitThing.getInstance().onJokeResponse(new SubscriberByTag("onRefreshing",JokeFragment.this));
    }

    public void loadMore(){
        mJokeLoadMore.setVisibility(View.GONE);
        isLoadMoreShow=false;
        if(!isLoadMoreing){
            isLoadMoreing=true;
            mJokeLoading.setVisibility(View.VISIBLE);
            isLoadMoreingShow=true;
            RetrofitThing.getInstance().onJokeResponse(new SubscriberByTag("loadMore",JokeFragment.this));
        }else{
            Toast.makeText(getActivity(),"已经在努力加载了...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onlyloadpage_loadfailed_llayout:
                mJokeLoadFailed.setVisibility(View.GONE);
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
                mJokeProgressBar.setVisibility(View.GONE);
                mJokeRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "onRefreshing":
                isLoadData=true;
                mJokeSwipeRefreshLayout.setRefreshing(false);
                mJokeRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "loadMore":
                isLoadMoreing=false;
                isFirstShowLoadLayout=true;
                mJokeLoading.setVisibility(View.GONE);
                isLoadMoreingShow=false;
                Log.e("joke","loadmoreover");
                break;
        }

    }

    @Override
    public void onError(String tag, Throwable e) {
        Log.e("joke",e.getMessage());
        switch (tag){
            case "lazyLoad":
                isLoadData=false;
                mJokeProgressBar.setVisibility(View.GONE);
                mJokeRecyclerView.setVisibility(View.GONE);
                mJokeLoadFailed.setVisibility(View.VISIBLE);
                Log.e("joke",e.getMessage());
                break;
            case "onRefreshing":
                isLoadData=false;
                mJokeSwipeRefreshLayout.setRefreshing(false);
                mJokeRecyclerView.setVisibility(View.GONE);
                mJokeLoadFailed.setVisibility(View.VISIBLE);
                break;
            case "loadMore":
                isLoadMoreing=false;
                isFirstShowLoadLayout=true;
                mJokeLoading.setVisibility(View.GONE);
                isLoadMoreingShow=false;
                if(lastVisibleItemPosition+1==mJokeRecyclerViewAdapter.getItemCount()){
                    mJokeLoadMore.setVisibility(View.VISIBLE);
                    isLoadMoreShow=true;
                }
                Toast.makeText(getActivity(),"网络似乎开小差了...",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNext(String tag, Object o) {
        switch (tag){
            case "lazyLoad":
                if(o==null||((JokeData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    jokeDetailList=((JokeData)o).result;
                    mJokeRecyclerViewAdapter=new JokeRecyclerViewAdapter(getActivity(),jokeDetailList);
                    mJokeRecyclerViewAdapter.notifyDataSetChanged();
                    mJokeRecyclerView.setAdapter(mJokeRecyclerViewAdapter);
                    mJokeRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                break;
            case "onRefreshing":
                if(o==null||((JokeData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    List<JokeDetail> jokeDetails=((JokeData)o).result;
                    if(jokeDetailList!=null){
                        for(int i=0;i<jokeDetailList.size();i++){
                            jokeDetails.add(jokeDetailList.get(i));
                        }
                    }
                    jokeDetailList=jokeDetails;
                    mJokeRecyclerViewAdapter=new JokeRecyclerViewAdapter(getActivity(),jokeDetailList);
                    mJokeRecyclerViewAdapter.notifyDataSetChanged();
                    mJokeRecyclerView.setAdapter(mJokeRecyclerViewAdapter);
                    mJokeRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                break;
            case "loadMore":
                if(o==null||((JokeData)o).result.isEmpty()){
                    Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    List<JokeDetail> jokeDetails=((JokeData)o).result;
                    Log.e("joke",jokeDetails.size()+"");
                    for(int i=0;i<jokeDetails.size();i++){
                        jokeDetailList.add(jokeDetails.get(i));
                    }
                    Log.e("joke",jokeDetailList.size()+"");
                    mJokeRecyclerViewAdapter.notifyItemInserted(mJokeRecyclerViewAdapter.getItemCount());
                }
                break;
        }

    }

}
