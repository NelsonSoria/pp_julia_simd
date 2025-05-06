package org.example;

public class FractalParams {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;

    final static double x_min = -1.5;
    final static double x_max = 1.5;

    final static double y_min = -1;
    final static double y_max = 1;

    final static  double c_real =-0.7;
    final static  double c_imag =0.2715;

    public final static int max_iterations = 10;

    static final int PALETTE_SIZE = 16;

    static int _bswap32(int a) {
        return
                ((a & 0X000000FF) << 24) |
                        ((a & 0X0000FF00) <<  8) |
                        ((a & 0x00FF0000) >>  8) |
                        ((a & 0xFF000000) >> 24);
    }

    public static int color_ramp[] = {
            (0xFF1010FF),
            (0xEF1019FF),
            (0xE01123FF),
            (0xD1112DFF),
            (0xC11237FF),
            (0xB21341FF),
            (0xA3134BFF),
            (0x931455FF),
            (0x84145EFF),
            (0x751568FF),
            (0x651672FF),
            (0x56167CFF),
            (0x471786FF),
            (0x371790FF),
            (0x28189AFF),
            (0x1919A4FF)
    };
}
