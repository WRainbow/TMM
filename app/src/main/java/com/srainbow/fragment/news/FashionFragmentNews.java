package com.srainbow.fragment.news;

public class FashionFragmentNews extends NewsBaseFragment {

    public FashionFragmentNews() {
        setTypeData("shishang","时\t\t尚","时尚");
    }

    public static FashionFragmentNews newInstance() {
        FashionFragmentNews fragment = new FashionFragmentNews();
        return fragment;
    }

}
