package com.srainbow.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SRainbow on 2016/10/17.
 */
public class FileUtil {

    public static List<File> getListFiles(Object obj) {
        Log.e("file","sacn");
        List<File>files=new ArrayList<>();
        File directory = null;
        try{
            if (obj instanceof File) {
                directory = (File) obj;
            } else {
                directory = new File(obj.toString());
            }
            if(!directory.isHidden()){
                if (directory.isFile()) {
                    files.add(directory);
                } else if (directory.isDirectory()) {
                    File[] fileArr = directory.listFiles();
                    if(fileArr!=null){
                        for (File file:fileArr) {
                            files.add(file);
                        }
                    }else{

                        Log.e("file","fileArr is null");
                    }
                }
            }else{
                Log.e("file","隐藏文件");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("fileerror",e.getMessage());
        }
        return files;
    }

    public static List<String> getPathByFileList(List<File> list){
        List<String> pathList=new ArrayList<>();
        for(File file:list){
            pathList.add(file.getAbsolutePath());
        }
        return pathList;
    }

    public static List<String> getFileNameByFileList(List<File> list){
        List<String> fileNameList=new ArrayList<>();
        for(File file:list){
            fileNameList.add(file.getName());
        }
        return fileNameList;
    }
    public static int getRandomIndex(int end) {
        return (int) (Math.random() * end);
    }
}