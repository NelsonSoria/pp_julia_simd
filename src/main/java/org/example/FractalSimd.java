package org.example;

import java.nio.ByteBuffer;

public class FractalSimd {
    public ByteBuffer pixel_buffer;

    public FractalSimd() {
        pixel_buffer = ByteBuffer.allocateDirect(FractalParams.WIDTH * FractalParams.HEIGHT*4);
    }

    public void juliaSimd() {
        FractalDll.INSTANCE.julia_simd(FractalParams.x_min,
                FractalParams.y_min, FractalParams.x_max,
                FractalParams.y_max, FractalParams.max_iterations,
                pixel_buffer);
    }
}
