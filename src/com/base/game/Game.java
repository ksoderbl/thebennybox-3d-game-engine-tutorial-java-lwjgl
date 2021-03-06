package com.base.game;

import com.base.engine.core.Transform;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Window;

public class Game
{
	// tutorial 31
	private Tutorial tutorial;
	private boolean tutorial31stuff = false;

	// wolfenstein 3D
	private boolean wolfenstein3Dclone = true;
	private static Level level;
	//private Player player;

	public Game()
	{
		if (wolfenstein3Dclone)
		{
			Player player = new Player(new Vector3f(2, 0.4375f, 2));
			level = new Level("level1.png", "WolfCollection.png", player);

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

	public static Level getLevel()
	{
		return level;
	}
}
