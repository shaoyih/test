package com.android.fablix;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class Pop extends Activity{
    protected   void onCreate(Bundle bd){
        super.onCreate(bd);
        setContentView(R.layout.pop_window);
        DisplayMetrics met= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(met);

        int w=(int) (met.widthPixels*0.85);
        int h=(int)(met.heightPixels*0.7);

        getWindow().setLayout(w,h);

    }
}
