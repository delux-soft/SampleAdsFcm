package com.example.sampleadsfcm.ads;

import android.app.Activity;
import android.util.Log;

import com.example.sampleadsfcm.abstractAds.AdaptiveBanner;
import com.google.android.gms.ads.AdView;


import java.lang.ref.WeakReference;

public class AdaptiveBannerImpl extends AdaptiveBanner {
    private static final String TAG = "AdaptiveBannerImpl";
    private final AdaptiveListener listener;

    public AdaptiveBannerImpl(WeakReference<Activity> weakReference, AdaptiveListener listener) {
        super(weakReference);
        this.listener = listener;
        Log.i(TAG, "AdaptiveBannerImpl: Initialized");
    }

    @Override
    protected void adLoad(AdView adView) {
        listener.onLoad(adView);
    }

    @Override
    protected void adClosed() {
        listener.onClosed();
    }

    @Override
    protected void adFailed(String error) {
        listener.onFailed(error);
    }

    @Override
    protected void adImpression() {
        listener.adImpression();
    }

    public interface AdaptiveListener {
        void onLoad(AdView adView);
        void onClosed();
        void onFailed(String error);
        void adImpression();
    }
}
