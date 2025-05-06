package org.example;

public class FractalCpu {
    public int pixel_buffer[];

    public FractalCpu() {
        pixel_buffer = new int[FractalParams.WIDTH * FractalParams.HEIGHT];
    }

    private int divergente(double z0r, double z0i) {
        int iter = 1;
        double zr = z0r;
        double zi = z0i;

        while ((zr * zr + zi * zi) < 4.0 && iter < FractalParams.max_iterations) {
            double dr = zr * zr - zi * zi + FractalParams.c_real;
            double di = 2 * zr * zi + FractalParams.c_imag;
            zr = dr;
            zi = di;
            iter++;
        }

        if (iter < FractalParams.max_iterations) {
            int index = iter % FractalParams.PALETTE_SIZE;
            return FractalParams.color_ramp[index];
        } else {
            return 0x00000000;  // negro
        }
    }


    public void mandelbrotCpu() {
        double dx = (FractalParams.x_max - FractalParams.x_min) / FractalParams.WIDTH;
        double dy = (FractalParams.y_max - FractalParams.y_min) / FractalParams.HEIGHT;
        for (int i = 0; i < FractalParams.WIDTH; i++) {
            for (int j = 0; j < FractalParams.HEIGHT; j++) {
                double x = FractalParams.x_min + i * dx;
                double y = FractalParams.y_max - j * dy;
                int color = divergente(x, y);
                pixel_buffer[j * FractalParams.WIDTH + i] = color;
            }
        }
    }
}