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
	// tutorial 31, wolfenstein 3D
	private Bitmap level;
	private Shader shader;
	private Material material;
	private Mesh mesh;
	private Transform transform;
	private Camera camera;

	// wolfenstein 3D
	private static final float SPOT_WIDTH = 1;
	private static final float SPOT_LENGTH = 1;
	private static final float SPOT_HEIGHT = 1;
	
	// tutorial 31
	private boolean tutorial31stuff = false;
	private boolean wolfenstein3Dclone = !tutorial31stuff;

	PointLight tutorial31PLight1 = new PointLight(
			new BaseLight( new Vector3f(1,0.5f,0), 0.8f ),
			new Attenuation(0, 0, 1 ),
			new Vector3f(-2, 0, 5	),
			10
	);
	PointLight tutorial31PLight2 = new PointLight(
			new BaseLight( new Vector3f(0,0.5f,1), 0.8f ),
			new Attenuation(0, 0, 1 ),
			new Vector3f(2, 0, 7	),
			10
	);

	SpotLight tutorial31SLight1 = new SpotLight(new PointLight(
			new BaseLight( new Vector3f(1,1,1), 1.0f ),
			new Attenuation(0, 0, 0.1f ),
			new Vector3f(-2, 0, 5	),
			10),
			new Vector3f(1,1,1), 0.7f);

	public Game()
	{
		if (wolfenstein3Dclone)
		{
			level = new Bitmap("level1.png").flipY();

			// This transposes the level, which is not necessarily what we want.
			//for (int i = 0; i < level.getWidth(); i++)
			//{
			//	for (int j = 0; j < level.getHeight(); j++)
			//	{
			//		int value = level.getPixel(i, j);
			//		//System.out.print(value);
			//		// white is -1
			//		if (value == -1) {
			//			System.out.print(".");
			//		}
			//		else {
			//			System.out.print("#");
			//		}
			//	}
			//	System.out.println();
			//}

			shader = BasicShader.getInstance();
			material = new Material(new Texture("wolf.png"), new Vector3f(1, 1, 1));

			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			ArrayList<Integer> indices = new ArrayList<Integer>();

			for (int i = 0; i < level.getWidth(); i++)
			{
				for (int j = 0; j < level.getHeight(); j++)
				{
					if ((level.getPixel(i, j) & 0xFFFFFF) == 0) {
						// inside wall
						continue;
					}

					float XHigher = 1;
					float XLower = 0;
					float YHigher = 1;
					float YLower = 0;

					//Generate floor
					indices.add(vertices.size() + 2);
					indices.add(vertices.size() + 1);
					indices.add(vertices.size() + 0);
					indices.add(vertices.size() + 3);
					indices.add(vertices.size() + 2);
					indices.add(vertices.size() + 0);

					vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XLower, YLower)));
					vertices.add(new Vertex(new Vector3f((i + 1)  * SPOT_WIDTH,0,j * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
					vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,0,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
					vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH,0,(j + 1) * SPOT_LENGTH), new Vector2f(XLower, YHigher)));

					//Generate ceiling
					indices.add(vertices.size() + 0);
					indices.add(vertices.size() + 1);
					indices.add(vertices.size() + 2);
					indices.add(vertices.size() + 0);
					indices.add(vertices.size() + 2);
					indices.add(vertices.size() + 3);

					vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XLower, YLower)));
					vertices.add(new Vertex(new Vector3f((i + 1)  * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
					vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
					vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XLower, YHigher)));

					//Generate Walls
					if ((level.getPixel(i, j - 1) & 0xFFFFFF) == 0)
					{
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 1);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 3);

						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, 0,j * SPOT_LENGTH), new Vector2f(XLower, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1)  * SPOT_WIDTH, 0,j * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XLower, YHigher)));
					}

					if ((level.getPixel(i, j + 1) & 0xFFFFFF) == 0)
					{
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 1);
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 3);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 0);

						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, 0,(j + 1) * SPOT_LENGTH), new Vector2f(XLower, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1)  * SPOT_WIDTH, 0,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XLower, YHigher)));
					}

					if ((level.getPixel(i - 1, j) & 0xFFFFFF) == 0)
					{
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 1);
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 3);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 0);

						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, 0,j * SPOT_LENGTH), new Vector2f(XLower, YLower)));
						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, 0,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
						vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XLower, YHigher)));
					}

					if ((level.getPixel(i + 1, j) & 0xFFFFFF) == 0)
					{
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 1);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 0);
						indices.add(vertices.size() + 2);
						indices.add(vertices.size() + 3);

						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, 0,j * SPOT_LENGTH), new Vector2f(XLower, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, 0,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YLower)));
						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(XHigher, YHigher)));
						vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(XLower, YHigher)));
					}

				}
			}


			//Vertex[] vertices = new Vertex[]{
			//		new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)),
			//		new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)),
			//		new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)),
			//		new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0))
			//};
			//int[] indices = new int[] {
			//	0, 1, 2,
			//	0, 2, 3
			//}

			Vertex[] vertArray = new Vertex[vertices.size()];
			Integer[] intArray = new Integer[indices.size()];

			vertices.toArray(vertArray);
			indices.toArray(intArray);

			mesh = new Mesh(vertArray, Util.toIntArray(intArray));
			transform = new Transform();
			Transform.setCamera(new Camera());
			transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);

			System.out.println("=====================================================================");
			System.out.println("Use a, s, d, w to move and mouse to change direction.");
			System.out.println("Activate mouse control by clicking, use Esc to exit mouse control.");
			System.out.println("=====================================================================");
		}

		if (tutorial31stuff)
		{
			//mesh = new Mesh();
			material = new Material(new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);
			shader = PhongShader.getInstance();
			camera = new Camera();
			transform = new Transform();

			/*
			Vertex[] data = new Vertex[] {
					new Vertex(new Vector3f(-1f,-1f,-1f), new Vector2f(0f,0f)),
					new Vertex(new Vector3f(1f,-1f,-1f), new Vector2f(0f,1f)),
					new Vertex(new Vector3f(0f,-1f,1f), new Vector2f(1.0f,1f)),
					new Vertex(new Vector3f(0f,1f,0f), new Vector2f(1f,0f))
							};
			int[] indices = new int[] {
					0,3,1,
					2,1,3,
					2,0,1,
					3,0,2
			};

			mesh.addVertices(data, indices, true);
			*/

			/*
			Vertex[] vertices = new Vertex[] {
					new Vertex ( new Vector3f(-1, -1 , 0.5773f), new Vector2f(0.0f, 0.0f) ),
					new Vertex ( new Vector3f( 0, -1 , -1.15475f), new Vector2f(0.5f, 0.0f) ),
					new Vertex ( new Vector3f( 1, -1 , 0.5773f), new Vector2f(1.0f, 0.0f) ),
					new Vertex ( new Vector3f(0, 1 , 0), new Vector2f(0.5f, 1.0f) ),
			};
			int[] indices = new int[] {
					3,1,0,
					2,1,3,
					0,1,2,
					0,2,3
			};
			*/


			float fieldDepth = 10.0f;
			float fieldWidth = 10.0f;

			Vertex[] vertices = new Vertex[]{
					new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
					new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
					new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
					new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))
			};

			int indices[] = {
					0, 1, 2,
					2, 1, 3
			};

			mesh = new Mesh(vertices, indices, true);
			//	mesh = new Mesh("box.obj");

			transform.setProjection(70, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
			Transform.setCamera(camera);

			PhongShader.setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));
			PhongShader.setDirectionalLight(
					new DirectionalLight(
							new BaseLight(new Vector3f(1, 1, 1), 0.1f),
							new Vector3f(1f, 1f, 1f)));


			PhongShader.setPointLights(new PointLight[]{tutorial31PLight1, tutorial31PLight2});
			PhongShader.setSpotLights(new SpotLight[]{tutorial31SLight1});
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
	}
	
	float temp = 0.0f;
	
	public void update()
	{
		if (wolfenstein3Dclone)
		{

		}

		if (tutorial31stuff)
		{
			temp += Time.getDelta();

			//float sinTemp = (float)Math.sin(temp);

			transform.setTranslation(0, -1f, 5);

			tutorial31PLight1.setPosition(new Vector3f(3, 0, 8.0f * (float) (Math.sin(temp) + 1.0 / 2.0) + 10));
			tutorial31PLight2.setPosition(new Vector3f(7, 0, 8.0f * (float) (Math.cos(temp) + 1.0 / 2.0) + 10));

			//transform.setRotation(0, sinTemp * 180, 0);
			//float sc = 1.0f;
			//transform.setScale(sc, sc, sc);

			tutorial31SLight1.getPointLight().setPosition(camera.getPos());
			tutorial31SLight1.setDirection(camera.getForward());
		}
 	}
	
	public void render()
	{
		if (wolfenstein3Dclone)
		{
			shader.bind();
			shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
			mesh.draw();
		}

		if (tutorial31stuff)
		{
			RenderUtil.setClearColor(Transform.getCamera().getPos().div(2048f).abs());
			shader.bind();
			shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
			mesh.draw();

			//Window.render();
		}
	}
	
	public void cleanUp()
	{
		if (wolfenstein3Dclone)
		{

		}

		if (tutorial31stuff)
		{
			Window.dispose();
		}
	}
}
