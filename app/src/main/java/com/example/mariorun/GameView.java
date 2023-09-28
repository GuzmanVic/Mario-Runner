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
    private final Sprite marios[] = new Sprite[4];
    private Sprite bloque;
    private final double scale = 1.75;
    private int tiempo = 0, x = 0, velx = 20, vely = 20;
    private int marioY;  // posición vertical de Mario
    private boolean isJumping = false;
    private boolean saltando = false;//verifica que mario esté en el suelo
    int acel = 20, y = 0;

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
//                bloque = BitmapFactory.decodeResource(getResources(), R.drawable.bloque2);
                marios[0] = new Sprite(context, R.drawable.mario1,10,710);
                marios[1] = new Sprite(context, R.drawable.mario2,10,710);
                marios[2] = new Sprite(context, R.drawable.mario3,10,710);
                marios[3] = new Sprite(context, R.drawable.mario2,10,710);
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
        canvas.drawColor(Color.argb(255, 92, 148, 252));
        for (int i = 0; i < 28; i++) {
            canvas.drawBitmap(bloque.getBmp(), x + bloque.w * i, 894, null);
        }
        mario = marios[tiempo % 4];

        canvas.drawBitmap(mario.getBmp(), mario.getX(), mario.getY() + y + vely, null);
        if (saltando) {
            vely += acel;
            y -= vely;
        }
        if (vely > 400) {
            vely -= acel;
        }
        tiempo++;
        x -= velx;
        if (x < -bloque.w) {
            x += bloque.w;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        saltando = true;
        //MI MÉTODO DE SALTO
/*        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isJumping) {
                    jump();// Iniciar la animación de salto
                }
                break;
        }
        return true;
  */
        return true;
    }
//MI METODO DE SALTO
    private void jump() {
        isJumping = true;

        final int jumpHeight = 150;  // Altura del salto
        final int jumpDuration = 100;  // Duración del salto en milisegundos
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
