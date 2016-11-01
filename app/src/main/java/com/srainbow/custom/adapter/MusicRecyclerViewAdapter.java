package com.srainbow.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srainbow.activity.R;
import com.srainbow.custom.data.MusicDetail;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2016/10/12.
 */
public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.MusicViewHolder>
        implements View.OnClickListener {

    private Context mContex;
    private List<MusicDetail> musicDetailList;
    private OnRecyclerViewItemClickListener mRecyclerViewItemListener;

    public MusicRecyclerViewAdapter(Context context,List<MusicDetail> list){
        this.mContex=context;
        this.musicDetailList=list;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_music_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        holder.mTvMusicTitle.setText(musicDetailList.get(position).title);
        holder.mTvMusicArtist.setText(musicDetailList.get(position).artist);
        holder.mTvMusicTime.setText(musicDetailList.get(position).duration);
        holder.mTvMusicSize.setText(musicDetailList.get(position).size);
        holder.mItemLayout.setOnClickListener(this);
        holder.mItemLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (musicDetailList==null?0:musicDetailList.size());
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mRecyclerViewItemListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(mRecyclerViewItemListener!=null){
            mRecyclerViewItemListener.onItemClick(v,(int)v.getTag());
        }
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.music_title_tv)public TextView mTvMusicTitle;
        @Bind(R.id.music_artist_tv)public TextView mTvMusicArtist;
        @Bind(R.id.music_time_tv)public TextView mTvMusicTime;
        @Bind(R.id.music_size_tv)public TextView mTvMusicSize;
        @Bind(R.id.music_item_llayout)public LinearLayout mItemLayout;
        public MusicViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View v,int position);
    }
}
