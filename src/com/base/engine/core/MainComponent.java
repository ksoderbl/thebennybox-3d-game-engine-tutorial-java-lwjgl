package com.base.engine.core;

import com.base.engine.rendering.RenderUtil;
import com.base.engine.rendering.Window;
import com.base.game.Game;

public class MainComponent
{
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final String TITLE = "#16 Wolfenstein 3D Clone Tutorial: Hit Detection, Part 2: Chasing the Player, #31 3D Game Engine Tutorial: Finishing the Lighting Segment";
	public static final double FRAME_CAP = 5000.0;
	
	private boolean isRunning;
	private Game game;
	
	public MainComponent()
	{
		System.out.println(RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
	}
	
	public void start()
	{
		if (isRunning)
			return;
		
		run();
	}
	
	public void stop()
	{
		if (!isRunning)
			return;
		isRunning = false;
	}
	
	private void run()
	{
		isRunning = true;
		
		int frames = 0;
		long frameCounter = 0;
		
		final double frameTime = 1.0 / FRAME_CAP;
		
		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
        while (isRunning)
        {
			boolean render = false;
			
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime / (double)Time.SECOND;
			frameCounter += passedTime;
			
			while (unprocessedTime > frameTime)
			{
				render = true;
				
				unprocessedTime -= frameTime;
				
				if (Window.isCloseRequested())
					stop();
				
				Time.setDelta(frameTime);
								
				game.input();
				Input.update();
				
				game.update();
				
				if (frameCounter >= Time.SECOND)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render)
			{
				render();
				frames++;
			}
			else {
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

        cleanUp();
	}

	
	private void render()
	{
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}
	
	private void cleanUp()
	{
		game.cleanUp();
		Window.dispose();
	}
	
    public static void main(String[] args)
    {
        Window.createWindow(WIDTH, HEIGHT, TITLE);
      
        MainComponent mainComponent = new MainComponent();
        mainComponent.start();
    }
}

