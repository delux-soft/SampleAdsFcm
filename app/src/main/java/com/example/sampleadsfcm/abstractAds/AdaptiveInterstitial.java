package com.example.sampleadsfcm.abstractAds;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sampleadsfcm.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import java.lang.ref.WeakReference;

abstract public class AdaptiveInterstitial {
    private static final String TAG = "AdaptiveInterstitial";
    private InterstitialAd mInterstitialAd;



    public void getAd(Context context){
        AdRequest adRequest = new AdRequest.Builder().
                build();
        InterstitialAd.load(context, context.getString(R.string.interstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                        setCallback();
                        onAdLoad(new WeakReference<>(mInterstitialAd));
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        Log.i(TAG, loadAdError.getMessage());
                        onAdFailed(loadAdError.getMessage());
                    }
                });
    }


    private void setCallback(){

        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Log.i(TAG, "onAdDismissedFullScreenContent: ad dismissed");
                onAdDismissed();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.i(TAG, "onAdFailedToShowFullScreenContent: "+adError.getMessage());
                onAdFailedToShow(adError.getMessage());
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.i(TAG, "onAdShowedFullScreenContent: ad successfully showed");
                onAdFullScreen();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adImpression();
            }
        });
    }

    protected abstract void onAdLoad(WeakReference<InterstitialAd> interstitialAd);
    protected abstract void onAdFailed(String error);
    protected abstract void onAdDismissed();
    protected abstract void onAdFailedToShow(String error);
    protected abstract void onAdFullScreen();
    protected abstract void adImpression();
}
