package com.base.engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {
    private int vaoID;
    private int vertexCount;
    
    public Mesh() {
        this.vaoID = 0;
        this.vertexCount = 0;
  	}

    public Mesh(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
        
    // not efficient, but compatible with bennybox ideas
    public void addVertices(Loader loader, Vertex[] data)
    {
    	float[] vertices = new float[data.length * Vertex.SIZE];
    	
    	int j = 0;
    	for (int i = 0; i < data.length; i++) {
    		Vertex v = data[i];
    		vertices[j++] = v.getPos().getX();
    		vertices[j++] = v.getPos().getY();
    		vertices[j++] = v.getPos().getZ();
    	}
    	
    	//Mesh otherMesh = loader.loadToVAO(vertices, Vertex.SIZE);
    	
        int vaoID = loader.createVAO();
        loader.storeDataInAttributeList(0, Vertex.SIZE, vertices);
        loader.unbindVAO();
    	
    	this.vaoID = vaoID;
    	this.vertexCount = data.length;
    }
    
    public void draw()
    {
		GL30.glBindVertexArray(getVaoID());
		GL20.glEnableVertexAttribArray(0);
		//if using indices
		//GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		//using only vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, getVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
    }
}
