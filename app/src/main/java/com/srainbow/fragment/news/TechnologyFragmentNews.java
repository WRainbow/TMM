package com.srainbow.fragment.news;

public class TechnologyFragmentNews extends NewsBaseFragment {

    public TechnologyFragmentNews() {
        setTypeData("keji","科\t\t技","科技");
    }

    public static TechnologyFragmentNews newInstance() {
        TechnologyFragmentNews fragment = new TechnologyFragmentNews();
        return fragment;
    }

}
