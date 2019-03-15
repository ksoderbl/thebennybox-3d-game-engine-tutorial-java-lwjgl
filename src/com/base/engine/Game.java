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
	private Transform transform;
	
	float[] vertices = {
	      	-1f,-1f,0f,
        	1f,-1f,0f,
        	0f,1f,0f,
        };
	
	
	public Game()
	{
		mesh = new Mesh();
		shader = new Shader();
		loader = new Loader();
		
		Vertex[] data = new Vertex[] { 
				new Vertex(new Vector3f(-1f,-1f,0f)),
				new Vertex(new Vector3f(0f,1f,0f)),
				new Vertex(new Vector3f(1f,-1f,0f))
				};

		mesh.addVertices(loader, data);
		
		transform = new Transform();
        		
        shader.addVertexShader(ResourceLoader.loadShader(VERTEX_FILE));
        shader.addFragmentShader(ResourceLoader.loadShader(FRAGMENT_FILE));
        shader.compileShader();
        shader.addUniform("transform");
        
        //int[] indices = {
        //	0,1,2	
        //};
        
        //mesh = loader.loadToVAO(vertices, 3);
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
	
	float temp = 0.0f;
	
	public void update()
	{
		temp += Time.getDelta();
		
		float sinTemp = (float)Math.sin(temp);
		
		transform.setTranslation(sinTemp, 0, 0);
		transform.setRotation(0, 0, sinTemp * 180);
		transform.setScale(sinTemp, sinTemp, sinTemp);
 	}
	
	public void render()
	{
       	shader.bind();
		shader.setUniform("transform", transform.getTransformation());

       	mesh.draw();
       	
       	//shader.stop(); // For #11 3D Game Engine Tutorial: Uniforms, shaders has to be in use to set uniform variables

       	//Window.render();
	}
	
	public void cleanUp()
	{
        shader.cleanUp();
        loader.cleanUp();
        Window.dispose();
	}
}
