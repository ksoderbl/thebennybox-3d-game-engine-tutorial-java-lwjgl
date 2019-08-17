package com.base.engine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import com.base.engine.core.Vector3f;


public class RenderUtil
{
	public static void clearScreen()
	{
		//TODO: Stencil Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static void setTextures(boolean enabled)
	{
		if (enabled) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		else {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	public static void unbindTextures()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public static void setClearColor(Vector3f color)
	{
		GL11.glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}

	public static void initGraphics()
	{
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL32.GL_DEPTH_CLAMP);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static String getOpenGLVersion()
	{
		return GL11.glGetString(GL11.GL_VERSION);
	}
}
