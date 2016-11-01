package com.srainbow.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.srainbow.activity.R;
import com.srainbow.custom.transform.GlideRoundTransform;
import com.srainbow.fragment.smile.FunnyPicFragment;
import com.srainbow.retrofit_rxjava.basedata.FunnyPicDetail;
import com.srainbow.retrofit_rxjava.basedata.JokeDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class FunnyPicRecyclerViewAdapter extends RecyclerView.Adapter<FunnyPicRecyclerViewAdapter.CustomViewHolder>
        implements View.OnClickListener{
    private Context mContext;
    private List<FunnyPicDetail> funnyPicDetailList;
    private OnRecyclerViewItemClickListener mRecyclerViewItemListener;
    private String PictureType;
    private String loadUrl;
    private String loadTitle;

    public FunnyPicRecyclerViewAdapter(Context context, List<FunnyPicDetail> details){
        mContext=context;
        this.funnyPicDetailList=details;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_smile_funnypic_layout,parent,false);
//        view.setOnClickListener(this);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final String url=funnyPicDetailList.get(position).url;
        holder.mTvShowTitle.setText(funnyPicDetailList.get(position).content);
        if(MimeTypeMap.getFileExtensionFromUrl(url).equals("gif")){
            Picasso.with(mContext).load(url).error(R.drawable.ic_loadfailed_with_words).placeholder(R.drawable.ic_smile).into(holder.mIvShowPicture);
            holder.mIvStartPlay.setVisibility(View.VISIBLE);
        }else{
            Picasso.with(mContext).load(url).error(R.drawable.ic_loadfailed_with_words).placeholder(R.drawable.ic_smile).into(holder.mIvShowPicture);
        }
        holder.mIvShowPicture.setTag(url);
        holder.mIvShowPicture.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return funnyPicDetailList==null?0:this.funnyPicDetailList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mRecyclerViewItemListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(mRecyclerViewItemListener!=null){
            mRecyclerViewItemListener.onItemClick(v,v.getTag().toString());
        }
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.funnypic_title_tv)public TextView mTvShowTitle;
        @Bind(R.id.funnypic_picture_iv)public ImageView mIvShowPicture;
        @Bind(R.id.funnypic_playicon_iv)public ImageView mIvStartPlay;
        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View v,String url);
    }
}
