package com.base.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class Window {

    private static final int FPS_CAP = 60;

    private static long lastFrameTime;
    private static float delta;

    public static void createWindow(int width, int height, String title)
    {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            /*
            DisplayMode[] modes = Display.getAvailableDisplayModes();

            for (int i=0;i<modes.length;i++) {
                DisplayMode current = modes[i];
                System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
                        current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
            }
            */

            /*
            -If the edges of the water quad look a bit jagged (especially when you zoom out with the camera)
            then you may have a problem with the precision of the depth buffer. This can be fixed by increasing
            the NEAR_PLANE value of your projection matrix in the MasterRenderer, or by changing "new PixelFormat()"
             to "new PixelFormat().withDepthBits(24)" when creating the display.
             */



            DisplayMode mode = new DisplayMode(width, height);
            //DisplayMode mode = Display.getDesktopDisplayMode();
            Display.setDisplayMode(mode);
            Display.create(new PixelFormat(), attribs);
            Display.setTitle(title);
            GL11.glEnable(GL13.GL_MULTISAMPLE);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0, width, height);
        lastFrameTime = getCurrentTime();
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }


    // returns current time in milliseconds
    private static long getCurrentTime() {
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
		
	public static void render()
	{
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static void dispose()
	{
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}
	
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
	
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	public static String getTitle()
	{
		return Display.getTitle();
	}

}
