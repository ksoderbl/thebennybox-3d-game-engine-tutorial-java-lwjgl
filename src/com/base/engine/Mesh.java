package com.base.engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.*;

public class Mesh
{
    private int vaoID;
    private int vertexCount;
    
    public Mesh()
    {
        this.vaoID = 0;
        this.vertexCount = 0;
  	}

    public Mesh(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
    
    public int createVAO()
    {
        int vaoID = GL30.glGenVertexArrays();
        //vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    
    public void bindIndicesBuffer(int[] indices)
    {
        int vboID = GL15.glGenBuffers();
        //vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = Util.createFlippedBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
    {
        int vboID = GL15.glGenBuffers();
        //vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = Util.createFlippedBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public void unbindVAO()
    {
        GL30.glBindVertexArray(0);
    }

    public void addVertices(Vertex[] vertices, int[] indices)
    {
        addVertices(vertices, indices, false);
    }

    public void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        if (calcNormals) {
            calcNormals(vertices, indices);
        }

    	float[] verticesArray = new float[vertices.length * Vertex.POS_SIZE];
        float[] textureCoords = new float[vertices.length * Vertex.TEXCOORD_SIZE];
        float[] normalsArray = new float[vertices.length * Vertex.NORMAL_SIZE];
    	
    	int j = 0;
    	int k = 0;
    	int l = 0;
    	for (int i = 0; i < vertices.length; i++) {
    		Vertex v = vertices[i];
    		verticesArray[j++] = v.getPos().getX();
    		verticesArray[j++] = v.getPos().getY();
    		verticesArray[j++] = v.getPos().getZ();
    		textureCoords[k++] = v.getTexCoord().getX();
            textureCoords[k++] = v.getTexCoord().getY();
            normalsArray[l++] = v.getNormal().getX();
            normalsArray[l++] = v.getNormal().getY();
            normalsArray[l++] = v.getNormal().getZ();
    	}


    	/*
        int vaoID = loader.createVAO();
        loader.storeDataInAttributeList(0, Vertex.SIZE, vertices);
        loader.unbindVAO();
    	this.vaoID = vaoID;
    	this.vertexCount = vertices.length;
    	*/
    	
    	//Mesh otherMesh = loader.loadToVAO(vertices, indices);
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, Vertex.POS_SIZE, verticesArray);
        storeDataInAttributeList(1, Vertex.TEXCOORD_SIZE, textureCoords);
        storeDataInAttributeList(2, Vertex.NORMAL_SIZE, normalsArray);
        unbindVAO();
    	
    	this.vaoID = vaoID;
    	this.vertexCount = indices.length;
    }
    
    public void draw()
    {
		GL30.glBindVertexArray(getVaoID());
		GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1); // textureCoords
        GL20.glEnableVertexAttribArray(2); // normals
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, .getID());
		//if using indices
		GL11.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		//using only vertices
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, getVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
    }

    private void calcNormals(Vertex[] vertices, int[] indices)
    {
        for (int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f v1 = vertices[i1].getPos().sub( vertices[i0].getPos() );
            Vector3f v2 = vertices[i2].getPos().sub( vertices[i0].getPos() );

            Vector3f normal = v1.cross(v2).normalized();

            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }

        for (int i = 0; i < vertices.length; i++)
        {
            vertices[i].setNormal(vertices[i].getNormal().normalized());
        }
    }
}
