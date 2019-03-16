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
    
    // not efficient, but compatible with bennybox ideas
    public void addVertices(Vertex[] data, int[] indices)
    {
    	float[] vertices = new float[data.length * Vertex.POS_SIZE];
        float[] textureCoords = new float[data.length * Vertex.TEXCOORD_SIZE];
    	
    	int j = 0;
    	int k = 0;
    	for (int i = 0; i < data.length; i++) {
    		Vertex v = data[i];
    		vertices[j++] = v.getPos().getX();
    		vertices[j++] = v.getPos().getY();
    		vertices[j++] = v.getPos().getZ();
    		textureCoords[k++] = v.getTexCoord().getX();
            textureCoords[k++] = v.getTexCoord().getY();
    	}


    	/*
        int vaoID = loader.createVAO();
        loader.storeDataInAttributeList(0, Vertex.SIZE, vertices);
        loader.unbindVAO();
    	this.vaoID = vaoID;
    	this.vertexCount = data.length;
    	*/
    	
    	//Mesh otherMesh = loader.loadToVAO(vertices, indices);
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();
    	
    	this.vaoID = vaoID;
    	this.vertexCount = indices.length;
    }
    
    public void draw()
    {
		GL30.glBindVertexArray(getVaoID());
		GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1); // textureCoords
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, .getID());
		//if using indices
		GL11.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		//using only vertices
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, getVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
    }
}
