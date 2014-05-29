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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import world.World;

import utils.Named;
import utils.Initializable;

public class Model implements Initializable, Named{

	public String name;
	public String fname;

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
		initialized = false;
	}

	@Override
	public void initialize()/* throws IOException*/ {
		if (initialized) return;
		ArrayList<Float> t_v = new ArrayList<>(0);
		ArrayList<Float> t_t = new ArrayList<>(0);
		ArrayList<Float> t_n = new ArrayList<>(0);
		ArrayList<Integer> i_v = new ArrayList<>(0);
		ArrayList<Integer> i_t = new ArrayList<>(0);
		ArrayList<Integer> i_n = new ArrayList<>(0);
		BufferedReader f;
		try {
			f = new BufferedReader(new FileReader("res/models/"+this.fname));
		} catch (java.io.FileNotFoundException e) {
			return;
		}
		String s=""; String[] w;
		while (s!=null){
            try {
                s = f.readLine();
            } catch (java.io.IOException e){
                return;
            }
			if ((s == null)||(s.isEmpty()))
				continue;
//			else
//				System.out.print(s+"\n");
			s = s.replace('/', ' ');
			s = s.replace("  ", " ");
			w = s.split(" ");
			switch (w[0]){
				case "v":
//					System.out.print(w[1]+w[2]+w[3]+"\n");
					t_v.add(Float.parseFloat(w[1]));
					t_v.add(Float.parseFloat(w[2]));
					t_v.add(Float.parseFloat(w[3]));
					break;
				case "vt":
					t_t.add(Float.parseFloat(w[1]));
					t_t.add(Float.parseFloat(w[2]));
					break;
				case "vn":
					t_n.add(Float.parseFloat(w[1]));
					t_n.add(Float.parseFloat(w[2]));
					t_n.add(Float.parseFloat(w[3]));
					break;
				case "f":
					i_v.add(Integer.parseInt(w[1])-1);
					i_t.add(Integer.parseInt(w[2])-1);
					i_n.add(Integer.parseInt(w[3])-1);
					i_v.add(Integer.parseInt(w[4])-1);
					i_t.add(Integer.parseInt(w[5])-1);
					i_n.add(Integer.parseInt(w[6])-1);
					i_v.add(Integer.parseInt(w[7])-1);
					i_t.add(Integer.parseInt(w[8])-1);
					i_n.add(Integer.parseInt(w[9])-1);
					break;
				default:
					break;
			}
		}
/*		System.out.print(t_v);
		System.out.print(t_t);
		System.out.print(t_n);
		System.out.print(i_v);
		System.out.print(i_t);
		System.out.print(i_n);*/
		ArrayList<Long> lm = new ArrayList<>();
	//	int v_l = t_v.size();
		int t_l = t_t.size()/2;
		int n_l = t_n.size()/3;
		int t = 0;
		Long key;
		int val;
		this.i = BufferUtils.createIntBuffer(i_v.size());
		for (int i=0; i<i_v.size(); ++i){
			key = ((long)i_v.get(i))*t_l*n_l +
				  i_t.get(i)*n_l +
				  i_n.get(i);
			val = lm.indexOf(key);
			if (val < 0){
				lm.add(key);
				val = t++;
			}
			this.i.put(val);
		}
//		System.out.print("\nt_l="+t_l+", n_l="+n_l+"\n"+lm);
		this.v = BufferUtils.createFloatBuffer(3*t);
		this.t = BufferUtils.createFloatBuffer(2*t);
		this.n = BufferUtils.createFloatBuffer(3*t);
		int ind_n, ind_t, ind_v;
		for (int i=0; i<t; i++){
			key = lm.get(i);
			ind_n = (int)((key % (t_l * n_l))%n_l);
			ind_t = (int)((key / n_l) % (t_l));
			ind_v = (int) (key / (t_l * n_l));
//			System.out.print("\n"+key+" = "+ind_v+" "+ind_t+" "+ind_n);
			this.v.put(t_v.get(3*ind_v+0));
			this.v.put(t_v.get(3*ind_v+1));
			this.v.put(t_v.get(3*ind_v+2));
			this.t.put(t_t.get(2*ind_t+0));
			this.t.put(t_t.get(2*ind_t+1));
			this.n.put(t_n.get(3*ind_n+0));
			this.n.put(t_n.get(3*ind_n+1));
			this.n.put(t_n.get(3*ind_n+2));
		}

		this.v.rewind();
		this.t.rewind();
		this.n.rewind();
		this.i.rewind();

		glBindVertexArray(vao);

		v_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, this.v, GL_STATIC_DRAW);

		t_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
		glBufferData(GL_ARRAY_BUFFER, this.t, GL_STATIC_DRAW);

		n_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
		glBufferData(GL_ARRAY_BUFFER, this.n, GL_STATIC_DRAW);

		i_b = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.i, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
		this.initialized = true;
	}

	public void draw(float x, float y, float z, float a, Texture gs) {
		if (z>(float)Interface.getInstance().current_layer+0.9f) return;
		if (z<Interface.getInstance().current_layer-Renderer.getInstance().fdepth) return;
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

		int tex_size = glGetUniformLocation(prog, "size");
		int tex_start = glGetUniformLocation(prog, "start");
		glUniform2f(tex_size, gs.u_size, gs.v_size);
		glUniform2f(tex_start, gs.tex_u, gs.tex_v);

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

	@Override
	public String getName(){
		return name;
	}
}
