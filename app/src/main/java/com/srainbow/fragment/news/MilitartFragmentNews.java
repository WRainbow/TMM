package com.srainbow.fragment.news;

public class MilitartFragmentNews extends NewsBaseFragment {

    public MilitartFragmentNews() {
        setTypeData("junshi","军\t\t事","军事");
    }

    public static MilitartFragmentNews newInstance() {
        MilitartFragmentNews fragment = new MilitartFragmentNews();
        return fragment;
    }

}
