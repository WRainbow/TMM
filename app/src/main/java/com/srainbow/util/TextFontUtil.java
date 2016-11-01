package com.srainbow.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by SRainbow on 2016/10/9.
 */
public class TextFontUtil {
    private Context mContext;

    public TextFontUtil(Context context){
        super();
        this.mContext=context;
    }
    public Typeface setTextFont(String fontName){
        return Typeface.createFromAsset(mContext.getAssets(),"fonts/"+fontName+".ttf");
    }
}
