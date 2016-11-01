package com.srainbow.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.srainbow.activity.R;
import com.srainbow.retrofit_rxjava.basedata.TouTiaoDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2016/9/27.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.CustomViewHolder>
            implements View.OnClickListener{
    private Context mContext;
    private List<TouTiaoDetail> touTiaoDetailList;
    private LinearLayout.LayoutParams layoutParams;
    private OnRecyclerViewItemClickListener mItemClickListener;

    public NewsRecyclerViewAdapter(Context context, List<TouTiaoDetail> details){
        mContext=context;
        this.touTiaoDetailList=details;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_news_main_layout,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Picasso.with(mContext).load(touTiaoDetailList.get(position).thumbnail_pic_s).into(holder.mIvThumbNail);
        holder.mTvTitle.setText(touTiaoDetailList.get(position).title);
        holder.mTvAuthor.setText(touTiaoDetailList.get(position).author_name);
        holder.mTvDate.setText(touTiaoDetailList.get(position).date);
        holder.mOuterLinearLayout.setTag(touTiaoDetailList.get(position).url);
        holder.mOuterLinearLayout.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return touTiaoDetailList==null?0:this.touTiaoDetailList.size();
    }

    @Override
    public void onClick(View v) {
        if(mItemClickListener!=null){
            mItemClickListener.onItemClick(v,v.getTag().toString());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mItemClickListener=listener;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.headlines_thumbnail_iv)public ImageView mIvThumbNail;
        @Bind(R.id.headlines_title_tv)public TextView mTvTitle;
        @Bind(R.id.headlines_author_tv)public TextView mTvAuthor;
        @Bind(R.id.headlines_date_tv)public TextView mTvDate;
        @Bind(R.id.headlines_outerlinearlayout_llayout)public LinearLayout mOuterLinearLayout;
        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View v, String data);
    }
}
