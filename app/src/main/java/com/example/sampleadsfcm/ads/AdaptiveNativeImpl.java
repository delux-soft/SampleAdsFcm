package com.example.sampleadsfcm.ads;

import android.app.Activity;

import com.example.sampleadsfcm.abstractAds.AdaptiveNative;
import com.google.android.gms.ads.nativead.NativeAdView;


import java.lang.ref.WeakReference;

public class AdaptiveNativeImpl extends AdaptiveNative {
    private final NativeListener listener;

    public AdaptiveNativeImpl(WeakReference<Activity> weakReference,NativeListener listener) {
        super(weakReference);
        this.listener = listener;
    }

    @Override
    protected void onNativeLoaded(NativeAdView nativeAdView) {
        listener.onNativeLoad(nativeAdView);
    }

    @Override
    protected void onNativeFailed(String error) {
        listener.onNativeFailed(error);
    }

    @Override
    protected void onImpression() {
        listener.onImpression();
    }

    public interface NativeListener {
        void onNativeLoad(NativeAdView nativeAdView);
        void onImpression();
        void onNativeFailed(String error);
    }
}
