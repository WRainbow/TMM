package com.srainbow.fragment.news;

public class HeadlinesFragmentNews extends NewsBaseFragment {

    public HeadlinesFragmentNews() {
        setTypeData("top","头\t\t条","头条");
    }

    public static HeadlinesFragmentNews newInstance() {
        HeadlinesFragmentNews fragment = new HeadlinesFragmentNews();
        return fragment;
    }

}
