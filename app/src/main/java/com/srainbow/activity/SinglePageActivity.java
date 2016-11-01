package com.srainbow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.srainbow.constant.Constant;
import com.srainbow.fragment.drawer.AboutFragment;
import com.srainbow.fragment.drawer.EncourageEachOtherFragment;
import com.srainbow.fragment.drawer.IntroduceFragment;

public class SinglePageActivity extends BaseActivity {

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private EncourageEachOtherFragment mEncourageEachOtherFragment;
    private IntroduceFragment mIntroduceFragment;
    private AboutFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepage);
        initViews();
    }

    public void initViews(){
        mFragmentManager=getSupportFragmentManager();
        int showPageNum;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        showPageNum=bundle.getInt("showPageNum");
        switch (showPageNum){
            case Constant.ONE:
                showFragment(Constant.ONE);
                break;
            case Constant.TWO:
                showFragment(Constant.TWO);
                break;
            case Constant.THREE:
                showFragment(Constant.THREE);
                break;
        }
    }

    public void showFragment(int index){
        mTransaction=mFragmentManager.beginTransaction();
        hideFragments();
        switch (index) {
            case Constant.ONE:
                if(mIntroduceFragment==null){
                    mIntroduceFragment=IntroduceFragment.newInstance();
                    mTransaction.add(R.id.singlepage_showfragment_rlayout,mIntroduceFragment);
                }else{
                    mTransaction.show(mIntroduceFragment);
                }
                break;
            case Constant.TWO:
                if (mEncourageEachOtherFragment == null) {
                    mEncourageEachOtherFragment = EncourageEachOtherFragment.newInstance();
                    mTransaction.add(R.id.singlepage_showfragment_rlayout, mEncourageEachOtherFragment);
                } else {
                    mTransaction.show(mEncourageEachOtherFragment);
                }
                break;
            case Constant.THREE:
                if(mAboutFragment==null){
                    mAboutFragment=AboutFragment.newInstance();
                    mTransaction.add(R.id.singlepage_showfragment_rlayout,mAboutFragment);
                }else{
                    mTransaction.show(mAboutFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    public void hideFragments(){
        if(mIntroduceFragment!=null){
            mTransaction.hide(mIntroduceFragment);
        }
        if(mEncourageEachOtherFragment!=null){
            mTransaction.hide(mEncourageEachOtherFragment);
        }
        if(mAboutFragment!=null){
            mTransaction.hide(mAboutFragment);
        }
    }
}
