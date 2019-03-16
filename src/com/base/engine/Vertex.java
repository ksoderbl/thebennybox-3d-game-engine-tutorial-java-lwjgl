package com.base.engine;

public class Vertex
{
	public static final int POS_SIZE = 3;
	public static final int TEXCOORD_SIZE = 2;
	public static final int NORMAL_SIZE = 3;
	public static final int SIZE = POS_SIZE + TEXCOORD_SIZE + NORMAL_SIZE;

	private Vector3f pos;
	private Vector2f texCoord;

	private Vector3f normal;

	public Vertex(Vector3f pos)
	{
		this(pos, new Vector2f(0,0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord)
	{
		this(pos, texCoord, new Vector3f(0,0, 0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal)
	{
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
	}

	public Vector3f getPos()
	{
		return pos;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public Vector2f getTexCoord()
	{
		return texCoord;
	}

	public void setTexCoord(Vector2f texCoord)
	{
		this.texCoord = texCoord;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
}
