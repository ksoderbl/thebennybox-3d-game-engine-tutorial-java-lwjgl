package com.base.engine;

public class Tutorial
{
    private Shader shader;
    private Material material;
    private Mesh mesh;
    private Transform transform;
    private Camera camera;

    PointLight PLight1 = new PointLight(
            new BaseLight( new Vector3f(1,0.5f,0), 0.8f ),
            new Attenuation(0, 0, 1 ),
            new Vector3f(-2, 0, 5	),
            10
    );
    PointLight PLight2 = new PointLight(
            new BaseLight( new Vector3f(0,0.5f,1), 0.8f ),
            new Attenuation(0, 0, 1 ),
            new Vector3f(2, 0, 7	),
            10
    );

    SpotLight SLight1 = new SpotLight(new PointLight(
            new BaseLight( new Vector3f(1,1,1), 1.0f ),
            new Attenuation(0, 0, 0.1f ),
            new Vector3f(-2, 0, 5	),
            10),
            new Vector3f(1,1,1), 0.7f);

    public Tutorial()
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

        Transform.setProjection(70, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
        Transform.setCamera(camera);

        PhongShader.setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));
        PhongShader.setDirectionalLight(
                new DirectionalLight(
                        new BaseLight(new Vector3f(1, 1, 1), 0.1f),
                        new Vector3f(1f, 1f, 1f)));


        PhongShader.setPointLights(new PointLight[]{PLight1, PLight2});
        PhongShader.setSpotLights(new SpotLight[]{SLight1});
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

        //float sinTemp = (float)Math.sin(temp);

        transform.setTranslation(0, -1f, 5);

        PLight1.setPosition(new Vector3f(3, 0, 8.0f * (float) (Math.sin(temp) + 1.0 / 2.0) + 10));
        PLight2.setPosition(new Vector3f(7, 0, 8.0f * (float) (Math.cos(temp) + 1.0 / 2.0) + 10));

        //transform.setRotation(0, sinTemp * 180, 0);
        //float sc = 1.0f;
        //transform.setScale(sc, sc, sc);

        SLight1.getPointLight().setPosition(camera.getPos());
        SLight1.setDirection(camera.getForward());
    }

    public void render()
    {
        RenderUtil.setClearColor(Transform.getCamera().getPos().div(2048f).abs());
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        //Window.render();
    }

    public void cleanUp()
    {
        Window.dispose();
    }
}
