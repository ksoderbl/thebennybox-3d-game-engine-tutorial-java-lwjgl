package com.base.engine.core;

public class Quaternion
{
	private float x, y, z, w;

	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize()
	{
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion r)
	{
		float ww = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float xx = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float yy = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float zz = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(xx, yy, zz, ww);
	}
	
	public Quaternion mul(Vector3f r)
	{
		float ww = - x * r.getX() - y * r.getY() - z * r.getZ();
		float xx =   w * r.getX() + y * r.getZ() - z * r.getY();
		float yy =   w * r.getY() + z * r.getX() - x * r.getZ();
		float zz =   w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(xx, yy, zz, ww);
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
	
	
		
}
