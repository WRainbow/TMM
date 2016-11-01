package com.srainbow.fragment.news;

public class DomesticFragmentNews extends NewsBaseFragment {

    public DomesticFragmentNews() {
        setTypeData("guonei","国\t\t内","国内");
    }

    public static DomesticFragmentNews newInstance() {
        DomesticFragmentNews fragment = new DomesticFragmentNews();
        return fragment;
    }

}
