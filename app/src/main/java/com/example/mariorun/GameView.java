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
    private final SurfaceHolder holder;
    private final GameThread gameLoopThread;
    private final Bitmap marios[] = new Bitmap[4];
    private Bitmap bloque;
    private final double scale = 1.75;
    private int tiempo = 0;
    private int marioY;  // posición vertical de Mario
    private boolean isJumping = false;

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameThread(this);
        holder = getHolder();
        marioY = 726;  // Inicializar la posición vertical de Mario
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
                bloque = BitmapFactory.decodeResource(getResources(), R.drawable.bloque2);
                marios[0] = BitmapFactory.decodeResource(getResources(), R.drawable.mario1);
                marios[1] = BitmapFactory.decodeResource(getResources(), R.drawable.mario2);
                marios[2] = BitmapFactory.decodeResource(getResources(), R.drawable.mario3);
                marios[3] = BitmapFactory.decodeResource(getResources(), R.drawable.mario2);
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
        for (int i = 0; i < 27; i++) {
            canvas.drawBitmap(bloque, (int) (i * 48 * scale), 894, null);
        }
        canvas.drawBitmap(marios[tiempo % 4], 10, marioY, null);
        tiempo++;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isJumping) {
                    jump();// Iniciar la animación de salto
                }
                break;
        }
        return true;
    }

    private void jump() {
        isJumping = true;

        final int jumpHeight = 400;  // Altura del salto
        final int jumpDuration = 1000;  // Duración del salto en milisegundos
        final int framesPerSecond = 10;  // Cuadros por segundo

        int frames = (jumpDuration * framesPerSecond) / 1000;  // Calcular la cantidad de cuadros
        int jumpDistance = jumpHeight / frames;  // Calcular la distancia vertical por cuadro
        for (int i = 0; i < frames; i++) {
            marioY -= jumpDistance;  // Cambiar la posición vertical de Mario
            postInvalidate();  // Actualizar la vista
            try {
                Thread.sleep(1000 / framesPerSecond);  // Esperar para controlar la velocidad de la animación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < frames; i++) {
            marioY -= jumpDistance;  // Cambiar la posición vertical de Mario
            postInvalidate();  // Actualizar la vista
            try {
                Thread.sleep(1000 / framesPerSecond);  // Esperar para controlar la velocidad de la animación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Asegurarse de que Mario esté en la posición inicial después del salto
        marioY = 726;
        postInvalidate();//Actualizar nuevamente
        isJumping = false;//Declarar que ya no está saltando
    }
}
