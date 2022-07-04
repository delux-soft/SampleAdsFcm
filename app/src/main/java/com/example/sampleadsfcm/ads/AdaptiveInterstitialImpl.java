package com.example.sampleadsfcm.ads;

import com.example.sampleadsfcm.abstractAds.AdaptiveInterstitial;
import com.google.android.gms.ads.interstitial.InterstitialAd;


import java.lang.ref.WeakReference;

public class AdaptiveInterstitialImpl extends AdaptiveInterstitial {
    private static final String TAG = "AdaptiveInterstitialImp";
    private final InterstitialAdListener listener;

    public AdaptiveInterstitialImpl(InterstitialAdListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onAdLoad(WeakReference<InterstitialAd> interstitialAd) {
        listener.onAdLoad(interstitialAd);
    }

    @Override
    protected void onAdFailed(String error) {
        listener.onAdFailedToLoad(error);
    }

    @Override
    protected void onAdDismissed() {
        listener.onAdDismissed();
    }

    @Override
    protected void onAdFailedToShow(String error) {
        listener.onAdShowFailed(error);
    }

    @Override
    protected void onAdFullScreen() {
    }

    @Override
    protected void adImpression() {
        listener.adImpression();
    }

    public interface InterstitialAdListener {
        void onAdLoad(WeakReference<InterstitialAd> interstitialAd);
        void onAdFailedToLoad(String error);
        void onAdShow();
        void onAdShowFailed(String error);
        void onAdDismissed();
        void adImpression();

    }
}
