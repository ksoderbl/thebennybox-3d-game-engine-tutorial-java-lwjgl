package com.base.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

public class ResourceLoader
{
	public static String loadShader(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try
		{
			//String wd = Paths.get(".").toAbsolutePath().normalize().toString();
			//System.out.println("path = " +wd);
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName + ".glsl"));
			String line;
			while ((line = shaderReader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return shaderSource.toString();
	}
}
