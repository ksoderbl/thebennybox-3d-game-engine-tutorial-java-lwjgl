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
	private boolean tutorial31stuff = false;

	// wolfenstein 3D
	private boolean wolfenstein3Dclone = true;
	private Level level;
	private Player player;

	public Game()
	{
		if (wolfenstein3Dclone)
		{
			level = new Level("level1.png", "wolf.png");
			player = new Player(new Vector3f(0,0.4375f,0));

			Transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
			Transform.setCamera(player.getCamera());

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
			level.input();
			//Transform.getCamera().input();
			player.input();
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
			player.update();
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
			player.render();
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
