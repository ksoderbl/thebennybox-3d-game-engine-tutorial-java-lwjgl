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
	//private Loader loader;
	private Shader shader;
	private Transform transform;
	private Texture texture;
	private Camera camera;
	
	float[] vertices = {
	      	-1f,-1f,0f,
        	1f,-1f,0f,
        	0f,1f,0f,
        };
	
	
	public Game()
	{
		mesh = new Mesh();
		texture = ResourceLoader.loadTexture("test.png");
		shader = new Shader();
		//loader = new Loader();
		camera = new Camera();

		Vertex[] data = new Vertex[] {
				new Vertex(new Vector3f(-1f,-1f,0f), new Vector2f(0f,0f)),
				new Vertex(new Vector3f(0f,1f,0f), new Vector2f(0f,1f)),
				new Vertex(new Vector3f(1f,-1f,0f), new Vector2f(1.0f,1f)),
				new Vertex(new Vector3f(0f,-1f,1f), new Vector2f(1f,0f))
						};
		int[] indices = new int[] {
				3,1,0,
				2,1,3,
				0,1,2,
				0,2,3
		};
		mesh.addVertices(data, indices);

		//Vertex[] data = new Vertex[] {
		//		new Vertex(new Vector3f(-1f,-1f,0f)),
		//		new Vertex(new Vector3f(0f,1f,0f)),
		//		new Vertex(new Vector3f(1f,-1f,0f)),
		//		new Vertex(new Vector3f(0f,0f,1f)),
		//				};
        //int[] indices = new int[] {
        //		0,1,3,
        //		3,1,2,
        //		2,1,0,
        //		0,3,2
        //};
		//mesh.addVertices(data, indices);

		//mesh = ResourceLoader.loadMesh("box.obj");

		transform = new Transform();
		Transform.setCamera(camera);
		transform.setProjection(70, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
        		
        shader.addVertexShader(ResourceLoader.loadShader(VERTEX_FILE));
        shader.addFragmentShader(ResourceLoader.loadShader(FRAGMENT_FILE));
        shader.compileShader();
        shader.addUniform("transform");

        //mesh = loader.loadToVAO(vertices, 3);
	}
	
	public void input()
	{
		camera.input();
		//if (Input.getKeyDown(Keyboard.KEY_UP))
		//	System.out.println("We've just pressed up!");
		//if (Input.getKeyUp(Keyboard.KEY_UP))
		//	System.out.println("We've just released up!");
		//if (Input.getMouseDown(1))
		//	System.out.println("We've just right clicked at " + Input.getMousePosition());
		//if (Input.getMouseUp(1))
		//	System.out.println("We've just released right mouse button!");
	}
	
	float temp = 0.0f;
	
	public void update()
	{
		temp += Time.getDelta();
		
		float sinTemp = (float)Math.sin(temp);
		
		transform.setTranslation(sinTemp, 0, 5);
		transform.setRotation(0, sinTemp * 180, 0);
		float sc = 1.0f;
		transform.setScale(sc, sc, sc);
 	}
	
	public void render()
	{
       	shader.bind();
		shader.setUniform("transform", transform.getProjectedTransformation());
		texture.bind();
       	mesh.draw();
       	
       	//shader.stop(); // For #11 3D Game Engine Tutorial: Uniforms, shaders has to be in use to set uniform variables

       	//Window.render();
	}
	
	public void cleanUp()
	{
        shader.cleanUp();
		//loader.cleanUp();
        Window.dispose();
	}
}
