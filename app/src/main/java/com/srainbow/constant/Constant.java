package com.srainbow.constant;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.srainbow.custom.data.MusicDetail;
import com.srainbow.fragment.news.DomesticFragmentNews;
import com.srainbow.fragment.news.EconomyFragmentNews;
import com.srainbow.fragment.news.EntertainmentFragmentNews;
import com.srainbow.fragment.news.FashionFragmentNews;
import com.srainbow.fragment.news.HeadlinesFragmentNews;
import com.srainbow.fragment.news.InternationalFragmentNews;
import com.srainbow.fragment.news.MilitartFragmentNews;
import com.srainbow.fragment.news.SocialFragmentNews;
import com.srainbow.fragment.news.SportFragmentNews;
import com.srainbow.fragment.news.TechnologyFragmentNews;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SRainbow on 2016/9/27.
 */
public class Constant {
    public static String URL_VJUHE="http://v.juhe.cn/";

    public static String APPKEY_TOUTIAO="572869fde5d92d934844da9cf13e4f99";
    public static String APPKEY_XIAOHUA="b3c10341bc734b752fa7cb47b1fb0641";

    public static final int ONE=1;
    public static final int TWO=2;
    public static final int THREE=3;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE=1;
    public static List<MusicDetail> musicDetailList=new ArrayList<>();

    public static  int PLAY_STATUS=1;      //默认播放状态为随机播放(1:顺序、2:随机、3:单曲)
    public class PlayerMsg {
        public static final int PLAY_MSG = 1;		//播放
        public static final int PAUSE_MSG = 2;		//暂停
        public static final int STOP_MSG = 3;		//停止
        public static final int CONTINUE_MSG = 4;	//继续
        public static final int PREVIOUS_MSG = 5;	//上一首
        public static final int NEXT_MSG = 6;		//下一首
        public static final int PROGRESS_CHANGE = 7;//进度改变
        public static final int PLAYING_MSG = 8;	//正在播放
    }

    public static final String CHANGE_MUSIC="com.srainbow.action.CHANGE_MUSIC";
    public static final String CHANGE_TIME="com.srainbow.action.CHANGE_TIME";
    public static final String CHANGE_PALYSTATUS="com.srainbow.action.CHANGE_PALYSTATUS";


    public static List<String> getOptionTitles(){
        List<String>optionTitles=new ArrayList<>();
        optionTitles.add("头条");
        optionTitles.add("社会");
        optionTitles.add("国内");
        optionTitles.add("国际");
        optionTitles.add("娱乐");
        optionTitles.add("体育");
        optionTitles.add("军事");
        optionTitles.add("科技");
        optionTitles.add("财经");
        optionTitles.add("时尚");
        return optionTitles;
    }

    public static List<Fragment> getFragmentList(){
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(HeadlinesFragmentNews.newInstance());
        fragmentList.add(SocialFragmentNews.newInstance());
        fragmentList.add(DomesticFragmentNews.newInstance());
        fragmentList.add(InternationalFragmentNews.newInstance());
        fragmentList.add(EntertainmentFragmentNews.newInstance());
        fragmentList.add(SportFragmentNews.newInstance());
        fragmentList.add(MilitartFragmentNews.newInstance());
        fragmentList.add(TechnologyFragmentNews.newInstance());
        fragmentList.add(EconomyFragmentNews.newInstance());
        fragmentList.add(FashionFragmentNews.newInstance());
        return fragmentList;
    }

    public static List<MusicDetail> getLocalMusic(Context context){
        List<MusicDetail> musicDetails=new ArrayList<>();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(cursor!=null) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    MusicDetail musicDetail = new MusicDetail();
                    cursor.moveToNext();
                    String title = cursor.getString((cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
                    String artist = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));            //艺术家
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DURATION));          //时长
                    long size = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.SIZE));              //文件大小
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
                    int albumId=cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                    int isMusic = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));          //是否为音乐
                    if (isMusic != 0 && duration >= 1000 * 60*3) {     //只把时长大于2min的音乐添加到集合当中
                        Uri albumUri= ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);
                        musicDetail.setAlbumUri(albumUri);
                        musicDetail.setBaseInfo(title, artist, duration, size, path);
                        musicDetails.add(musicDetail);
                    }
                }
                Log.e("music", cursor.getCount() + "");
                Log.e("music", "isMusic有" + musicDetails.size());
                cursor.close();
            }
        }
        musicDetailList=musicDetails;
        return musicDetails;
    }


//    public static List<MusicDetail> scannFiles1(File folder) {
//        List<MusicDetail> musicDetailList=new ArrayList<>();
//        //指定正则表达式
//        Pattern mPattern = Pattern.compile("([^\\.]*)\\.([^\\.]*)");
//        // 当前目录下的所有文件
//        final String[] filenames = folder.list();
//        // 当前目录的名称
//        //final String folderName = folder.getName();
//        // 当前目录的绝对路径
//        //final String folderPath = folder.getAbsolutePath();
//        if (filenames != null) {
//            // 遍历当前目录下的所有文件
//            for (String name : filenames) {
//                File file = new File(folder, name);
//                // 如果是文件夹则继续递归当前方法
//                if (file.isDirectory()) {
//                    scannFiles(file);
//                }
//                // 如果是文件则对文件进行相关操作
//                else {
//                    Matcher matcher = mPattern.matcher(name);
//                    if (matcher.matches()) {
//                        // 文件名称
//                        String fileName = matcher.group(1);
//                        // 文件后缀
//                        String fileExtension = matcher.group(2);
//                        // 文件路径
//                        String filePath = file.getAbsolutePath();
//                        if (isMusicFile(fileExtension)) {
//                            MusicDetail musicDetail=new MusicDetail();
//                            musicDetail.setTitle(fileName);
//                            musicDetailList.add(musicDetail);
//                            System.out.println("This file is Music File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
//                        }
//                    }
//                }
//            }
//        }else{
//            Log.e("file","无文件");
//        }
//        return musicDetailList;
//    }

    /*
     判断是否为音乐文件
     @param extension 后缀名
     @return
     */
    public static boolean isMusicFile(String extension){
        if(extension==null){
            return false;
        }
        final String ext = extension.toLowerCase();
        if (ext.equals("mp3") || ext.equals("m4a") || ext.equals("wav") || ext.equals("amr") || ext.equals("awb") ||
                ext.equals("aac") || ext.equals("flac") || ext.equals("mid") || ext.equals("midi") ||
                ext.equals("xmf") || ext.equals("rtttl") || ext.equals("rtx") || ext.equals("ota") ||
                ext.equals("wma") ||ext.equals("ra") || ext.equals("mka") || ext.equals("m3u") || ext.equals("pls")) {
            return true;
        }
        return false;
    }
}
