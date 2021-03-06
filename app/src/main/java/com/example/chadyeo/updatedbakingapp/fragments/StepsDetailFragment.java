package com.example.chadyeo.updatedbakingapp.fragments;


import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chadyeo.updatedbakingapp.DetailActivity;
import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;


public class StepsDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = StepsDetailFragment.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    private ImageView mNoVideoImageView;
    private TextView mDetailSteps_textView;
    private ImageButton mArrowBackImageButton;
    private ImageButton mArrowForwardImageButton;
    private FrameLayout mExoMediaFrame;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private FrameLayout mExoFullScreenButton;
    private ImageView mExoFullScreenIcon;
    private Dialog mFullScreenDialog;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;

    ArrayList<Step> steps;
    long videoPosition;
    private int stepsPosition;
    private int numberOfSteps;
    private boolean mIsExoPlayerFullscreen;
    private String videoUrl;
    private String detailSteps;
    private String thumbnailURL;
    private boolean mTwoPane;

    public StepsDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps_detail, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null) {
           videoPosition = savedInstanceState.getLong("SELECTED_POSITION", C.TIME_UNSET);
        }

        Bundle extras_stepsPosition = this.getArguments();
        mTwoPane = extras_stepsPosition.getBoolean("mTwoPane");
        Log.e(LOG_TAG, "twoPane is " + mTwoPane);
        if (mTwoPane) {
            stepsPosition = 0;
        } else {
            stepsPosition = extras_stepsPosition.getInt("stepsPosition");
        }

        mNoVideoImageView = (ImageView)view.findViewById(R.id.image_no_video_imageView);
        mExoMediaFrame = (FrameLayout)view.findViewById(R.id.main_media_frame);
        mSimpleExoPlayerView = (SimpleExoPlayerView)view.findViewById(R.id.steps_playerView);
        mExoFullScreenButton = (FrameLayout)view.findViewById(R.id.exo_fullscreen_button);
        mExoFullScreenIcon = (ImageView)view.findViewById(R.id.exo_fullscreen_icon);
        mDetailSteps_textView = (TextView)view.findViewById(R.id.detail_steps_textView);
        mArrowBackImageButton = (ImageButton)view.findViewById(R.id.arrow_back_imageButton);
        mArrowForwardImageButton = (ImageButton)view.findViewById(R.id.arrow_forward_imageButton);

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        steps = new ArrayList<>();
        steps = (ArrayList<Step>)getActivity().getIntent().getExtras().getSerializable("steps");

        numberOfSteps = steps.size();

        videoUrl = steps.get(stepsPosition).getVideoURL();
        detailSteps = steps.get(stepsPosition).getDescription();
        thumbnailURL = steps.get(stepsPosition).getThumbnailURL();

        if (videoUrl.isEmpty()) {
            if (!thumbnailURL.isEmpty()) {
                Picasso.get().load(thumbnailURL).fit().into(mNoVideoImageView);
            }
            mNoVideoImageView.setVisibility(View.VISIBLE);
            mSimpleExoPlayerView.setVisibility(View.GONE);
            mSimpleExoPlayerView.hideController();
        } else {
            mNoVideoImageView.setVisibility(View.GONE);
            mSimpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrl));
        }

        mDetailSteps_textView.setText(detailSteps);

        mExoFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsExoPlayerFullscreen) {
                    openFullscreenDialog();
                } else {
                    closeFullscreenDialog();
                }
            }
        });

        mArrowBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepsPosition == 0) {
                    Toast.makeText(getContext(), "You are at the first steps", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!videoUrl.isEmpty()) {
                        releasePlayer();
                    }
                    stepsPosition = stepsPosition - 1;

                    detailSteps = steps.get(stepsPosition).getDescription();
                    mDetailSteps_textView.setText(detailSteps);

                    videoUrl = steps.get(stepsPosition).getVideoURL();
                    thumbnailURL = steps.get(stepsPosition).getThumbnailURL();

                    if (videoUrl.isEmpty()) {
                        if (!thumbnailURL.isEmpty()) {
                            Picasso.get().load(thumbnailURL).fit().into(mNoVideoImageView);
                        }
                        mNoVideoImageView.setVisibility(View.VISIBLE);
                        mSimpleExoPlayerView.setVisibility(View.GONE);
                        mSimpleExoPlayerView.hideController();
                    } else {
                        mNoVideoImageView.setVisibility(View.GONE);
                        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
                        initializePlayer(Uri.parse(videoUrl));
                    }
                }
            }
        });

        mArrowForwardImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepsPosition == numberOfSteps - 1) {
                    Toast.makeText(getContext(), "You are at the last steps", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!videoUrl.isEmpty()) {
                        releasePlayer();
                    }
                    stepsPosition = stepsPosition + 1;

                    detailSteps = steps.get(stepsPosition).getDescription();
                    mDetailSteps_textView.setText(detailSteps);

                    videoUrl = steps.get(stepsPosition).getVideoURL();
                    thumbnailURL = steps.get(stepsPosition).getThumbnailURL();

                    if (videoUrl.isEmpty()) {
                        if (!thumbnailURL.isEmpty()) {
                            Picasso.get().load(thumbnailURL).fit().into(mNoVideoImageView);
                        }
                        mNoVideoImageView.setVisibility(View.VISIBLE);
                        mSimpleExoPlayerView.setVisibility(View.GONE);
                        mSimpleExoPlayerView.hideController();
                    } else {
                        mNoVideoImageView.setVisibility(View.GONE);
                        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
                        initializePlayer(Uri.parse(videoUrl));
                    }
                }
            }
        });

        initializeMediaSession();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(videoUrl));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            videoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("SELECTED_POSITION", videoPosition);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     * */
    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
        if (!steps.get(stepsPosition).getVideoURL().isEmpty()) {
            releasePlayer();
        }
         */
        mMediaSession.setActive(false);
    }

    // ExoPlayer Event Listener
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Initialize ExoPlayer
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(videoPosition);
            mExoPlayer.addListener(this);
        }
    }

    /**
     * Release ExoPlayer
     */
    private void releasePlayer() {
        mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mExoMediaFrame.addView(mSimpleExoPlayerView);
        mIsExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mExoFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    private void openFullscreenDialog() {
        ((ViewGroup)mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mFullScreenDialog.addContentView(mSimpleExoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_shrink));
        mIsExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID);

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),
                PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new NotificationCompat.Action(
                R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), StepsDetailFragment.class), 0);

        builder.setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction);

        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Video Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Play to hear the steps");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(0, builder.build());
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
