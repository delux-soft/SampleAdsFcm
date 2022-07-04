package com.example.sampleadsfcm;

import android.app.Application;

import com.example.sampleadsfcm.ads.AppOpenManager;


public class ZonalApp extends Application {
    private static final String TAG = "zonalApp";

    private AppOpenManager appOpenManager;
    private static ZonalApp mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;


    }

    public void initAppOpenAd(){
        if (appOpenManager == null){
            appOpenManager = new AppOpenManager(this);
        }
    }

    public void setShowAd(boolean showAd){
        appOpenManager.setShowAd(showAd);
    }


    public static ZonalApp getInstance(){
        return mInstance;
    }


}
