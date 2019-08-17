package com.base.engine.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import com.base.engine.core.Util;

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
        boolean useSlickUtil = false;
        
        if (useSlickUtil) {
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
        }
        else {
        	// #12 Wolfenstein 3D Clone Tutorial: Enemies, Part 1
        	// Texture loader that does not use offsets
	        try
	        {
	        	BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
	        	
	        	boolean hasAlpha = image.getColorModel().hasAlpha();
	        	
	        	int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	        	
	        	ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);
	        	
	        	for (int y = 0; y < image.getHeight(); y++)
	        	{
	        		for (int x = 0; x < image.getWidth(); x++)
	        		{
	        			int pixel = pixels[y * image.getWidth() + x];
	        			
	        			buffer.put((byte) ((pixel >> 16) & 0xFF));
	        			buffer.put((byte) ((pixel >>  8) & 0xFF));
	        			buffer.put((byte) ((pixel >>  0) & 0xFF));
	        			if (hasAlpha) {
	        				buffer.put((byte) ((pixel >> 24) & 0xFF));
	        			}
	        			else {
	        				buffer.put((byte) (0xFF));
	        			}
	        		}
	        	}
        	
	        	buffer.flip();
	        	
	        	int texture = GL11.glGenTextures();
	        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	        	
	        	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	        	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	        	
	        	GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	        	GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	        	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	        	
	        	return texture;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            System.exit(1);
	        }
        
        }

        return 0;
    }
}
