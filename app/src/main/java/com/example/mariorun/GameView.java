package com.example.mariorun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    private final SurfaceHolder holder;
    private MediaPlayer mediaPlayer;
    private final GameThread gameLoopThread;
    private final Sprite marios[] = new Sprite[5];
    private Sprite bloque;
    private Sprite tubos[] = new Sprite[3];
    private int tiempo = 0, x = 0, velx = 20, vely = 20;
    private int marioY;  // posición vertical de Mario
    private boolean saltando = false;//verifica que mario esté en el suelo
    int acel = 23, y = 0;

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameThread(this);
        holder = getHolder();
        marioY = 726;  // Inicializar la posición vertical de Mario
        mediaPlayer = MediaPlayer.create(context, R.raw.jump);
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
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                bloque = BitmapFactory.decodeResource(getResources(), R.drawable.bloque2);
                marios[0] = new Sprite(context, R.drawable.mario1, 10, 727,32,64);
                marios[1] = new Sprite(context, R.drawable.mario2, 10, 727,32,64);
                marios[2] = new Sprite(context, R.drawable.mario3, 10, 727,32,64);
                marios[3] = new Sprite(context, R.drawable.mario2, 10, 727,32,64);
                marios[4] = new Sprite(context, R.drawable.mario4, 10, 727,32,64);

                tubos[0] = new Sprite(context, R.drawable.tubo1, 1000, 0, 64, 64);
                tubos[1] = new Sprite(context, R.drawable.tubo2, 1000, 0, 64, 96);
                tubos[2] = new Sprite(context, R.drawable.tubo3, 1000, 0, 64, 120);
                bloque = new Sprite(context, R.drawable.bloque2, 0, 0, 32, 32);
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
        Sprite mario;
        Sprite enem1 = tubos[0], enem2, enem3;
        canvas.drawColor(Color.argb(255, 92, 148, 252));
        for (int i = 0; i < 28; i++) {
            canvas.drawBitmap(bloque.getBmp(), x + bloque.w * i, 894, null);
        }
        if (saltando) {
            vely += acel;
            y += vely;
            acel -= 5;

            if (y <= 0) {
                saltando = false;
                y = 0;
                vely = 0;
                acel = 26;
            }
            mario = marios[4];
        } else {
            mario = marios[tiempo % 4];
        }
        mario.setY(mario.getY() - y);
        enem1.setY(800 - enem1.h);
        canvas.drawBitmap(mario.getBmp(), mario.getX(), mario.getY(), null);
        canvas.drawBitmap(enem1.getBmp(), enem1.getX(), enem1.getY(), null);
        //canvas.drawText("Colision: "+Colision(mario, enem1), 500, 500, p1);
        enem1.setX(enem1.getX() - velx);
        if (Colision(mario, enem1))
            gameLoopThread.setRunning(false);


        enem1.setX(enem1.getX() - velx);

        tiempo++;
        x -= velx;
        if (x < -bloque.w) {
            x += bloque.w;
        }
        if (Colision(mario, enem1))
            gameLoopThread.setRunning(false);


        enem1.setX(enem1.getX() - velx);

        if (enem1.getX() < 0 - enem1.w)
            enem1.setX(2000);

        tiempo++;


        x -= velx;

        if (x < -bloque.w) {
            x += bloque.w;
        }
    }

    public boolean Colision(Sprite s1, Sprite s2) {
        boolean b = (s1.getX() > s2.getX() - s1.w);
        b = b && s1.getY() > s2.getY() - s1.h;
        b = b && s1.getX() < s2.getX() + s1.w;
        b = b && s1.getY() < s2.getY() + s2.h;
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        saltando = true;
        mediaPlayer.start();
        return true;
    }


}
