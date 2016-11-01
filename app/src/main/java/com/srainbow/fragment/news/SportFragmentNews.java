package com.srainbow.fragment.news;

public class SportFragmentNews extends NewsBaseFragment {

    public SportFragmentNews() {
        setTypeData("tiyu","体\t\t育","体育");
    }

    public static SportFragmentNews newInstance() {
        SportFragmentNews fragment = new SportFragmentNews();
        return fragment;
    }

}
