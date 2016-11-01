package com.srainbow.fragment.drawer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srainbow.activity.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncourageEachOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncourageEachOtherFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.encourage_eachother_3_tv)public TextView mTvEncourageEachOther;
    @Bind(R.id.encourage_backicon_iv)public ImageView mIvBackIcon;
    public EncourageEachOtherFragment() {

    }

    public static EncourageEachOtherFragment newInstance() {
        return new EncourageEachOtherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_encourage_eachother, container, false);
        ButterKnife.bind(this,view);
        try {
            SpannableString spannableString = new SpannableString(getString(R.string.encourage_eachother_3));
            spannableString.setSpan(new ForegroundColorSpan(Color.rgb(207, 54, 54)), 32, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvEncourageEachOther.setText(spannableString);
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        mIvBackIcon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.encourage_backicon_iv:
                getActivity().finish();
                break;
        }
    }
}
