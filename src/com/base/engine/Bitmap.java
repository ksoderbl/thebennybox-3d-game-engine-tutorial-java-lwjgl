package com.base.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bitmap
{
    private int width;
    private int height;
    private int[] pixels;

    public Bitmap(String fileName)
    {
        BufferedImage image = null;

        // IntelliJ IDEA: Ctrl + Alt + T to surround with try catch
        try {
            image = ImageIO.read(new File("./res/bitmaps/" + fileName));

            width = image.getWidth();
            height = image.getHeight();

            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Bitmap(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int[] getPixels()
    {
        return pixels;
    }

    public int getPixel(int x, int y)
    {
        return pixels[x + y * width];
    }

    public void setPixel(int x, int y, int value)
    {
        pixels[x + y * width] = value;
    }
}
