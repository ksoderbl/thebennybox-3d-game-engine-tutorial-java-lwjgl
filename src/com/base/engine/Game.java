package com.base.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Vector4f;
import com.base.engine.Vector3f;

public class Game
{
	// tutorial 31
	private Tutorial tutorial;

	// wolfenstein 3D
	private Level level;

	// tutorial 31
	private boolean tutorial31stuff = true;
	private boolean wolfenstein3Dclone = !tutorial31stuff;

	public Game()
	{
		if (wolfenstein3Dclone)
		{
			Transform.setCamera(new Camera());
			Transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);

			level = new Level("level1.png", "wolf.png");

			System.out.println("=====================================================================");
			System.out.println("Use a, s, d, w to move and mouse to change direction.");
			System.out.println("Activate mouse control by clicking, use Esc to exit mouse control.");
			System.out.println("=====================================================================");
		}

		if (tutorial31stuff)
		{
			tutorial = new Tutorial();
		}
	}
	
	public void input()
	{
		if (wolfenstein3Dclone)
		{
			Transform.getCamera().input();
		}

		if (tutorial31stuff)
		{
			tutorial.input();
		}
	}
	
	public void update()
	{
		if (wolfenstein3Dclone)
		{
			level.update();
		}

		if (tutorial31stuff)
		{
			tutorial.update();
		}
 	}
	
	public void render()
	{
		if (wolfenstein3Dclone)
		{
			level.render();
		}

		if (tutorial31stuff)
		{
			tutorial.render();
		}
	}
	
	public void cleanUp()
	{
		if (wolfenstein3Dclone)
		{

		}

		if (tutorial31stuff)
		{
			tutorial.cleanUp();
		}
	}
}
