package com.srainbow.fragment.news;


import android.util.Log;

public class SocialFragmentNews extends NewsBaseFragment {

    public SocialFragmentNews() {
        setTypeData("shehui","社\t\t会","社会");
    }

    public static SocialFragmentNews newInstance() {
        SocialFragmentNews fragment = new SocialFragmentNews();

        Log.d("newInstance","Headlines");
        return fragment;
    }
}
