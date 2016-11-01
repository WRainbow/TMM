package com.srainbow.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.srainbow.activity.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SRainbow on 2016/10/21.
 */
public class ImageUtil {

    public static Bitmap createThumbFromUir(ContentResolver res, Uri albumUri,Bitmap defaultBitmap) {
        InputStream in = null;
        Bitmap bmp = null;
        try {
            in = res.openInputStream(albumUri);
            BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
            if (in != null) {
                bmp = BitmapFactory.decodeStream(in, null, sBitmapOptions);
                in.close();
            }

        } catch (IOException e){
            Log.e("error",e.getMessage());
        }
        if(bmp!=null){
            return bmp;
        }else{
            Log.e("null","defaultBitmap");
            return defaultBitmap;
        }
    }

    public static void imageViewByThumbFromUir(ContentResolver res, Uri albumUri, ImageView imageView) {
        InputStream in = null;
        Bitmap bmp = null;
        try {
            in = res.openInputStream(albumUri);
            BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
            if (in != null) {
                bmp = BitmapFactory.decodeStream(in, null, sBitmapOptions);
                in.close();
            }

        } catch (IOException e){
            Log.e("error",e.getMessage());
        }
        if(bmp!=null){
            imageView.setImageBitmap(bmp);
        }else{
            Log.e("null","defaultBitmap");
            imageView.setImageResource(R.drawable.ic_mp3_default);
        }
    }
}
