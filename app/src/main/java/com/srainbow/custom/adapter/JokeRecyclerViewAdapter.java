package com.srainbow.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srainbow.activity.R;
import com.srainbow.retrofit_rxjava.basedata.JokeDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class JokeRecyclerViewAdapter extends RecyclerView.Adapter<JokeRecyclerViewAdapter.CustomViewHolder>{
    private static Context mContext;
    private List<JokeDetail> jokeDataList;

    public JokeRecyclerViewAdapter(Context context, List<JokeDetail> details){
        mContext=context;
        this.jokeDataList=details;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_smile_joke_layout,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.mTvShowContent.setText(jokeDataList.get(position).content);
        holder.mTvShowContent.setTag(jokeDataList.get(position).hashId);
    }

    @Override
    public int getItemCount() {
        return jokeDataList==null?0:this.jokeDataList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.joke_showcontent_tv)public TextView mTvShowContent;
        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
