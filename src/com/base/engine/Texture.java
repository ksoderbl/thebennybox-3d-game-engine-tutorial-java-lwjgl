package com.base.engine;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;

public class Texture
{
    private int id;

    public Texture(String fileName)
    {
        this(loadTexture(fileName));
    }

    public Texture(int id)
    {
        this.id = id;
    }

    public void bind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public int getId()
    {
        return id;
    }

    private static int loadTexture(String fileName)
    {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        try
        {
            FileInputStream fs = new FileInputStream(new File("./res/textures/" + fileName));
            // GL_NEAREST to disable filtering
            int id = TextureLoader.getTexture(ext, fs, GL11.GL_NEAREST).getTextureID();

            return id;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return 0;
    }
}
