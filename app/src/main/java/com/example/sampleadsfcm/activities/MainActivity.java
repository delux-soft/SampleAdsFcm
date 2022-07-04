package com.example.sampleadsfcm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sampleadsfcm.ZonalApp;
import com.example.sampleadsfcm.ads.AdaptiveInterstitialImpl;
import com.example.sampleadsfcm.ads.AdaptiveNativeImpl;
import com.example.sampleadsfcm.databinding.ActivityMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    AdaptiveNativeImpl adaptiveNative;
    AdaptiveInterstitialImpl adaptiveInterstitial;
    private NativeAdView nativeAdView = null;
    private InterstitialAd interstitialAd = null;
    boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(
                this,
                initializationStatus -> Log.i(TAG, "onInitializationComplete: "+initializationStatus));

        ZonalApp.getInstance().initAppOpenAd();

        adaptiveNative = new AdaptiveNativeImpl(new WeakReference<>(this), new AdaptiveNativeImpl.NativeListener() {
            @Override
            public void onNativeLoad(NativeAdView nativeAdView) {
                if (!isDestroyed()){
                    Log.d(TAG, "onNativeLoad: it is alive");
                    if (!isPause){
                        Log.d(TAG, "onNativeLoad: it is not pause");

                        binding.flAdplaceholder.removeAllViews();
                        binding.flAdplaceholder.addView(nativeAdView);
                        binding.nativeContainer.setVisibility(View.VISIBLE);
                    }else {
                        Log.d(TAG, "onNativeLoad: it is in pause");
                        MainActivity.this.nativeAdView = nativeAdView;
                    }
                }else {
                    Log.d(TAG, "onNativeLoad: it is destroyed");
                }
            }

            @Override
            public void onImpression() {
                Log.d(TAG, "onImpression: called");
            }

            @Override
            public void onNativeFailed(String error) {
                Log.d(TAG, "onNativeFailed: $error");
                if (!isDestroyed()){
                    binding.nativeContainer.setVisibility(View.INVISIBLE);
                }
            }
        });

        adaptiveInterstitial = new AdaptiveInterstitialImpl(new AdaptiveInterstitialImpl.InterstitialAdListener() {
            @Override
            public void onAdLoad(WeakReference<InterstitialAd> interstitialAd) {
                MainActivity.this.interstitialAd = interstitialAd.get();
            }

            @Override
            public void onAdFailedToLoad(String error) {
                Log.d(TAG, "onAdFailedToLoad: "+error);
            }

            @Override
            public void onAdShow() {
                Log.d(TAG, "onAdShow: ");
            }

            @Override
            public void onAdShowFailed(String error) {
                Log.d(TAG, "onAdShowFailed: "+error);
            }

            @Override
            public void onAdDismissed() {
                Log.d(TAG, "onAdDismissed: ");
                interstitialAd = null;
            }

            @Override
            public void adImpression() {
                Log.d(TAG, "adImpression: called");
            }
        });

        binding.getRoot().getViewTreeObserver().addOnWindowFocusChangeListener(b -> {
            Log.d(TAG, "onFocusChanged: "+b);
            // only interact with ads ,if focus is true
            if (b){
                if (nativeAdView != null){
                    // show restore native ad here
                    binding.flAdplaceholder.removeAllViews();
                    binding.flAdplaceholder.addView(nativeAdView);
                    binding.nativeContainer.setVisibility(View.VISIBLE);
                    nativeAdView = null;
                }else {
                    // load new native ad , if restore ad is null
                    adaptiveNative.loadNativeAd();
                }
                isPause = false;
            }
        });

        binding.letstart.setOnClickListener(v -> {
            changeScreen();
            if (interstitialAd != null){
                interstitialAd.show(MainActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // load interstitial ad ,if it is null
        if (interstitialAd == null){
            adaptiveInterstitial.getAd(getApplicationContext());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    private void changeScreen() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();
    }
}