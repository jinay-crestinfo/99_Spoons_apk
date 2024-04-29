package com.oysb.utils.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class AdvPlayer extends RelativeLayout {
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SurfaceView sv_video;

    public AdvPlayer(Context context) {
        super(context);
        init();
    }

    private void init() {
        sv_video = new SurfaceView(getContext());
        sv_video.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        surfaceHolder = sv_video.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Surface created
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Surface changed
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Surface destroyed
                releaseMediaPlayer();
            }
        });
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.getCurrentPosition();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
