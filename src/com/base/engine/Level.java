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

    // temp variable
    private Door door;

    public Level(String levelName, String textureName)
    {
        level = new Bitmap(levelName).flipY();
        material = new Material(new Texture(textureName), new Vector3f(1, 1, 1));
        transform = new Transform();

        shader = BasicShader.getInstance();

        generateLevel();

        Transform doorTransform = new Transform();
        doorTransform.setTranslation(7.5f,0,8);
        door = new Door(doorTransform, material);
    }

    public void input()
    {

    }

    public void update()
    {
        door.update();
    }

    public void render()
    {
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        door.render();
    }

    public Vector3f checkCollision(Vector3f oldPos, Vector3f newPos, float objectWidth, float objectLength)
    {
        Vector2f collisionVector = new Vector2f(1, 1);
        Vector3f movementVector = newPos.sub(oldPos);

        if (movementVector.length() > 0)
        {
            Vector2f blockSize = new Vector2f(SPOT_WIDTH, SPOT_LENGTH);
            Vector2f objectSize = new Vector2f(objectWidth, objectLength);

            Vector2f oldPos2 = new Vector2f(oldPos.getX(), oldPos.getZ());
            Vector2f newPos2 = new Vector2f(newPos.getX(), newPos.getZ());

            for (int i = 0; i < level.getWidth(); i++)
            {
                for (int j = 0; j < level.getHeight(); j++)
                {
                    if((level.getPixel(i, j) & 0xFFFFFF) == 0)
                    {
                        collisionVector = collisionVector.mul(rectCollide(oldPos2, newPos2, objectSize, blockSize.mul(new Vector2f(i, j)), blockSize));
                    }
                }
            }

            Vector2f doorSize = new Vector2f(Door.LENGTH, Door.WIDTH);

            Vector3f doorPos3f = door.getTransform().getTranslation();
            Vector2f doorPos2f = new Vector2f(doorPos3f.getX(), doorPos3f.getZ());
            collisionVector = collisionVector.mul(rectCollide(oldPos2, newPos2, objectSize, doorPos2f, doorSize));
        }

        return new Vector3f(collisionVector.getX(), 0, collisionVector.getY());
    }

    private Vector2f rectCollide(Vector2f oldPos, Vector2f newPos, Vector2f size1, Vector2f pos2, Vector2f size2)
    {
        Vector2f result = new Vector2f(0,0);

        if (newPos.getX() + size1.getX() < pos2.getX() ||
                newPos.getX() - size1.getX() > pos2.getX() + size2.getX() * size2.getX() ||
                oldPos.getY() + size1.getY() < pos2.getY() ||
                oldPos.getY() - size1.getY() > pos2.getY() + size2.getY() * size2.getY())
            result.setX(1);
        if (oldPos.getX() + size1.getX() < pos2.getX() ||
                oldPos.getX() - size1.getX() > pos2.getX() + size2.getX() * size2.getX() ||
                newPos.getY() + size1.getY() < pos2.getY() ||
                newPos.getY() - size1.getY() > pos2.getY() + size2.getY() * size2.getY())
            result.setY(1);
        return result;
    }

    private void addFace(ArrayList<Integer> indices, int startLocation, boolean direction)
    {
        if (direction) {
            indices.add(startLocation + 2);
            indices.add(startLocation + 1);
            indices.add(startLocation + 0);
            indices.add(startLocation + 3);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
        }
        else {
            indices.add(startLocation + 0);
            indices.add(startLocation + 1);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
            indices.add(startLocation + 2);
            indices.add(startLocation + 3);
        }
    }

    private float[] calcTexCoords(int value)
    {
        int texX = value; // e.g. green component (0-255)
        texX /= NUM_TEXTURES; // wolf.png has 16 textures: 0-15

        int texY = texX % NUM_TEX_EXP; // 0->0, 1->1, 2->2, 3->3, 4->0, etc.
        texX /= NUM_TEX_EXP;           // 0->0, 1->0, 2->0, 3->0, 4->1, etc.

        float[] result = new float[4];

        float XHigher = 1f - (float)texX/(float)NUM_TEX_EXP;
        float XLower = XHigher - 1f/(float)NUM_TEX_EXP;
        // YHigher and YLower swapped to flip textures upside down
        float YLower = 1f - (float)texY/(float)NUM_TEX_EXP;
        float YHigher = YLower - 1f/(float)NUM_TEX_EXP;

        result[0] = XHigher;
        result[1] = XLower;
        result[2] = YHigher;
        result[3] = YLower;

        return result;
    }

    private void addVertices(ArrayList<Vertex> vertices, int i, int j, float offset, boolean x, boolean y, boolean z, float[] texCoords)
    {
        if (x && z) {
            vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, offset * SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, offset * SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, offset * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, offset * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else if (x && y) {
            vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, j * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1)  * SPOT_WIDTH, j * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f(i * SPOT_WIDTH, (j + 1) * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else if (y && z) {
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, i * SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, i * SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, (i + 1) * SPOT_HEIGHT,(j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, (i + 1) * SPOT_HEIGHT,j * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else {
            System.err.println("Invalid plane used in level generator");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void generateLevel()
    {
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
                int texX = ((level.getPixel(i, j) & 0x00FF00) >> 8);
                float[] texCoords = calcTexCoords(texX);

                //Generate floor
                addFace(indices, vertices.size(), true);
                addVertices(vertices, i, j, 0, true, false, true, texCoords);

                //Generate ceiling
                addFace(indices, vertices.size(), false);
                addVertices(vertices, i, j, 1,true, false, true, texCoords);

                //Generate Walls
                // texture coordinates for walls
                texX = ((level.getPixel(i, j) & 0xFF0000) >> 16); // red component (0-255)
                texCoords = calcTexCoords(texX);

                if ((level.getPixel(i, j - 1) & 0xFFFFFF) == 0)
                {
                    addFace(indices, vertices.size(), false);
                    addVertices(vertices, i, 0, j, true, true, false, texCoords);
                }

                if ((level.getPixel(i, j + 1) & 0xFFFFFF) == 0)
                {
                    addFace(indices, vertices.size(), true);
                    addVertices(vertices, i, 0, (j + 1), true, true, false, texCoords);
                }

                if ((level.getPixel(i - 1, j) & 0xFFFFFF) == 0)
                {
                    addFace(indices, vertices.size(), true);
                    addVertices(vertices, 0, j, i,false, true, true, texCoords);
                }

                if ((level.getPixel(i + 1, j) & 0xFFFFFF) == 0)
                {
                    addFace(indices, vertices.size(), false);
                    addVertices(vertices, 0, j, (i + 1), false, true, true, texCoords);
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
    }

    public Shader getShader()
    {
        return shader;
    }
}
