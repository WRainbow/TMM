package com.srainbow.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by SRainbow on 2016/10/19.
 */
public class PermissionUtil {

    public static boolean isPermissionGranted(Context context, String persion){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }
}
