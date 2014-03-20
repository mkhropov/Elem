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
	private World world;
	public int x,y,z;
	private int xsize, ysize;
	public boolean needs_update;
	public boolean used;

	public static final int CHUNK_SIZE = 16;

	public FloatBuffer vbuf;
	public FloatBuffer tbuf;
	public FloatBuffer nbuf;
	public IntBuffer ibuf;
	public int tail_size; //indices for invisible top sides

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

	public GraphicalChunk(World w, int x, int y, int z){
		this.world = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.tail_size = 0;
		this.used = false;
		this.needs_update = true;
		xsize = (w.xsize-x>CHUNK_SIZE)?CHUNK_SIZE:(w.xsize-x);
		ysize = (w.ysize-y>CHUNK_SIZE)?CHUNK_SIZE:(w.ysize-y);

		this.vbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*3);
		this.tbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*2);
		this.nbuf = BufferUtils.createFloatBuffer(xsize*ysize*6*4*3);
		this.ibuf = BufferUtils.createIntBuffer(xsize*ysize*6*6);

		this.vao = glGenVertexArrays();
		glBindVertexArray(vao);
		this.v_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
		this.t_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
		this.n_b = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
		this.i_b = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuf, GL_STATIC_DRAW);
		glBindVertexArray(0);

//		rebuild();
	}

	void addFace(Block b, int f){
		int ind = (ibuf.position()>0)?(ibuf.get(ibuf.position()-1)+1):0;
		for (int i = 0; i<4; ++i){
			vbuf.put(vert[f][3*i+0]+b.x);
			vbuf.put(vert[f][3*i+1]+b.y);
			vbuf.put(vert[f][3*i+2]+b.z);
			tbuf.put(b.m.m.tex_u+(text[2*i+0]+((float)Math.sin(b.x+b.y+b.z)+1.f)/2.f)/8.f);//FIX textures
			tbuf.put(b.m.m.tex_v+(text[2*i+1]+((float)Math.cos(b.x+b.y+b.z)+1.f)/2.f)/8.f);// offsets
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
		for (int i=x*CHUNK_SIZE; i<x*CHUNK_SIZE+xsize; i++)
			for (int j=y*CHUNK_SIZE; j<y*CHUNK_SIZE+ysize; j++){
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
				if (world.empty(i,j,z+1))
					addFace(b, 1);
			}

		/* now add usually invisible top sides to the end of the buffer */
		tail_size = 0;
		for (int i=x*CHUNK_SIZE; i<x*CHUNK_SIZE+xsize; i++)
			for (int j=y*CHUNK_SIZE; j<y*CHUNK_SIZE+ysize; j++){
				if (world.empty(i,j,z)) continue;
				b = world.blockArray[i][j][z];
				if (!world.empty(i,j,z+1)){
					addFace(b, 1);
					tail_size += 6;
				}
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
//		glBufferSubData(GL_ARRAY_BUFFER, 0, vbuf);
		glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
//		glBufferSubData(GL_ARRAY_BUFFER, 0, tbuf);
		glBufferData(GL_ARRAY_BUFFER, tbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
//		glBufferSubData(GL_ARRAY_BUFFER, 0, nbuf);
		glBufferData(GL_ARRAY_BUFFER, nbuf, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
//		glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, ibuf);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuf, GL_STATIC_DRAW);
		glBindVertexArray(0);
//		System.out.println(x+" "+y+" "+z);
//		System.out.println(ibuf+", tail size "+tail_size);
		needs_update = false;
	}

	public void rebuild(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		rebuild();
	}

	public void draw(boolean show_top) {
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
		GSList.getInstance().get("textures").bind();

		glEnableVertexAttribArray(v_attr);
		glBindBuffer(GL_ARRAY_BUFFER, v_b);
		glVertexAttribPointer(v_attr, 3, GL_FLOAT, false, 0, 0);
//		System.out.println("Vertices sent "+vbuf);

		glEnableVertexAttribArray(t_attr);
		glBindBuffer(GL_ARRAY_BUFFER, t_b);
		glVertexAttribPointer(t_attr, 2, GL_FLOAT, false, 0, 0);
//		System.out.println("Texture coord sent "+tbuf);

		glEnableVertexAttribArray(n_attr);
		glBindBuffer(GL_ARRAY_BUFFER, n_b);
		glVertexAttribPointer(n_attr, 3, GL_FLOAT, true, 0, 0);
//		System.out.println("Normals sent "+nbuf);

		/* if show_top is not set ->
		 do not draw underground top sides of cubes */
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glDrawElements(GL_TRIANGLES, ibuf.limit()-(show_top?0:tail_size),
			GL_UNSIGNED_INT, 0);
//		 System.out.println("Indices applied");

		glDisableVertexAttribArray(n_attr);
		glDisableVertexAttribArray(t_attr);
        glDisableVertexAttribArray(v_attr);
		glBindVertexArray(0);
	}

	public boolean contains(int nx, int ny, int nz){
		return ((nx >= x) && (nx < x+xsize) &&
				(ny >= y) && (ny < y+ysize) &&
				(nz == z));
	}
}
