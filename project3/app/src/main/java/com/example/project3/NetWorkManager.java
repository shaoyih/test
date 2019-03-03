package com.example.project3;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;


public class NetWorkManager {

    private static NetWorkManager instance=null;
    RequestQueue queue;

    private NetWorkManager(){
        NukeSSLCerts.nuke();
    }

    static NetWorkManager sharedManager(Context ctx){
        if (instance==null){

            instance= new NetWorkManager();
            instance.queue=Volley.newRequestQueue(ctx.getApplicationContext());
            CookieHandler.setDefault(new CookieManager());
        }
        return instance;


    }
}
