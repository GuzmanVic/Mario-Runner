package com.example.mariorun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

public class GameView extends SurfaceView {
    final SurfaceHolder holder;
    final GameThread gameLoopThread;
    final MusicThread musicThread;
    private Sprite bloque;
    private MediaPlayer saltoSonido, loose;
    private Sprite marios[] = new Sprite[5];
    private Sprite tubos[] = new Sprite[3];
    int pisoMario;
    boolean gameOver = false;
    private int tiempo = 0, x = 0, velx = 30, vely = 0, acel = 75, y = 0;
    private boolean salto = false;
    private long tiempoJuego = 0; // Inicialmente el tiempo es 0 en milisegundos
    private long aumentoX = 0;

    Random random = new Random();
    int maxX = 3000;
    int aleatorio = random.nextInt(maxX - 2000) + 2000;
    int aleatorio1 = random.nextInt(maxX - 2000) + 2000;
    int aleatorio2 = random.nextInt(maxX - 2000) + 2000;


    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameThread(this);
        musicThread = new MusicThread(context, R.raw.game_music);
        musicThread.start();
        holder = getHolder();
        saltoSonido = MediaPlayer.create(context, R.raw.jump);
        loose = MediaPlayer.create(context, R.raw.loose);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                musicThread.stopMusic();
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
                marios[0] = new Sprite(context, R.drawable.mario1, 100, 640, 32, 64);
                marios[1] = new Sprite(context, R.drawable.mario2, 100, 640, 32, 64);
                marios[2] = new Sprite(context, R.drawable.mario3, 100, 640, 32, 64);
                marios[3] = new Sprite(context, R.drawable.mario2, 100, 640, 32, 64);
                marios[4] = new Sprite(context, R.drawable.mario4, 100, 640, 32, 64);

                tubos[0] = new Sprite(context, R.drawable.tubo1, aleatorio, 0, 64, 64);
                tubos[1] = new Sprite(context, R.drawable.tubo2, aleatorio1, 0, 64, 96);
                tubos[2] = new Sprite(context, R.drawable.tubo3, aleatorio2, 0, 64, 128);
                pisoMario = marios[0].getY();
                bloque = new Sprite(context, R.drawable.bloque2, 0, 0, 32, 32);
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                musicThread.resumeMusic();
                // Iniciar un hilo para el contador de tiempo
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (!gameOver) {
                                tiempoJuego += 1000; // Aumenta el tiempo en 1 segundo cada segundo
                            }
                            try {
                                Thread.sleep(1000); // Espera 1 segundo
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {


        Paint p1 = new Paint(Color.BLACK);
        super.draw(canvas);
        Sprite mario;
        Sprite enem1 = tubos[0];
        Sprite enem2 = tubos[1];
        Sprite enem3 = tubos[2];
        p1.setTextSize(60);
        aumentarX();
        canvas.drawColor(Color.argb(255, 92, 148, 252));
        if (enem2.getX() < 0 - enem2.w) {
            aleatorio1 = random.nextInt(maxX - 2000) + 2000;
            enem2.setX(aleatorio1); // Corrección: asigna "aleatorio1" a la posición de enem2
        }
        if (enem3.getX() < 0 - enem3.w) {
            aleatorio2 = random.nextInt(maxX - 2000) + 2000;
            enem3.setX(aleatorio2); // Corrección: asigna "aleatorio2" a la posición de enem3
        }
        for (int i = 0; i < 28; i++) {
            canvas.drawBitmap(bloque.getBmp(), x + bloque.w * i, 809, null);
            canvas.drawBitmap(bloque.getBmp(), x + bloque.w * i, 808 + bloque.h, null);
        }

        if (salto) {
            y += acel;
            acel -= 5;
            if (y <= 0) {
                salto = false;
                y = 0;
                acel = 70;
            }
            mario = marios[4];
        } else {
            mario = marios[tiempo % 4];
        }
        mario.setY(pisoMario - y);
        enem1.setY(812 - enem1.h);
        enem2.setY(812 - enem2.h);
        enem3.setY(812 - enem3.h);
        canvas.drawBitmap(mario.getBmp(), mario.getX(), mario.getY(), null);
        canvas.drawBitmap(enem1.getBmp(), enem1.getX(), enem1.getY(), null);
        canvas.drawBitmap(enem2.getBmp(), enem2.getX(), enem2.getY(), null);
        canvas.drawBitmap(enem3.getBmp(), enem3.getX(), enem3.getY(), null);

        if (Colision(mario, enem1)) {
            gameLoopThread.setRunning(false);
            gameOver = true;
            musicThread.pauseMusic();
            loose.start();
        }
        enem1.setX(enem1.getX() - velx);
        enem2.setX(enem2.getX() - velx);
        enem3.setX(enem3.getX() - velx);

        if (enem1.getX() < 0 - enem1.w) {
            aleatorio = random.nextInt(maxX - 2000) + 2000;
            enem1.setX(aleatorio);
        }
        if (enem2.getX() < 0 - enem2.w) {
            aleatorio1 = random.nextInt(maxX - 2000) + 2000;
            enem3.setX(aleatorio);
        }
        if (enem3.getX() < 0 - enem3.w) {
            aleatorio2 = random.nextInt(maxX - 2000) + 2000;
            enem3.setX(aleatorio);
        }

        tiempo++;
        x -= velx;

        if (x < -bloque.w) {
            x += bloque.w;
        }
    }

    public boolean Colision(Sprite s1, Sprite s2) {
        boolean b;
        b = s1.getX() > (s2.getX() - s1.w);
        b = b && s1.getY() > s2.getY() - s1.h;

        b = b && s1.getX() < s2.getX() + s1.w;
        b = b && s1.getY() < s2.getY() + s1.h;
        return b;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver && y == 0) {
            if (saltoSonido != null && !saltoSonido.isPlaying()) {
                saltoSonido.start();
            }
            salto = true;
        }
        return true;
    }


    private void aumentarX() {
        long segundos = tiempoJuego / 1000;
        if (segundos % 10 == 0 && tiempoJuego - aumentoX >= 10000) { // Verifica si han pasado al menos 10 segundos desde el último aumento
            maxX = maxX + 500;
            aleatorio = random.nextInt(maxX - 2000) + 2000;//Aumenta el rango de apariciones en pantalla
            velx += 10; // Aumenta la velocidad
            aumentoX = tiempoJuego; // Actualiza el registro del último aumento
        }
    }

}