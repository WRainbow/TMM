package com.srainbow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srainbow.activity.R;
import com.srainbow.custom.adapter.NewsRecyclerViewAdapter;
import com.srainbow.custom.adapter.ViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsFragment extends Fragment implements NewsRecyclerViewAdapter.OnRecyclerViewItemClickListener,
        ViewPager.OnPageChangeListener{

    private ViewPagerAdapter mViewPagerAdapter;

    @Bind(R.id.news_viewpager_vp)public ViewPager mViewPager;
    @Bind(R.id.news_tabpageindicator_ti)public TabPageIndicator mTabPageIndicator;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Log.e("newInstance","news");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","news");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news, container, false);
         ButterKnife.bind(this,view);
        Log.d("onCreateView","news");
        initViewPager();
        return view;
    }

    @Override
    public void onItemClick(View v, String data) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                Log.e("news","头条");
                break;
            case 1:
                Log.e("news","社会");
                break;
            case 2:
                Log.e("news","国内");
                break;
            case 3:
                Log.e("news","国际");
                break;
            case 4:
                Log.e("news","娱乐");
                break;
            case 5:
                Log.e("news","体育");
                break;
            case 6:
                Log.e("news","军事");
                break;
            case 7:
                Log.e("news","科技");
                break;
            case 8:
                Log.e("news","财经");
                break;
            case 9:
                Log.e("news","时尚");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public void initViewPager(){
        mViewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);
        mTabPageIndicator.setViewPager(mViewPager);
    }

    public void hideAllFragment(ViewPager viewPager){
        for(int i=0;i<viewPager.getChildCount();i++){

        }
    }

}
