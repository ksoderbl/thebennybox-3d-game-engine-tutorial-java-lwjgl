package com.base.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;

//import org.lwjgl.util.vector.Matrix4f;
//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class Shader
{
    private int program;
    private HashMap<String, Integer> uniforms;
    
    private int vertexShaderID;
    private int fragmentShaderID;
    
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader()
    {
    	program = GL20.glCreateProgram();
    	uniforms = new HashMap<String, Integer>();
    }
    
    public Shader(String vertexFile, String fragmentFile)
    {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShaderID);
        GL20.glAttachShader(program, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
        getAllUniformLocations();
    }

    public void bind()
    {
        GL20.glUseProgram(program);
    }

    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
    {

    }

    // same as getUniformLocation
    public void addUniform(String uniform)
    {
    	int uniformLocation = GL20.glGetUniformLocation(program, uniform);
    	
    	System.out.println("addUniform, program: " + program);
    	System.out.println("addUniform, uniform: " + uniform);
    	System.out.println("addUniform, got location: " + uniformLocation);
    	
    	if (uniformLocation < 0) {
    		System.err.println("Error: Could not get uniform location: " + uniform);
    		new Exception().printStackTrace();
    		System.exit(1);
    	}
    	
    	uniforms.put(uniform, uniformLocation);
    }
    
    public void setUniformi(String uniformName, int value)
    {
    	GL20.glUniform1i(uniforms.get(uniformName), value);
    }
    
    public void setUniformf(String uniformName, float value)
    {
    	GL20.glUniform1f(uniforms.get(uniformName), value);
    }
    
    public void setUniform(String uniformName, Vector3f value)
    {
    	GL20.glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(String uniformName, Matrix4f value)
    {
    	GL20.glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
    }
    
    
	public void addVertexShader(String text)
	{
		addProgram(text, GL20.GL_VERTEX_SHADER);
	}

	public void addFragmentShader(String text)
	{
		addProgram(text, GL20.GL_FRAGMENT_SHADER);
	}
	
	public void addGeometryShader(String text)
	{
		addProgram(text, GL32.GL_GEOMETRY_SHADER);
	}
	
	private int addProgram(String text, int type)
	{
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, text);
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shader, 1024));
            System.err.println("Could not compile shader " + text);
            System.exit(-1);
        }
        GL20.glAttachShader(program, shader);
        return shader;
	}
	
	public void compileShader()
	{
	    bindAttributes();
	    GL20.glLinkProgram(program);
	    GL20.glValidateProgram(program);
	    getAllUniformLocations();
		
		/*
		glBindAttribLocation(program, 0, "position");
		
		glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
        {
            System.out.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE)
        {
            System.out.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }
        */
	}

    protected void getAllUniformLocations()
    {
    	
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(program, uniformName);
    }

    //public void start() {
    //        GL20.glUseProgram(program);
    //}

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(program, vertexShaderID);
        GL20.glDetachShader(program, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(program);
    }

    protected void bindAttributes()
    {
    	bindAttribute(0, "position");
    }

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(program, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected void load2DVector(int location, org.lwjgl.util.vector.Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    protected void loadVector(int location, org.lwjgl.util.vector.Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, org.lwjgl.util.vector.Vector4f vector) {
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, org.lwjgl.util.vector.Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                //shaderSource.append(line).append("\n");
                shaderSource.append(line).append("//\n"); // not sure what the // is about
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + file + "\" not found!");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Could not read file: \"" + file + "\"!" );
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 1024));
            System.err.println("Could not compile shader " + file);
            System.exit(-1);
        }
        return shaderID;
    }
}
