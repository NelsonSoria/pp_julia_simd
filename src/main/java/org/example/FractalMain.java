package org.example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class FractalMain {
    int mode = 1;
    private long window;
    private int textureID;

    private IntBuffer intBuffer;

    FractalCpu fractal;
    FractalSimd fractalSimd;

    FPSCounter fpsCounter = new FPSCounter();

    FractalMain() {
        fractal = new FractalCpu();
        fractalSimd = new FractalSimd();
        intBuffer = BufferUtils.createIntBuffer(FractalParams.WIDTH * FractalParams.HEIGHT);
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();


        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");


        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        window = glfwCreateWindow(FractalParams.WIDTH, FractalParams.HEIGHT, "Julia Set!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if (key == GLFW_KEY_1 && action == GLFW_RELEASE) {
                System.out.println("Modo JAVA");
                mode = 1;
            }
            if (key == GLFW_KEY_2 && action == GLFW_RELEASE) {
                System.out.println("Modo SIMD");
                mode = 2;
            }
        });


        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);


            glfwGetWindowSize(window, pWidth, pHeight);


            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


            glfwSetWindowPos(
                    window,
                    (vidmode.width() - FractalParams.WIDTH) / 2,
                    (vidmode.height() - FractalParams.HEIGHT) / 2
            );
        }


        glfwMakeContextCurrent(window);


        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();
        String version = GL11.glGetString(GL11.GL_VERSION);
        String vendor = GL11.glGetString(GL11.GL_VENDOR);
        String renderer = GL11.glGetString(GL_RENDERER);

        System.out.println("OpenGL version : " + version);
        System.out.println("OpenGL vendor  : " + vendor);
        System.out.println("OpenGL renderer: " + renderer);

        {

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-1, 1, -1, 1, -1, 1);

            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glLoadIdentity();
        }
        GL.createCapabilitiesWGL();

        setupTexture();
    }

    void setupTexture() {
        textureID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, FractalParams.WIDTH, FractalParams.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void loop() {

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);


        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            paint();
            glfwSwapBuffers(window);


            glfwPollEvents();
        }
    }

    void paint() {
        fpsCounter.update();
        intBuffer.clear();

        if (mode == 1) {
            fractal.mandelbrotCpu();
            intBuffer.put(fractal.pixel_buffer);

        } else {
            fractalSimd.juliaSimd();
            intBuffer.put(fractalSimd.pixel_buffer.asIntBuffer());

        }

        intBuffer.flip();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, FractalParams.WIDTH, FractalParams.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, intBuffer);


        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex3f(-1, -1, 0);

            glTexCoord2f(0, 0);
            glVertex3f(-1, 1, 0);

            glTexCoord2f(1, 0);
            glVertex3f(1, 1, 0);

            glTexCoord2f(1, 1);
            glVertex3f(1, -1, 0);
        }
        glEnd();
    }

    public static void main(String[] args) {
        System.setProperty("java.library.path", "C: tools/librerias");
        new FractalMain().run();
    }
}
