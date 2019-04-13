package com.base.engine;

import java.util.ArrayList;

public class Level
{
    private static final int NUM_TEX_EXP = 4;
    private static final int NUM_TEXTURES = (int)Math.pow(2, NUM_TEX_EXP);

    // wolfenstein 3D
    private static final float SPOT_WIDTH = 1;
    private static final float SPOT_LENGTH = 1;
    private static final float SPOT_HEIGHT = 1;

    private Mesh mesh;
    private Bitmap level;
    private Shader shader;
    private Material material;
    private Transform transform;

    // Level("level1.png", "wolf.png")
    public Level(String levelName, String textureName)
    {
        level = new Bitmap(levelName).flipY();

        shader = BasicShader.getInstance();
        material = new Material(new Texture(textureName), new Vector3f(1, 1, 1));

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

                // texture coords for floor and ceiling
                int texX = ((level.getPixel(i, j) & 0x00FF00) >> 8); // green component (0-255)
                texX /= NUM_TEXTURES; // wolf.png has 16 textures: 0-15

                int texY = texX % NUM_TEX_EXP; // 0->0, 1->1, 2->2, 3->3, 4->0, etc.
                texX /= NUM_TEX_EXP;           // 0->0, 1->0, 2->0, 3->0, 4->1, etc.

                float XHigher = 1f - (float)texX/(float)NUM_TEX_EXP;
                float XLower = XHigher - 1f/(float)NUM_TEX_EXP;
                // YHigher and YLower swapped to flip textures upside down
                float YLower = 1f - (float)texY/(float)NUM_TEX_EXP;
                float YHigher = YLower - 1f/(float)NUM_TEX_EXP;

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

                // texture coordinates for walls
                texX = ((level.getPixel(i, j) & 0xFF0000) >> 16); // red component (0-255)
                texX /= NUM_TEXTURES;

                texY = texX % NUM_TEX_EXP;
                texX /= NUM_TEX_EXP;

                XHigher = 1f - (float)texX/(float)NUM_TEX_EXP;
                XLower = XHigher - 1f/(float)NUM_TEX_EXP;
                // YHigher and YLower swapped to flip textures upside down
                YLower = 1f - (float)texY/(float)NUM_TEX_EXP;
                YHigher = YLower - 1f/(float)NUM_TEX_EXP;

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

    }

    public void input()
    {

    }

    public void update()
    {

    }

    public void render()
    {
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }

}
