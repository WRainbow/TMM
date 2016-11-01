package com.srainbow.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srainbow.activity.R;
import com.srainbow.fragment.smile.JokeFragment;
import com.srainbow.fragment.smile.FunnyPicFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SmileFragment extends Fragment implements View.OnClickListener{

    private FragmentTransaction mTransactioin;
    private FragmentManager mFragmentManager;
    private final int jokeNum=1;
    private final int pictureNum=2;
    private JokeFragment mJokeFragment;
    private FunnyPicFragment mFunnyPicFragment;

    @Bind(R.id.smile_joke_tv)public TextView mTvJokePage;
    @Bind(R.id.smile_picture_tv)public TextView mTvPicturePage;

    public SmileFragment() {

    }

    public static SmileFragment newInstance() {
        SmileFragment fragment = new SmileFragment();
        Log.e("newInstance","smile");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_smile, container, false);
        ButterKnife.bind(this,view);
        initFragment();
        mTvJokePage.setOnClickListener(this);
        mTvPicturePage.setOnClickListener(this);
        return view;
    }

    public void initFragment(){
        mFragmentManager=getChildFragmentManager();
        mTvJokePage.setSelected(true);
        showFragment(jokeNum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smile_joke_tv:
                mTvJokePage.setBackgroundResource(R.drawable.textview_style_selected);
                mTvPicturePage.setBackgroundResource(R.drawable.textview_style_select_pre);
                showFragment(jokeNum);
                Log.e("smile","joke");
                break;
            case R.id.smile_picture_tv:
                mTvJokePage.setBackgroundResource(R.drawable.textview_style_select_pre);
                mTvPicturePage.setBackgroundResource(R.drawable.textview_style_selected);
                showFragment(pictureNum);
                Log.e("smile","picture");
                break;
        }
    }

    public void showFragment(int index){
        mTransactioin=mFragmentManager.beginTransaction();
        hideFragment();
        switch (index){
            case jokeNum:
                if(mJokeFragment==null){
                    mJokeFragment=JokeFragment.newInstance();
                    mTransactioin.add(R.id.smile_showfragment_llayout,mJokeFragment);
                    Log.e("show","joke");
                }else{
                    mTransactioin.show(mJokeFragment);
                    Log.e("show2","joke");
                }
                break;
            case pictureNum:
                if(mFunnyPicFragment==null){
                    mFunnyPicFragment= FunnyPicFragment.newInstance();
                    mTransactioin.add(R.id.smile_showfragment_llayout,mFunnyPicFragment);
                    Log.e("show","picture");
                }else{
                    mTransactioin.show(mFunnyPicFragment);
                    Log.e("show2","picture");
                }
                break;
        }
        mTransactioin.commit();
    }

    public void hideFragment(){
        if(mJokeFragment!=null){
            mTransactioin.hide(mJokeFragment);
            Log.e("hide","joke");
        }
        if(mFunnyPicFragment!=null){
            mTransactioin.hide(mFunnyPicFragment);
            Log.e("hide","picture");
        }
    }
}
