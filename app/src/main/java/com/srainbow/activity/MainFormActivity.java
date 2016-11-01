package com.srainbow.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.srainbow.constant.Constant;
import com.srainbow.fragment.MusicFragment;
import com.srainbow.fragment.NewsFragment;
import com.srainbow.fragment.SmileFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainFormActivity extends BaseActivity implements View.OnClickListener{

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private NewsFragment mNewsFragment;
    private SmileFragment mSmileFragment;
    private MusicFragment mMusicFragment;
    private final int newsNum=1;
    private final int smileNum=2;
    private final int musicNum=3;
    private boolean isExist;

    @Bind(R.id.main_drawerlayout_dlayout)public DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar_mulu_iv)public ImageView mIvMulu;
    @Bind(R.id.toolbar_llayout_news_iv)public ImageView mIvNews;
    @Bind(R.id.toolbar_llayout_smile_iv)public ImageView mIvSmile;
    @Bind(R.id.toolbar_llayout_music_iv)public ImageView mIvMusic;
    @Bind(R.id.main_drawer)public LinearLayout mLlayoutDrawer;
    @Bind(R.id.main_drawer_introduce_tv)public TextView mTvIntroduce;
    @Bind(R.id.main_drawer_encourage_eachother_tv)public TextView mTvEncourageEachOther;
    @Bind(R.id.main_drawer_about_tv)public TextView mTvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainform);
        ButterKnife.bind(this);
        initViews();
    }

    public void initViews(){
        isExist=false;
        mFragmentManager=getSupportFragmentManager();
        showFragment(newsNum);

        mIvMulu.setOnClickListener(this);
        mIvNews.setOnClickListener(this);
        mIvSmile.setOnClickListener(this);
        mIvMusic.setOnClickListener(this);
        mLlayoutDrawer.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
        mTvEncourageEachOther.setOnClickListener(this);
        mTvIntroduce.setOnClickListener(this);
        mTvEncourageEachOther.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_mulu_iv:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.toolbar_llayout_news_iv:
                mIvNews.setImageResource(R.drawable.ic_news_changed);
                mIvSmile.setImageResource(R.drawable.ic_smile);
                mIvMusic.setImageResource(R.drawable.ic_music);
                showFragment(newsNum);
                break;
            case R.id.toolbar_llayout_smile_iv:
                mIvNews.setImageResource(R.drawable.ic_news);
                mIvSmile.setImageResource(R.drawable.ic_smile_changed);
                mIvMusic.setImageResource(R.drawable.ic_music);
                showFragment(smileNum);
                break;
            case R.id.toolbar_llayout_music_iv:
                mIvNews.setImageResource(R.drawable.ic_news);
                mIvSmile.setImageResource(R.drawable.ic_smile);
                mIvMusic.setImageResource(R.drawable.ic_music_changed);
                showFragment(musicNum);
                break;
            case R.id.main_drawer_introduce_tv:
                Intent intentFromIntroduce=new Intent(MainFormActivity.this,SinglePageActivity.class);
                intentFromIntroduce.putExtra("showPageNum", Constant.ONE);
                startActivity(intentFromIntroduce);
                break;
            case R.id.main_drawer_encourage_eachother_tv:
                Intent intentFromEncourage=new Intent(MainFormActivity.this,SinglePageActivity.class);
                intentFromEncourage.putExtra("showPageNum", Constant.TWO);
                startActivity(intentFromEncourage);
                break;
            case R.id.main_drawer_about_tv:
                Intent intentFromAbout=new Intent(MainFormActivity.this,SinglePageActivity.class);
                intentFromAbout.putExtra("showPageNum", Constant.THREE);
                startActivity(intentFromAbout);
                break;
        }
    }

    public void initShowFragment(){
        mTransaction =mFragmentManager.beginTransaction();
        mTransaction.add(R.id.main_showfragment_llayout,mNewsFragment);
        mTransaction.add(R.id.main_showfragment_llayout,mSmileFragment);
        mTransaction.add(R.id.main_showfragment_llayout,mMusicFragment);
        mTransaction.commit();
    }

    public void showFragment(int index){
        mTransaction =mFragmentManager.beginTransaction();
        hideFragment();
        switch (index){
            case newsNum:
                if(mNewsFragment==null){
                    mNewsFragment=NewsFragment.newInstance();
                    mTransaction.add(R.id.main_showfragment_llayout,mNewsFragment);
                    Log.e("show1","news");
                }else{
                    mTransaction.show(mNewsFragment);
                    Log.e("show2","news");
                }
                break;
            case smileNum:
                if(mSmileFragment==null){
                    mSmileFragment=SmileFragment.newInstance();
                    mTransaction.add(R.id.main_showfragment_llayout,mSmileFragment);
                    Log.e("show1","smile");
                }else{
                    mTransaction.show(mSmileFragment);
                    Log.e("show2","smile");
                }
                break;
            case musicNum:
                if(mMusicFragment==null){
                    mMusicFragment=MusicFragment.newInstance();
                    mTransaction.add(R.id.main_showfragment_llayout,mMusicFragment);
                    Log.e("show1","music");
                }else{
                    mTransaction.show(mMusicFragment);
                    Log.e("show2","music");
                }
                break;
        }
        mTransaction.commit();
    }

    public void hideFragment(){
        if(mNewsFragment!=null){
            mTransaction.hide(mNewsFragment);
            Log.e("hide","news");
        }
        if(mSmileFragment!=null){
            mTransaction.hide(mSmileFragment);
            Log.e("hide","smile");
        }
        if(mMusicFragment!=null){
            mTransaction.hide(mMusicFragment);
            Log.e("hide","music");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }else if(!isExist){
                isExist=true;
                showMessageByString("再按一下退出程序");
                mHandler.sendEmptyMessageDelayed(0,2000);
            }else{
                cancelToast();
                MainFormActivity.this.finish();
            }
        }
        return false;
    }

    Handler  mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            isExist=false;
        }
    };

}

