package com.example.sampleadsfcm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sampleadsfcm.ads.AdaptiveBannerImpl;
import com.example.sampleadsfcm.databinding.ActivitySecondBinding;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private ActivitySecondBinding binding;
    private AdaptiveBannerImpl adaptiveBanner;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adaptiveBanner = new AdaptiveBannerImpl(new WeakReference<>(this), new AdaptiveBannerImpl.AdaptiveListener() {
            @Override
            public void onLoad(AdView adView) {
                if (!isDestroyed()){
                    Log.d(TAG, "onLoad: it is alive");
                    if (!isPause){
                        Log.d(TAG, "onLoad: it is not pause");
                        binding.ady.addView(adView);
                        binding.ady.setVisibility(View.VISIBLE);
                    }else {
                        Log.d(TAG, "onLoad: it is in pause");
                    }
                }else {
                    Log.d(TAG, "onLoad: it is destroyed");
                }
            }

            @Override
            public void onClosed() {
                Log.d(TAG, "onClosed: ");
            }

            @Override
            public void onFailed(String error) {
                Log.d(TAG, "onFailed: "+error);
                if (!isDestroyed()){
                    binding.ady.setVisibility(View.GONE);
                }
            }

            @Override
            public void adImpression() {
                Log.d(TAG, "adImpression: called");
            }
        });

        binding.getRoot().getViewTreeObserver().addOnWindowFocusChangeListener(b -> {
            if (b){
                isPause = false;
                adaptiveBanner.loadAd();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }
}