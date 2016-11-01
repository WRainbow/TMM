package com.srainbow.custom.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.srainbow.constant.Constant;

import java.util.List;

/**
 * Created by SRainbow on 2016/9/27.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList= Constant.getFragmentList();
        mTitles=Constant.getOptionTitles();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return (mFragmentList!=null?mFragmentList.size():0);
    }
//
//    @Override
//    public boolean isViewFromObject(View view,Object object){
//        return view==object;
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
