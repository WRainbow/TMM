package com.srainbow.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMessageByString(String msg){
        if(mToast==null){
            mToast=Toast.makeText(BaseActivity.this,msg, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }

    public void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
        }
    }
}
