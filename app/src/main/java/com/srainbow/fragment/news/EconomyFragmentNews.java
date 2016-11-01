package com.srainbow.fragment.news;

public class EconomyFragmentNews extends NewsBaseFragment {

    public EconomyFragmentNews() {
        setTypeData("caijing","财\t\t经","财经");
    }

    public static EconomyFragmentNews newInstance() {
        EconomyFragmentNews fragment = new EconomyFragmentNews();
        return fragment;
    }

}
