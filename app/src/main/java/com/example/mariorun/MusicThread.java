package com.example.mariorun;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicThread extends Thread {
    private Context context;
    private MediaPlayer musica;
    private boolean isPlaying;

    public MusicThread(Context context, int resourceId) {
        this.context = context;
        musica = MediaPlayer.create(context, resourceId);
        musica.setLooping(true);
        isPlaying = false;
    }

    @Override
    public void run() {
        try {
            if (!musica.isPlaying()) {
                musica.start();
                isPlaying = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (musica.isPlaying()) {
            musica.pause();
            isPlaying = false;
        }
    }

    public void resumeMusic() {
        if (!musica.isPlaying()) {
            musica.start();
            isPlaying = true;
        }
    }

    public void stopMusic() {
        if (musica != null) {
            musica.stop();
            musica.release();
            musica = null;
        }
    }

    public boolean isMusicPlaying() {
        return isPlaying;
    }
}
