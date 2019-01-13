package com.example.android.bakeme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoPlayerFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = VideoPlayerFragment.class.getSimpleName();

    private Unbinder mUnbinder;
    private String mVideoURL;
    private int mImagePlaceholderId;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Context mContext;
    private long mCurrentVideoPlayerPosition;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.placeholder_image_view)
    ImageView noAvailableVideoImageView;

    public VideoPlayerFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if bundle contains the URL key, set this value to the member variable of this class
        // and set up ExoPlayer to play the right video
        if (getArguments().containsKey(Constants.VIDEO_URL_KEY)) {
            mVideoURL = getArguments().getString(Constants.VIDEO_URL_KEY);
            mImagePlaceholderId = Constants.NO_IMAGE_ID;
        }

        // if image Id is contained in the bundle, set the image to be displayed in
        // the ExoPlayerView in place of the video, and set URL value to NULL.
        else if (getArguments().containsKey(Constants.DEFAULT_IMAGE_KEY)) {
            mImagePlaceholderId = (getArguments().getInt(Constants.DEFAULT_IMAGE_KEY));
            mVideoURL = null;
        }


        if (savedInstanceState != null) {
            mCurrentVideoPlayerPosition = savedInstanceState.getLong(Constants.CURRENT_VIDEO_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState == null){
            setupMediaPlayerOrShowDefaultImage();
        }


        return rootView;
    }

    public void setupMediaPlayerOrShowDefaultImage(){

        if (!TextUtils.isEmpty(mVideoURL) && mVideoURL != null){

            // if playing video, do not display the default image view
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            noAvailableVideoImageView.setVisibility(View.GONE);

            // create the Media Player and Media Session to handle playing the video.
            initializePlayer(Uri.parse(mVideoURL));
            initializeMediaSession();

        } else if (mImagePlaceholderId != Constants.NO_IMAGE_ID) {

            // if no video is available for this step, display image of the cake instead
            simpleExoPlayerView.setVisibility(View.GONE);
            noAvailableVideoImageView.setVisibility(View.VISIBLE);
            noAvailableVideoImageView.setImageResource(mImagePlaceholderId);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param videoUri The URI of the video to play.
     */
    private void initializePlayer(Uri videoUri) {
        if (mSimpleExoPlayer == null) {
            // create instance of MediaPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

            // attach the player to the view
            simpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(mContext, "BakeMe");
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(mContext, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mSimpleExoPlayer.prepare(videoSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
            mSimpleExoPlayer.seekTo(mCurrentVideoPlayerPosition);

        }

    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mSimpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mSimpleExoPlayer.seekTo(0);
        }
    }

    /**
     * Release Exoplayer
     */
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mCurrentVideoPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) mCurrentVideoPlayerPosition
                = mSimpleExoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mSimpleExoPlayer == null) {
            setupMediaPlayerOrShowDefaultImage();
        } else {
            mSimpleExoPlayer.setPlayWhenReady(true);
            mSimpleExoPlayer.seekTo(mCurrentVideoPlayerPosition);
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.CURRENT_VIDEO_POSITION, mCurrentVideoPlayerPosition);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync.
     *
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
