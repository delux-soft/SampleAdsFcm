package com.example.sampleadsfcm.abstractAds;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;

import com.example.sampleadsfcm.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;


import java.lang.ref.WeakReference;

abstract public class AdaptiveBanner {
    private static final String TAG = "AdaptiveBanner";

    private final WeakReference<Activity> weakReference;

    public AdaptiveBanner(WeakReference<Activity> weakReference) {
        this.weakReference = weakReference;
        Log.i(TAG, "AdaptiveBanner: inititalized");
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(weakReference.get());
        adView.setAdUnitId(weakReference.get().getResources().getString(R.string.banner_id));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adFailed(loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adLoad(adView);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adImpression();
            }
        });
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = weakReference.get().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(weakReference.get(), adWidth);
    }

    protected abstract void adLoad(AdView adView);
    protected abstract void adClosed();
    protected abstract void adFailed(String error);
    protected abstract void adImpression();

}
