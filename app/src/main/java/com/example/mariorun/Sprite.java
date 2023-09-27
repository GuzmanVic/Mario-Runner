package com.example.mariorun;

import android.graphics.Bitmap;

public class Sprite {
    Bitmap bmp;
    int x, y, w, h;

    public Sprite(Bitmap bmp, int x, int y) {
        this.bmp = bmp;
        this.x = x;
        this.y = y;
    }

    public Sprite(Bitmap bmp, int x, int y, int w, int h) {
        this.bmp = bmp;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Bitmap getBmp(Bitmap bmp) {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
