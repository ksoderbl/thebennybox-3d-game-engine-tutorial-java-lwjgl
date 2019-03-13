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
	private static final String VERTEX_FILE = "basicVertex";
	private static final String FRAGMENT_FILE = "basicFragment";
	
	private Mesh mesh;
	private Loader loader;
	private Shader shader;
	float[] vertices = {
	      	-1f,-1f,0f,
        	1f,-1f,0f,
        	0f,1f,0f,
        };
	
	
	public Game()
	{
		Vertex[] data = new Vertex[] { 
				new Vertex(new Vector3f(-1f,-1f,0f)),
				new Vertex(new Vector3f(0f,1f,0f)),
				new Vertex(new Vector3f(1f,-1f,0f))
				};

		loader = new Loader();
        shader = new Shader();
        shader.addVertexShader(ResourceLoader.loadShader(VERTEX_FILE));
        shader.addFragmentShader(ResourceLoader.loadShader(FRAGMENT_FILE));
        shader.compileShader();
        //int[] indices = {
        //	0,1,2	
        //};
        
        //mesh = loader.loadToVAO(vertices, 3);

		mesh = new Mesh();
		
		mesh.addVertices(loader, data);

	}
	
	public void input()
	{
		if (Input.getKeyDown(Keyboard.KEY_UP))
			System.out.println("We've just pressed up!");
		if (Input.getKeyUp(Keyboard.KEY_UP))
			System.out.println("We've just released up!");
		if (Input.getMouseDown(1))
			System.out.println("We've just right clicked at " + Input.getMousePosition());
		if (Input.getMouseUp(1))
			System.out.println("We've just released right mouse button!");
	}
	
	public void update()
	{
	}
	
	public void render()
	{
       	shader.bind();

       	mesh.draw();
       	
       	shader.stop();
       	//Window.render();
	}
	
	public void cleanUp()
	{
        shader.cleanUp();
        loader.cleanUp();
        Window.dispose();
	}
}
