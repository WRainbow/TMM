package com.srainbow.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.activity.R;
import com.srainbow.constant.Constant;
import com.srainbow.custom.adapter.MusicRecyclerViewAdapter;
import com.srainbow.custom.data.MusicDetail;
import com.srainbow.service_receiver.MusicPlayService;
import com.srainbow.util.FileUtil;
import com.srainbow.util.ImageUtil;
import com.srainbow.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MusicFragment extends Fragment implements View.OnClickListener,MusicRecyclerViewAdapter.OnRecyclerViewItemClickListener{

    private int musicNumFound=0;
    private int currentPosition=0;
    private boolean isPlaying;
    private boolean isPause;
    private LinearLayoutManager mLinearLayoutManager;
    private MusicRecyclerViewAdapter mMusicRecyclerViewAdapter;
    private List<MusicDetail> musicDetailList;

    @Bind(R.id.music_recyclerview_rv)public RecyclerView mMusicRecyclerView;
    @Bind(R.id.music_musicnum_tv)public TextView mTvMusicNumber;
    @Bind(R.id.music_playcontent_songname_tv)public TextView mTvSongName;
    @Bind(R.id.music_playcontent_artistname_tv)public TextView mTvArtistName;
    @Bind(R.id.music_startplay_iv)public ImageView mIvPlayAll;
    @Bind(R.id.music_playcontent_pause_iv)public ImageView mIvPauseMusic;
    @Bind(R.id.music_playcontent_previous_iv)public ImageView mIvPreviousMusic;
    @Bind(R.id.music_playcontent_next_iv)public ImageView mIvNextMusic;
    @Bind(R.id.music_playstatus_iv)public ImageView mIvPlayStatus;
    @Bind(R.id.music_playcontent_albumic_iv)public ImageView mIvAlbumIcon;
    @Bind(R.id.music_playcontent_rlayout)public RelativeLayout mRlayoutPlayContent;
    @Bind(R.id.music_playallmusic_rlayout)public RelativeLayout mRlayoutPlayAllMusic;

    public MusicFragment() {

    }

    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
        Log.e("newInstance","music");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this,view);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        initData();
        onRegisterReceiver();
        if(musicDetailList!=null){
            musicNumFound=musicDetailList.size();
        }
        mTvMusicNumber.setText(String.format(getResources().getString(R.string.musicnum),musicNumFound));
        mIvPlayAll.setOnClickListener(this);
        mIvPauseMusic.setOnClickListener(this);
        mIvPreviousMusic.setOnClickListener(this);
        mIvNextMusic.setOnClickListener(this);
        mIvPlayStatus.setOnClickListener(this);
        mRlayoutPlayContent.setOnClickListener(this);
        mRlayoutPlayAllMusic.setOnClickListener(this);
        return view;
    }

    public void initData(){
        musicDetailList=new ArrayList<>();
        //检查权限
        if(PermissionUtil.isPermissionGranted(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }else{
            MyRunable myThread=new MyRunable();
            new Thread(myThread).start();
        }

        if(musicDetailList!=null){
            musicNumFound=musicDetailList.size();
        }
        mMusicRecyclerViewAdapter=new MusicRecyclerViewAdapter(getActivity(),musicDetailList);
        mMusicRecyclerViewAdapter.setOnItemClickListener(this);
        mMusicRecyclerView.setAdapter(mMusicRecyclerViewAdapter);
        mMusicRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    public void onRegisterReceiver(){
        MusicPlayReceiver musicPlayReceiver=new MusicPlayReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constant.CHANGE_MUSIC);
        getActivity().registerReceiver(musicPlayReceiver,filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_playcontent_rlayout:
                break;
            case R.id.music_playallmusic_rlayout:
                playAllMusic();
                break;
            case R.id.music_playcontent_pause_iv:
                continueMusic();
                break;
            case R.id.music_playcontent_previous_iv:
                previousMusic();
                break;
            case R.id.music_playcontent_next_iv:
                nextMusic();
                break;
            case R.id.music_playstatus_iv:
                Intent intentPlayStatus=new Intent(Constant.CHANGE_PALYSTATUS);
                switch (Constant.PLAY_STATUS){
                    case 1:
                        Constant.PLAY_STATUS=2;
                        mIvPlayStatus.setImageResource(R.drawable.ic_randomcircle);
                        intentPlayStatus.putExtra("playStutas",Constant.PLAY_STATUS);
                        break;
                    case 2:
                        Constant.PLAY_STATUS=3;
                        mIvPlayStatus.setImageResource(R.drawable.ic_singlecircle);
                        intentPlayStatus.putExtra("playStutas",Constant.PLAY_STATUS);
                        break;
                    case 3:
                        Constant.PLAY_STATUS=1;
                        mIvPlayStatus.setImageResource(R.drawable.ic_fullcircle);
                        intentPlayStatus.putExtra("playStutas",Constant.PLAY_STATUS);
                        break;
                }
                getActivity().sendBroadcast(intentPlayStatus);
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        currentPosition=position;
        mIvPauseMusic.setImageResource(R.drawable.ic_pause);
        Intent intent=new Intent(getActivity(), MusicPlayService.class);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("MSG",Constant.PlayerMsg.PLAY_MSG);
        getActivity().startService(intent);       //启动服务
        setPlayContent();
        isPause=false;
        isPlaying=true;
        Log.e("artist",musicDetailList.get(currentPosition).artist);
    }

    public void playAllMusic(){
        Constant.PLAY_STATUS=2;
        mIvPlayStatus.setImageResource(R.drawable.ic_randomcircle);
        Intent intentPlayStatus=new Intent(Constant.CHANGE_PALYSTATUS);
        intentPlayStatus.putExtra("playStutas",Constant.PLAY_STATUS);
        getActivity().sendBroadcast(intentPlayStatus);
        currentPosition=FileUtil.getRandomIndex(musicDetailList.size()-1);
        playMusic();
    }

    public void playMusic(){
        Log.e("song","play");
        mIvPauseMusic.setImageResource(R.drawable.ic_pause);
        Intent intent=new Intent(getActivity(), MusicPlayService.class);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("MSG",Constant.PlayerMsg.PLAY_MSG);
        getActivity().startService(intent);       //启动服务
        setPlayContent();
        isPause=false;
        isPlaying=true;
    }

    public void pauseMusic(){
        Log.e("song","pause");
        mIvPauseMusic.setImageResource(R.drawable.ic_play_empty);
        Intent intent=new Intent(getActivity(), MusicPlayService.class);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("MSG",Constant.PlayerMsg.PAUSE_MSG);
        getActivity().startService(intent);       //启动服务
        isPlaying=false;
        isPause=true;
    }

    public void stopMusic(){
        Log.e("song","stop");
        Intent intent=new Intent(getActivity(), MusicPlayService.class);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("MSG",Constant.PlayerMsg.STOP_MSG);
        getActivity().startService(intent);       //启动服务

    }

    public void continueMusic(){
        Log.e("song","continue");
        mIvPauseMusic.setImageResource(R.drawable.ic_pause);
        if(isPlaying){
            pauseMusic();
        }else if(isPause){
            Intent intent=new Intent(getActivity(), MusicPlayService.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra("MSG",Constant.PlayerMsg.CONTINUE_MSG);
            getActivity().startService(intent);       //启动服务
            isPause=false;
            isPlaying=true;
        }else{
            playMusic();
        }
    }

    public void previousMusic(){
        Log.e("song","previous");
        if(Constant.PLAY_STATUS==2){
            currentPosition= FileUtil.getRandomIndex(musicDetailList.size()-1);
        }else{
            currentPosition=currentPosition-1;
        }
        if(currentPosition>=0){
            Intent intent=new Intent(getActivity(), MusicPlayService.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra("MSG",Constant.PlayerMsg.PREVIOUS_MSG);
            getActivity().startService(intent);       //启动服务
            setPlayContent();
        }else{
            currentPosition=0;
            Toast.makeText(getActivity(),"已经是第一首歌曲了",Toast.LENGTH_SHORT).show();
        }
    }

    public void nextMusic(){
        Log.e("song","next");
        if(Constant.PLAY_STATUS==2){
            currentPosition= FileUtil.getRandomIndex(musicDetailList.size()-1);
        }else{
            currentPosition=currentPosition+1;
        }
        if(currentPosition<=musicDetailList.size()-1){
            Intent intent=new Intent(getActivity(), MusicPlayService.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra("MSG",Constant.PlayerMsg.NEXT_MSG);
            getActivity().startService(intent);       //启动服务
            setPlayContent();
        }else{
            currentPosition=musicDetailList.size()-1;
            Toast.makeText(getActivity(),"已经是最后一首歌曲了",Toast.LENGTH_SHORT).show();
        }

    }

    public void setPlayContent(){
        ImageUtil.imageViewByThumbFromUir(getActivity().getContentResolver(),
                musicDetailList.get(currentPosition).albumUri,
                mIvAlbumIcon);
        mTvSongName.setText(musicDetailList.get(currentPosition).title);
        mTvArtistName.setText(musicDetailList.get(currentPosition).artist);
    }

    public class MusicPlayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Constant.CHANGE_MUSIC)){
                currentPosition=intent.getIntExtra("currentPosition",0);
                setPlayContent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyRunable myThread=new MyRunable();
                    new Thread(myThread).start();
                }
                break;
        }
    }



    Handler myHandler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            ArrayList list=bundle.getParcelableArrayList("musicList");
            if(list!=null){
                musicDetailList=(List<MusicDetail>)list.get(0);
                musicNumFound=musicDetailList.size();
                mTvMusicNumber.setText(String.format(getResources().getString(R.string.musicnum),musicNumFound));
                mMusicRecyclerViewAdapter=new MusicRecyclerViewAdapter(getActivity(),musicDetailList);
                mMusicRecyclerViewAdapter.setOnItemClickListener(MusicFragment.this);
                mMusicRecyclerViewAdapter.notifyDataSetChanged();
                mMusicRecyclerView.setAdapter(mMusicRecyclerViewAdapter);
                mMusicRecyclerView.setLayoutManager(mLinearLayoutManager);
            }
        }
    };


    class MyRunable implements Runnable{

        @Override
        public void run() {

            Message msg = new Message();
            Bundle bundle = new Bundle();// 存放数据
            ArrayList list=new ArrayList();
            List<MusicDetail> musicDetails=new ArrayList<>();
;           musicDetails=Constant.getLocalMusic(getActivity());
            list.add(musicDetails);
            bundle.putParcelableArrayList("musicList",list);
            msg.setData(bundle);
            MusicFragment.this.myHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent=new Intent(getActivity(), MusicPlayService.class);
        getActivity().stopService(intent);       //启动服务
    }
}
