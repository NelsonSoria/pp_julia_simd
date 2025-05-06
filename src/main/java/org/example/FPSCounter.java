package org.example;

public class FPSCounter {
    private long lastTime;
    private int fps;
    private int frames;

    public FPSCounter() {
        lastTime = System.currentTimeMillis();
        fps = 0;
        frames = 0;
    }

    public void update() {
        // Incrementa el contador de cuadros por cada llamada a `update`
        frames++;

        // Verifica si ha pasado un segundo
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 1000) {
            // Actualiza el valor de FPS
            fps = frames;

            // Reinicia el contador de cuadros y el tiempo de referencia
            frames = 0;
            lastTime = currentTime;

            // Imprime o usa el valor de FPS en tu aplicación
            System.out.println("FPS: " + fps);
        }
    }

    public int getFPS() {
        return fps;
    }
}
