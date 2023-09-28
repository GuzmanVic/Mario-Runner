package com.example.mariorun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sprite {
    Bitmap bmp;
    int x, y, w, h;
    double scale = 2.7;

    public Sprite(Context context,int id, int x, int y) {
        this.bmp = BitmapFactory.decodeResource(context.getResources(),id);
        this.x = x;
        this.y = y;
    }

    public Sprite(Context context, int id, int x, int y, int w, int h) {
        this.bmp = BitmapFactory.decodeResource(context.getResources(), id);
        this.x = x;
        this.y = y;
        this.w = (int) (w * scale);
        this.h = (int) (h * scale);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x=x;
    }

    public void setY(int y){
        this.y=y;
    }
    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
