package org.example;

import jnr.ffi.LibraryLoader;

import java.nio.Buffer;

public interface FractalDll {
    String LIBRARY_NAME ="libjulia_simd";
    FractalDll INSTANCE = LibraryLoader.create(FractalDll.class)
            .load(LIBRARY_NAME);

    void julia_simd(double x_min, double y_min, double x_max, double y_max, int max_iteraciones, Buffer pixel_buffer);
}
