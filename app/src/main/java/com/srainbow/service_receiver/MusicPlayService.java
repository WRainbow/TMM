package com.srainbow.service_receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.srainbow.constant.Constant;
import com.srainbow.util.FileUtil;

import java.io.IOException;

public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private int currentMusicPosition;
    private int msg;
    private boolean isPause;
    private MyReceiver myReceiver;

    public MusicPlayService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "onCreate() executed");
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        registerReceiver(myReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getExtras();
        currentMusicPosition =bundle.getInt("currentPosition");
        msg=bundle.getInt("MSG");
        switch (msg){
            case Constant.PlayerMsg.PLAY_MSG:
                playMusic(currentMusicPosition);
                break;
            case Constant.PlayerMsg.PAUSE_MSG:
                pauseMusic();
                break;
            case Constant.PlayerMsg.STOP_MSG:
                stopMusic();
                break;
            case Constant.PlayerMsg.CONTINUE_MSG:
                resume();
                break;
            case Constant.PlayerMsg.PREVIOUS_MSG:
                playMusic(currentMusicPosition);
                break;
            case Constant.PlayerMsg.NEXT_MSG:
                playMusic(currentMusicPosition);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void playMusic(int musicPosition){
        String path=Constant.musicDetailList.get(musicPosition).path;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();  //进行缓冲
            mediaPlayer.start();
//            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
        }catch (IOException e){
            Log.e("error",e.getMessage());
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    private void stopMusic(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp==mediaPlayer){
            switch (Constant.PLAY_STATUS){
                //默认顺序播放
                case 1:
                    currentMusicPosition++;
                    if(currentMusicPosition > Constant.musicDetailList.size()-1) {	//变为第一首的位置继续播放
                        currentMusicPosition = 0;
                    }
                    Intent sendIntent = new Intent(Constant.CHANGE_MUSIC);
                    sendIntent.putExtra("currentPosition", currentMusicPosition);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    playMusic(currentMusicPosition);
                    break;
                //随机播放
                case 2:
                    currentMusicPosition= FileUtil.getRandomIndex(Constant.musicDetailList.size()-1);
                    Intent intent=new Intent(Constant.CHANGE_MUSIC);
                    intent.putExtra("currentPosition",currentMusicPosition);
                    sendBroadcast(intent);
                    playMusic(currentMusicPosition);
                    break;
                //单曲循环
                case 3:
                    mediaPlayer.start();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();//不加这句会logcat出：mediaplayer went away with unhandled events
            mediaPlayer.release();
        }
    }

    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if(positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if(mediaPlayer != null) {
                    int currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(Constant.CHANGE_TIME);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
    };

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("playStutas", -1);
            switch (control) {
                case 1:
                    Constant.PLAY_STATUS = 1; // 将播放状态置为1表示：顺序播放
                    break;
                case 2:
                    Constant.PLAY_STATUS = 2;	//将播放状态置为2表示：随机播放
                    break;
                case 3:
                    Constant.PLAY_STATUS = 3;	//将播放状态置为3表示：单曲循环
                    break;
            }

        }
    }
}
