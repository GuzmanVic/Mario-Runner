package com.example.mariorun;

import android.graphics.Canvas;

public class GameThread extends Thread {
    static final long FPS = 10; // Establece la velocidad de cuadros por segundo (10 FPS).

    final GameView gameView; // Referencia al objeto GameView para interactuar con él desde este hilo.
    private boolean running = false; // Indica si el hilo del juego está en ejecución.

    public GameThread(GameView view) {
        this.gameView = view; // Inicializa la referencia al objeto GameView.
    }

    public void setRunning(boolean run) {
        running = run; // Método para iniciar o detener el hilo del juego.
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS; // Calcula el tiempo en milisegundos entre cada cuadro.
        long startTime;
        long sleepTime;

        while (running) { // Bucle principal del hilo del juego.
            Canvas c = null;
            startTime = System.currentTimeMillis(); // Registra el tiempo actual.

            try {
                c = gameView.getHolder().lockCanvas(); // Bloquea el lienzo para dibujar en él.
                gameView.draw(c); // Llama al método de dibujo de GameView para actualizar la vista.

            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c); // Desbloquea el lienzo y muestra el dibujo.
                }
            }

            sleepTime = ticksPS - (System.currentTimeMillis() - startTime); // Calcula el tiempo de espera para mantener el FPS.
            try {
                if (sleepTime > 0)
                    sleep(sleepTime); // Espera el tiempo necesario para mantener el FPS deseado.
                else
                    sleep(10); // Si el cálculo resulta en un tiempo negativo, se espera 10 milisegundos.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

