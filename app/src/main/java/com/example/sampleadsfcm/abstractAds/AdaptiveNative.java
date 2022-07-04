package com.example.sampleadsfcm.abstractAds;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sampleadsfcm.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;


import java.lang.ref.WeakReference;

abstract public class AdaptiveNative {

    private final WeakReference<Activity> weakReference;
    private NativeAd nativeAd;

    public AdaptiveNative(WeakReference<Activity> weakReference) {
        this.weakReference = weakReference;
    }

    public void loadNativeAd(){
        AdLoader.Builder builder = new AdLoader.Builder(weakReference.get(), weakReference.get().getString(R.string.native_ad));

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions nativeAdOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.forNativeAd(nativeAd -> {

            AdaptiveNative.this.nativeAd = nativeAd;


            NativeAdView adView =
                    (NativeAdView) weakReference.get().getLayoutInflater()
                            .inflate(R.layout.native_ad_layout, null);
            populateUnifiedNativeAdView(nativeAd, adView);

            onNativeLoaded(adView);



        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onNativeFailed(loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                onImpression();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        }).withNativeAdOptions(nativeAdOptions).build().loadAd(new AdRequest.Builder().build());

    }


    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        View headView = adView.findViewById(R.id.ad_headline);
        View actionView = adView.findViewById(R.id.ad_call_to_action);
        View appIconView = adView.findViewById(R.id.ad_app_icon);
        View starView = adView.findViewById(R.id.ad_stars);

        adView.setMediaView(mediaView);
        adView.setHeadlineView(headView);
        adView.setCallToActionView(actionView);
        adView.setIconView(appIconView);
        adView.setStarRatingView(starView);


        if (adView.getMediaView() == null){
            mediaView.setVisibility(View.GONE);
        }else {
            if (nativeAd.getMediaContent() != null){
                adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
            }else {
                mediaView.setVisibility(View.GONE);
            }
        }




        TextView header = (TextView) adView.getHeadlineView();

        if (header != null){
            header.setText(nativeAd.getHeadline());
        }else {
            headView.setVisibility(View.GONE);
        }





        Button button = (Button) adView.getCallToActionView();

        if (button == null) {
            actionView.setVisibility(View.INVISIBLE);
        } else {
            actionView.setVisibility(View.VISIBLE);
            button.setText(nativeAd.getCallToAction());

        }

        ImageView icon =(ImageView) adView.getIconView();
        if (icon == null){
            appIconView.setVisibility(View.INVISIBLE);
        }else {
            appIconView.setVisibility(View.VISIBLE);
            if (nativeAd.getIcon() == null){
                appIconView.setVisibility(View.INVISIBLE);
            }else {
                icon.setImageDrawable(nativeAd.getIcon().getDrawable());
            }
        }



        RatingBar ratingBar = (RatingBar) adView.getStarRatingView();

        if (ratingBar == null){
            starView.setVisibility(View.INVISIBLE);
        }else {
            starView.setVisibility(View.VISIBLE);
            if (nativeAd.getStarRating() == null){
                starView.setVisibility(View.INVISIBLE);
            }else {
                ratingBar.setRating(nativeAd.getStarRating().floatValue());
            }

        }




        adView.setNativeAd(nativeAd);
    }

    public void destroyNative(){
        if (nativeAd != null)
            nativeAd.destroy();
    }

    protected abstract void onNativeLoaded(NativeAdView nativeAdView);
    protected abstract void onNativeFailed(String error);
    protected abstract void onImpression();
}
