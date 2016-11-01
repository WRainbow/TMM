package com.srainbow.fragment.drawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.srainbow.activity.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IntroduceFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.introduce_backicon_iv)public ImageView mIvBackIcon;
    public IntroduceFragment() {

    }

    public static IntroduceFragment newInstance() {

        return  new IntroduceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_introduce, container, false);
        ButterKnife.bind(this,view);
        mIvBackIcon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.introduce_backicon_iv:
                getActivity().finish();
                break;
        }
    }
}
