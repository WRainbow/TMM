package com.srainbow.fragment.news;

public class EntertainmentFragmentNews extends NewsBaseFragment {

    public EntertainmentFragmentNews() {
        setTypeData("yule","娱\t\t乐","娱乐");
    }

    public static EntertainmentFragmentNews newInstance() {
        EntertainmentFragmentNews fragment = new EntertainmentFragmentNews();
        return fragment;
    }

}
