package com.base.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL11;

public class RenderUtil
{
	public static void clearScreen()
	{
		//TODO: Stencil Buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
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
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static void setClearColor(Vector3f color)
	{
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}

	public static void initGraphics()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		//TODO: Depth clamp for later

		glEnable(GL_TEXTURE_2D);
		//gamma correction
		//https://www.youtube.com/watch?v=fkM9vx6ZDdU&list=PLEETnX-uPtBXP_B2yupUKlflXBznWIlL5&index=8
		glEnable(GL_FRAMEBUFFER_SRGB);
	}
	
	public static String getOpenGLVersion()
	{
		return glGetString(GL_VERSION);
	}
}
