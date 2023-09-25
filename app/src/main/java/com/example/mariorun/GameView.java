package com.example.mariorun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    final SurfaceHolder holder;
    final GameThread gameLoopThread;
    private Bitmap marios[];
    private Bitmap bloque;
    private double scale = 2.8;
    private int tiempo = 0;

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                bloque = BitmapFactory.decodeResource(context.getResources(), R.drawable.bloque2);
                marios[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mario1);
                marios[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mario2);
                marios[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mario3);
                marios[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mario2);
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.argb(255, 92, 148, 252));
        for (int i = 0; i < 20; i++) {
            canvas.drawBitmap(bloque, (int) (i * 32 * scale), 800, null);
        }
        canvas.drawBitmap(marios[tiempo % 4], 10, 860, null);
        tiempo++;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
