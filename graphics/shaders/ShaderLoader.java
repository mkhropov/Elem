package graphics.shaders;

import java.io.FileReader;
import java.io.BufferedReader;

import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderLoader {

	public static int createShader(String vertexShaderFname,
					  String fragmentShaderFname){
		BufferedReader f;
		String s;
		String code;
		int l;

		try {
			f =  new BufferedReader(
				new FileReader("graphics/shaders/"+vertexShaderFname));
		} catch (java.io.FileNotFoundException e) {
//			f.close();
			return 0;
		}
		code = "";
		try {
			while ((s = f.readLine()) != null)
				code += s+"\n";
		} catch (java.io.IOException e) {
	//		f.close();
		}
		int vsh = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vsh, code);
		glCompileShader(vsh);
		l = ARBShaderObjects.glGetObjectParameteriARB(vsh,
			ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
		if (l > 1){
			System.out.println("Errors at compiling vertex shader "+
				vertexShaderFname+":");
			System.out.print(ARBShaderObjects.glGetInfoLogARB(vsh, l));
		}

		try {
			f =  new BufferedReader(
				new FileReader("graphics/shaders/"+fragmentShaderFname));
		} catch (java.io.FileNotFoundException e) {
//			f.close();
			return 0;
		}
		code = "";
		try {
			while ((s = f.readLine()) != null)
				code += s+"\n";
		} catch (java.io.IOException e) {
//			f.close();
		}
		int fsh = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fsh, code);
		glCompileShader(fsh);
		l = ARBShaderObjects.glGetObjectParameteriARB(fsh,
			ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
		if (l > 1){
			System.out.println("Errors at compiling fragment shader "+
				fragmentShaderFname+":");
			System.out.print(ARBShaderObjects.glGetInfoLogARB(fsh, l));
		}

		int p = glCreateProgram();
		glAttachShader(p, vsh);
		glAttachShader(p, fsh);
		glLinkProgram(p);
		l = ARBShaderObjects.glGetObjectParameteriARB(p,
			ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
		System.out.print(ARBShaderObjects.glGetInfoLogARB(p, l));
		glDetachShader(p, fsh);
		glDetachShader(p, vsh);

		glDeleteShader(fsh);
		glDeleteShader(vsh);

		return p;
	}

	public static void deleteShader(int p){
		glDeleteProgram(p);
	}
}
