package com.srainbow.fragment.news;

public class InternationalFragmentNews extends NewsBaseFragment {

    public InternationalFragmentNews() {
        setTypeData("guoji","国\t\t际","国际");
    }

    public static InternationalFragmentNews newInstance() {
        InternationalFragmentNews fragment = new InternationalFragmentNews();
        return fragment;
    }

}
