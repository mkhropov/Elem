package graphics;

import world.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

//import java.util.ArrayList;

public class GraphicalChunk {
//	private ArrayList<GraphicalCube> cubes;
	private World world;
	private int x,y,z;
	private int xsize, ysize;
	private int mode;
	public static final int MODE_TOP_VIEW = 0;
	public static final int MODE_SHOW_ALL = 1;
	public static final int MODE_FOG_OF_WAR = 2;

	public static final int CHUNK_SIZE = 16;

	public FloatBuffer vbuf;
	public FloatBuffer tbuf;
	public FloatBuffer nbuf;
	public IntBuffer ibuf;

	public int vao;
	public int v_b;
	public int t_b;
	public int n_b;
	public int i_b;

	public int v_attr;
	public int t_attr;
	public int n_attr;
	public int mvp_uniform;
	public int t_uniform;
	static float[][] vert = new float[][]{
		{0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f, 0.f},//~ z-1
		{0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f, 1.f, 1.f},//~ z+1
		{0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f, 1.f, 0.f, 0.f, 1.f},//~ y-1
		{0.f, 1.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 0.f, 1.f, 1.f},//~ y+1
		{0.f, 0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f},//~ x-1
		{1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f, 1.f} //~ x+1
	};
	static float[] text = new float[]{0.f, 0.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f};
	static float[][] norm = new float[][]{
		{ 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f},
		{ 0.f, 0.f, 1.f,  0.f, 0.f, 1.f,  0.f, 0.f, 1.f,  0.f, 0.f, 1.f },
		{ 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f},
		{ 0.f, 1.f, 0.f,  0.f, 1.f, 0.f,  0.f, 1.f, 0.f,  0.f, 1.f, 0.f },
		{-1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f, -1.f, 0.f, 0.f },
		{ 1.f, 0.f, 0.f,  1.f, 0.f, 0.f,  1.f, 0.f, 0.f,  1.f, 0.f, 0.f }
	};

	public GraphicalChunk(World w, int x, int y, int z, int mode) {
		this.world = w;
		this.x = x;
		this.y = y;
		this.z = z;
		xsize = (w.xsize-x>CHUNK_SIZE)?CHUNK_SIZE:(w.xsize-x);
		ysize = (w.ysize-y>CHUNK_SIZE)?CHUNK_SIZE:(w.ysize-y);
		this.mode = mode;
		this.vbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*3);
		this.tbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*2);
		this.nbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*3);
		this.ibuf = BufferUtils.createIntBuffer(xsize*ysize*6*6);
		this.vao = glGenVertexArrays();
		glBindVertexArray(vao);
		this.v_b = glGenBuffers();
		this.t_b = glGenBuffers();
		this.n_b = glGenBuffers();
		this.i_b = glGenBuffers();
		glBindVertexArray(0);
		rebuild();
	}

	void addFace(Block b, int f){
		int ind = (ibuf.position()>0)?(ibuf.get(ibuf.position()-1)+1):0;
		for (int i = 0; i<4; ++i){
			vbuf.put(vert[f][3*i+0]+b.x);
			vbuf.put(vert[f][3*i+1]+b.y);
			vbuf.put(vert[f][3*i+2]+b.z);
			tbuf.put(text[2*i+0]);//+(float)Math.sin(b.x+b.y+b.z));//FIX textures
			tbuf.put(text[2*i+1]);//+(float)Math.cos(b.x+b.y+b.z));// offsets
			nbuf.put(norm[f][3*i+0]);
			nbuf.put(norm[f][3*i+1]);
			nbuf.put(norm[f][3*i+2]);
		}
		ibuf.put(ind++); ibuf.put(ind++); ibuf.put(ind);
		ibuf.put(ind-2); ibuf.put(ind++); ibuf.put(ind);
	}

	public void rebuild() {
		Block b;
		/* fill vertex/texture/normal/indices buffers */
		vbuf.clear(); tbuf.clear(); nbuf.clear(); ibuf.clear();
		for (int i=x; i<x+xsize; i++)
			for (int j=y; j<y+ysize; j++){
				if (world.empty(i,j,z)) continue;
				b = world.blockArray[i][j][z];
				if (world.empty(i-1,j,z))
					addFace(b, 4);
				if (world.empty(i+1,j,z))
					addFace(b, 5);
				if (world.empty(i,j-1,z))
					addFace(b, 2);
				if (world.empty(i,j+1,z))
					addFace(b, 3);
				if (world.empty(i,j,z-1))
					addFace(b, 0);
				if (world.empty(i,j,z+1) || (mode == MODE_SHOW_ALL))
					addFace(b, 1);
			}
		vbuf.limit(vbuf.position());
		tbuf.limit(tbuf.position());
		nbuf.limit(nbuf.position());
		ibuf.limit(ibuf.position());
		vbuf.rewind(); tbuf.rewind(); nbuf.rewind(); ibuf.rewind();
/*		System.out.println("vbuf = ");
		for (int i=0; i<vbuf.limit()/3; ++i)
			System.out.print(i+" = "+vbuf.get(3*i)+" "+vbuf.get(3*i+1)+" "+vbuf.get(3*i+2)+"    \n");
		System.out.println("tbuf = ");
		for (int i=0; i<tbuf.limit()/2; ++i)
	        System.out.print(i+" = "+tbuf.get(2*i)+" "+tbuf.get(2*i+1)+"    \n");
		System.out.println("nbuf = ");
		for (int i=0; i<nbuf.limit()/3; ++i)
	        System.out.print(i+" = "+nbuf.get(3*i)+" "+nbuf.get(3*i+1)+" "+nbuf.get(3*i+2)+"    \n");
		System.out.println("ibuf = ");
		for (int i=0; i<ibuf.limit(); ++i)
			System.out.print(ibuf.get(i)+" ");
		System.out.println("\n");*/
		/* submit all generated buffers onto videocard */
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
		glBufferData(GL_ARRAY_BUFFER, tbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
		glBufferData(GL_ARRAY_BUFFER, nbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void draw() {
		int p = glGetInteger(GL_CURRENT_PROGRAM);
		mvp_uniform = glGetUniformLocation(p, "MVP");
		t_uniform = glGetUniformLocation(p, "tex");
		v_attr = glGetAttribLocation(p, "position");
		t_attr = glGetAttribLocation(p, "texture");
		n_attr = glGetAttribLocation(p, "normal");

		glBindVertexArray(vao);
		// model matrix is identity(), because cubes are static in world
		glUniformMatrix4(mvp_uniform, false, Renderer.getInstance().VP.fb());

		glUniform1i(t_uniform, 0);
		glActiveTexture(GL_TEXTURE0+0);
		GSList.getInstance().get("earth").bind();

		glEnableVertexAttribArray(v_attr);
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glVertexAttribPointer(v_attr, 3, GL_FLOAT, false, 0, 0);

		glEnableVertexAttribArray(t_attr);
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
		glVertexAttribPointer(t_attr, 2, GL_FLOAT, false, 0, 0);

		glEnableVertexAttribArray(n_attr);
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
		glVertexAttribPointer(n_attr, 3, GL_FLOAT, true, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glDrawElements(GL_TRIANGLES, ibuf.limit(), GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(n_attr);
		glDisableVertexAttribArray(t_attr);
        glDisableVertexAttribArray(v_attr);
		glBindVertexArray(0);
	}
}
