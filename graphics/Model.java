package graphics;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import graphics.shaders.Matrix4;
import iface.Interface;
import world.World;

public class Model {

	public String name;

	public FloatBuffer v; // vertices - 3 floats for a vertex
	public FloatBuffer t; // UV texture coordinates - 2 floats for a uv
	public FloatBuffer n; // normals - 3 floats for a normal
	public IntBuffer i; // indices - 3 for a facet

	public int v_b; // corresponfing GL buffers
	public int t_b;
	public int n_b;
	public int i_b;

	public float a0, a1, a2;
	public float phi, theta;
	public float s0, s1, s2;

/*	public int v_attr; // corresponding shader attributes
	public int t_attr;
	public int n_attr;*/

//	public Texture texture;
	//FloatBuffer MVP;
	/*int m_uniform;
	int vp_uniform;*/

	public int vao; // GL Vertex Array Object

	public boolean initialized;

	public Model(){
		vao = glGenVertexArrays();
		s0 = 1.f; s1 = 1.f; s2 = 1.f;
		a0 = 0.f; a1 = 0.f; a2 = 0.f;
		phi = 0.f; theta = 0.f;
		initialized = false;
	}

	public Model(String name){
		this();
		this.name = name;
	}

	public void prepare(){
		assert (initialized);

		v.rewind();
		t.rewind();
		n.rewind();
		i.rewind();

		glBindVertexArray(vao);

		v_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, v, GL_STATIC_DRAW);

		t_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
		glBufferData(GL_ARRAY_BUFFER, t, GL_STATIC_DRAW);

		n_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
		glBufferData(GL_ARRAY_BUFFER, n, GL_STATIC_DRAW);

		i_b = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, i, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
}

	public void draw(float x, float y, float z, float a, GraphicalSurface gs) {
		glBindVertexArray(vao);

		int prog = glGetInteger(GL_CURRENT_PROGRAM);
	//	GL20.glUseProgram(prog);
		if (prog == Renderer.getInstance().shaders[Renderer.SHADER_FADE]){
			int curr_z_attr = glGetUniformLocation(prog, "curr_z");
			int max_z_attr = glGetUniformLocation(prog, "max_z");
			glUniform1f(max_z_attr, (float)World.getInstance().zsize);
			glUniform1f(curr_z_attr, (float)Interface.getInstance().current_layer);
		}
		int v_attr = glGetAttribLocation(prog, "position");
		int t_attr = glGetAttribLocation(prog, "texture");
		int n_attr = glGetAttribLocation(prog, "normal");
		int t_uniform = glGetUniformLocation(prog, "tex");
		int m_uniform = glGetUniformLocation(prog, "M");
		int vp_uniform = glGetUniformLocation(prog, "VP");

		Matrix4 m = Matrix4.identity();
		m = m.multR(Matrix4.scale(new float[]{s0, s1, s2}));
		m = m.multR(Matrix4.rot(phi, 2));
		m = m.multR(Matrix4.rot(theta, 0));
		m = m.multR(Matrix4.rot(a, 2));
		m = m.multR(Matrix4.shift(new float[]{a0, a1, a2}));
		m = m.multR(Matrix4.shift(new float[]{x, y, z}));
//		m = m.multR(Matrix4.rot((float)(Math.PI/4.), 2));
//		m = m.multR(Matrix4.rot(a1, 1));
//		m = m.multR(Matrix4.rot((float)(Math.PI/4.), 0));
//		m = m.multR(Matrix4.scale(new float[]{scale, scale, 1.f}));
//		m = m.multR(Matrix4.lookAt(0.f, 0.f, -1.f, 0.5f, 0.5f, 0.f));
//		m = m.multR(Matrix4.scale(new float[]{3.f/4.f, 1.f, .1f}));
		glUniformMatrix4(m_uniform, false, m.fb());
		glUniformMatrix4(vp_uniform, false, Renderer.getInstance().VP.fb());

		glUniform1i(t_uniform, 0);
		glActiveTexture(GL_TEXTURE0+0);
		gs.bind();

		glEnableVertexAttribArray(v_attr);
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
//        glBufferSubData(GL_ARRAY_BUFFER, GL_STATIC_DRAW, v);
        glVertexAttribPointer(v_attr, 3, GL_FLOAT, false, 0, 0);

		glEnableVertexAttribArray(t_attr);
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
//        glBufferSubData(GL_ARRAY_BUFFER, GL_STATIC_DRAW, t);
        glVertexAttribPointer(t_attr, 2, GL_FLOAT, false, 0, 0);

		glEnableVertexAttribArray(n_attr);
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
//        glBufferSubData(GL_ARRAY_BUFFER, GL_STATIC_DRAW, n);
        glVertexAttribPointer(n_attr, 3, GL_FLOAT, true, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glDrawElements(GL_TRIANGLES, i.capacity(), GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(n_attr);
		glDisableVertexAttribArray(t_attr);
		glDisableVertexAttribArray(v_attr);

		glBindVertexArray(0);
	}
}
