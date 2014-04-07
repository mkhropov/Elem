package graphics;

import java.io.IOException;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import graphics.shaders.Matrix4;

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
	public float d0, d1;
	public float scale;

	public int v_attr; // corresponding shader attributes
	public int t_attr;
	public int n_attr;

	public Texture texture;
	FloatBuffer MVP;
	int mvp_uniform;

	public int vao; // GL Vertex Array Object

	public boolean initialized;

	public Model(){
		vao = glGenVertexArrays();
		scale = 1.f;
		a0 = 0.f; a1 = 0.f; a2 = 0.f;
		initialized = false;
	}

	public Model(String name){
		this();
		this.name = name;
	}

	public void prepare(int prog){
		assert (initialized);

		glUseProgram(prog);

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

		v_attr = glGetAttribLocation(prog, "position");
		t_attr = glGetAttribLocation(prog, "texture");
		n_attr = glGetAttribLocation(prog, "normal");

		int t_uniform = glGetUniformLocation(prog, "tex");
//		System.out.print("Texture uniform found at "+t_uniform+"\n");
		glUniform1i(t_uniform, 0);
/*		try {
			texture = TextureLoader.getTexture("PNG",
				ResourceLoader.getResourceAsStream("res/IconSunstrike.png"));
		} catch (IOException e){
		}
		System.out.print("Texture loaded to "+texture+"\n");
		System.out.print("Width="+texture.getTextureWidth()+
						", height="+texture.getTextureHeight()+"\n");
*/
		mvp_uniform = glGetUniformLocation(prog, "MVP");
//		System.out.print("MVP uniform found at "+mvp_uniform+"\n");
		a0 = 0.f; a1 = 0.f; a2 = 0.f; scale = 1.f;

		glBindVertexArray(0);

//		glUseProgram(0);

	/*	System.out.print("\nIndex buffer\n");
		for (int k=0; k<i.capacity(); ++k)
			System.out.print(i.get(k)+" ");
		System.out.print("\n\nVertex buffer\n");
		for (int k=0; k<v.capacity()/3; ++k)
			System.out.print(v.get(3*k)+" "+v.get(3*k+1)+" "+v.get(3*k+2)+"   ");
		System.out.print("\n\nUV buffer\n");
		for (int k=0; k<t.capacity()/3; ++k)
			System.out.print(t.get(2*k)+" "+t.get(2*k+1)+"   ");
		System.out.print("\n\nNormal buffer\n");
		for (int k=0; k<n.capacity()/3; ++k)
			System.out.print(n.get(3*k)+" "+n.get(3*k+1)+" "+n.get(3*k+2)+"   ");
		System.out.print("\n");*/
}

	public void draw(float x, float y, float z, float a, GraphicalSurface gs) {
		Renderer r = Renderer.getInstance();
		glBindVertexArray(vao);
		Matrix4 m = Matrix4.identity();
		m = m.multR(Matrix4.scale(new float[]{scale, scale, scale}));
		m = m.multR(Matrix4.rot(a, 2));
		m = m.multR(Matrix4.shift(new float[]{a0, a1, a2}));
		m = m.multR(Matrix4.shift(new float[]{x, y, z}));
//		m = m.multR(Matrix4.rot((float)(Math.PI/4.), 2));
//		m = m.multR(Matrix4.rot(a1, 1));
//		m = m.multR(Matrix4.rot((float)(Math.PI/4.), 0));
//		m = m.multR(Matrix4.shift(new float[]{d0, d1, 0.f}));
//		m = m.multR(Matrix4.scale(new float[]{scale, scale, 1.f}));
//		m = m.multR(Matrix4.lookAt(0.f, 0.f, -1.f, 0.5f, 0.5f, 0.f));
//		m = m.multR(Matrix4.scale(new float[]{3.f/4.f, 1.f, .1f}));

		MVP = m.multR(r.view).multR(r.proj).fb();
//		System.out.println(MVP.toString());
		glUniformMatrix4(mvp_uniform, false, MVP);

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
