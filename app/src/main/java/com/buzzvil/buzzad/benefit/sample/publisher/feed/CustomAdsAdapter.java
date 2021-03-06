package com.buzzvil.buzzad.benefit.sample.publisher.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buzzvil.buzzad.benefit.core.models.Ad;
import com.buzzvil.buzzad.benefit.core.models.Creative;
import com.buzzvil.buzzad.benefit.presentation.feed.ad.AdsAdapter;
import com.buzzvil.buzzad.benefit.presentation.media.CtaPresenter;
import com.buzzvil.buzzad.benefit.presentation.media.CtaView;
import com.buzzvil.buzzad.benefit.presentation.media.MediaView;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAd;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdRewardResult;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdView;
import com.buzzvil.buzzad.benefit.presentation.video.VideoErrorStatus;
import com.buzzvil.buzzad.benefit.presentation.video.VideoEventListener;
import com.buzzvil.buzzad.benefit.sample.publisher.R;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdsAdapter extends AdsAdapter<AdsAdapter.NativeAdViewHolder> {

    private final NativeAdView.OnNativeAdEventListener onNativeAdEventListener = new NativeAdView.OnNativeAdEventListener() {
        @Override
        public void onImpressed(final @NonNull NativeAdView view, final @NonNull NativeAd nativeAd) {
            Toast.makeText(view.getContext(), "onImpressed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClicked(@NonNull NativeAdView view, @NonNull NativeAd nativeAd) {
            Toast.makeText(view.getContext(), "onClicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardRequested(@NonNull NativeAdView view, @NonNull NativeAd nativeAd) {
            Toast.makeText(view.getContext(), "onRewardRequested", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewarded(@NonNull NativeAdView view, @NonNull NativeAd nativeAd, @Nullable NativeAdRewardResult nativeAdRewardResult) {
            Toast.makeText(view.getContext(), "onRewarded: " + nativeAdRewardResult, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onParticipated(final @NonNull NativeAdView view, final @NonNull NativeAd nativeAd) {
            Toast.makeText(view.getContext(), "onParticipated", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public AdsAdapter.NativeAdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final NativeAdView feedAdView = (NativeAdView) inflater.inflate(R.layout.bz_view_feed_ad, parent, false);
        return new NativeAdViewHolder(feedAdView);
    }

    @Override
    public void onBindViewHolder(NativeAdViewHolder holder, NativeAd nativeAd) {
        final NativeAdView view = (NativeAdView) holder.itemView;
        view.addOnNativeAdEventListener(onNativeAdEventListener);

        final Ad ad = nativeAd.getAd();
        final MediaView mediaView = view.findViewById(R.id.mediaView);
        final LinearLayout titleLayout = view.findViewById(R.id.titleLayout);
        final TextView titleView = view.findViewById(R.id.textTitle);
        final ImageView iconView = view.findViewById(R.id.imageIcon);
        final TextView descriptionView = view.findViewById(R.id.textDescription);
        final CtaView ctaView = view.findViewById(R.id.ctaView);

        mediaView.setCreative(ad.getCreative());
        mediaView.setVideoEventListener(new VideoEventListener() {
            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onResume() {
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onReplay() {
            }

            @Override
            public void onLanding() {
            }

            @Override
            public void onError(@NonNull VideoErrorStatus videoErrorStatus, @Nullable String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(mediaView.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onVideoEnded() {
            }
        });
        /* Optional feature to customize VideoPlayer
            mediaView.setVideoPlayerOverlayView(new VideoPlayerOverlayMediaView(getContext()));
            mediaView.setVideoUIConfig(
                    new VideoUIConfig.Builder()
                            .fullscreenIcon(R.drawable.bz_ic_fullscreen)
                            .showFullscreen(false)
                            .soundIconSelector(R.drawable.bz_ic_volume)
                            .playButtonIcon(R.drawable.exo_icon_play)
                            .pauseButtonIcon(R.drawable.exo_icon_pause)
                            .replayButtonIcon(R.drawable.bz_ic_btn_restart)
                            .goToButtonIcon(R.drawable.bz_ic_btn_more)
                            .build()
            );
         */
        titleView.setText(ad.getTitle());
        descriptionView.setText(ad.getDescription());
        Glide.with(holder.itemView).load(ad.getIconUrl()).into(iconView);
        final CtaPresenter ctaPresenter = new CtaPresenter(ctaView);
        ctaPresenter.bind(nativeAd);

        final Creative.Type creativeType = ad.getCreative() == null ? null : ad.getCreative().getType();
        if (Creative.Type.IMAGE.equals(creativeType)) {
            titleLayout.setVisibility(View.GONE);
            descriptionView.setVisibility(View.GONE);
        } else {
            titleLayout.setVisibility(View.VISIBLE);
            descriptionView.setVisibility(View.VISIBLE);
        }

        final Collection<View> clickableViews = new ArrayList<>();
        clickableViews.add(ctaView);
        clickableViews.add(mediaView);
        clickableViews.add(titleLayout);
        clickableViews.add(descriptionView);

        view.setNativeAd(nativeAd);
        view.setMediaView(mediaView);
        view.setClickableViews(clickableViews);
    }
}
