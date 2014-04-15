package graphics;

import graphics.shaders.Matrix4;
import world.*;
import iface.Interface;
import player.Player;
import physics.Material;

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
	private Player player;
	public int x,y,z;
	int rx, ry;
	private int xsize, ysize;
	public boolean needs_update;
	public boolean used;

	public static final int CHUNK_SIZE = 16;

	public FloatBuffer vbuf;
	public FloatBuffer tbuf;
	public FloatBuffer nbuf;
	public IntBuffer ibuf;

	private int tailSize;

	public int vao;
	public int v_b;
	public int t_b;
	public int n_b;
	public int i_b;

	public int v_attr;
	public int t_attr;
	public int n_attr;
	public int m_uniform;
	public int vp_uniform;
	public int t_uniform;
	static float[][][] vert = new float[][][]{{
		{0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f, 0.f},//~ z-1
		{0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f, 1.f, 1.f},//~ z+1
		{0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f, 1.f, 0.f, 0.f, 1.f},//~ y-1
		{0.f, 1.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 0.f, 1.f, 1.f},//~ y+1
		{0.f, 0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f},//~ x-1
		{1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f, 1.f} //~ x+1
	},{
		{0.f, 0.f, -0.2f, 1.f, 0.f, -0.2f, 1.f, 1.f, -0.2f, 0.f, 1.f, -0.2f},//~ z-1
		{0.f, 0.f, 0.01f, 1.f, 0.f, 0.01f, 1.f, 1.f, 0.01f, 0.f, 1.f, 0.01f},//~ z+1
		{0.f, 0.f, -0.2f, 1.f, 0.f, -0.2f, 1.f, 0.f, 0.f, 0.f, 0.f, 0.f},//~ y-1
		{0.f, 1.f, -0.2f, 1.f, 1.f, -0.2f, 1.f, 1.f, 0.f, 0.f, 1.f, 0.f},//~ y+1
		{0.f, 0.f, -0.2f, 0.f, 1.f, -0.2f, 0.f, 1.f, 0.f, 0.f, 0.f, 0.f},//~ x-1
		{1.f, 0.f, -0.2f, 1.f, 1.f, -0.2f, 1.f, 1.f, 0.f, 1.f, 0.f, 0.f} //~ x+1
	}};
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
		this.rx = x*CHUNK_SIZE;
		this.y = y;
		this.ry = y*CHUNK_SIZE;
		this.z = z;
		this.tailSize = 0;
		this.used = false;
		this.needs_update = true;
		xsize = (w.xsize-rx > CHUNK_SIZE)?CHUNK_SIZE:(w.xsize-rx);
		ysize = (w.ysize-ry > CHUNK_SIZE)?CHUNK_SIZE:(w.ysize-ry);

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
	}

	void addFace(int X, int Y, int Z, int f, boolean fog){
		int m = World.getInstance().getMaterialID(X, Y, Z);
		int form = World.getInstance().getForm(X, Y, Z);
		int ind = (ibuf.position()>0)?(ibuf.get(ibuf.position()-1)+1):0;
		for (int i = 0; i<4; ++i){
			vbuf.put(vert[form>>10][f][3*i+0]+X);
			vbuf.put(vert[form>>10][f][3*i+1]+Y);
			vbuf.put(vert[form>>10][f][3*i+2]+Z);
			if (fog) {
				tbuf.put(0.75f+0.5f*text[2*i+0]/8.f+0.5f*((float)Math.abs(Math.sin(1.9*X+Y+Z)))/4.f);
				tbuf.put(0.75f+0.5f*text[2*i+1]/8.f+0.5f*((float)Math.abs(Math.sin(X-1.9*Y+Z)))/4.f);
			} else {
				tbuf.put(Material.tex_u[m]
					+(1-Material.rand[m])*text[2*i+0]/8.f
					+Material.rand[m]*((float)Math.abs(Math.sin(1.9*X+Y+Z)))/4.f);//FIX textures
				tbuf.put(Material.tex_v[m]
					+(1-Material.rand[m])*text[2*i+1]/8.f
					+Material.rand[m]*((float)Math.abs(Math.sin(X-1.9*Y+Z)))/4.f);// offsets
			}
			nbuf.put(norm[f][3*i+0]);
			nbuf.put(norm[f][3*i+1]);
			nbuf.put(norm[f][3*i+2]);
		}
		ibuf.put(ind++); ibuf.put(ind++); ibuf.put(ind);
		ibuf.put(ind-2); ibuf.put(ind++); ibuf.put(ind);
	}

	public void rebuild() {
		player = Interface.getInstance().player;
		/* fill vertex/texture/normal/indices buffers */
		vbuf.clear(); tbuf.clear(); nbuf.clear(); ibuf.clear();

		if ((Interface.getInstance().viewMode & Renderer.VIEW_MODE_MASK) == Renderer.VIEW_MODE_FOW){
			// First pass - fow only
			for (int i=rx; i<rx+xsize; i++)
				for (int j=ry; j<ry+ysize; j++){
					if (world.isAir(i,j,z)) continue;
					if (!player.blockKnown(i,j,z)) continue;
					if (!world.isFull(i-1,j,z))
						addFace(i, j, z, 4, false);
					if (!world.isFull(i+1,j,z))
						addFace(i, j, z, 5, false);
					if (!world.isFull(i,j-1,z))
						addFace(i, j, z, 2, false);
					if (!world.isFull(i,j+1,z))
						addFace(i, j, z, 3, false);
					if (!world.isFull(i,j,z+1))
						addFace(i, j, z, 1, false);
				}

			//Second pass - unknown stuff
			for (int i=rx; i<rx+xsize; i++)
				for (int j=ry; j<ry+ysize; j++){
					if (player.blockKnown(i,j,z)) continue;
					if (i==0)
						addFace(i, j, z, 4, true);
					if (i==world.xsize-1)
						addFace(i, j, z, 5, true);
					if (j==0)
						addFace(i, j, z, 2, true);
					if (j==world.ysize-1)
						addFace(i, j, z, 3, true);
				}

			//Third pass - top of unknown blocks
			tailSize = 0;
			for (int i=rx; i<rx+xsize; i++)
				for (int j=ry; j<ry+ysize; j++){
					if (player.blockKnown(i,j,z)){
						if (!world.isAir(i,j,z)){
							addFace(i, j, z, 1, false);
							tailSize += 6;
						}
					} else {
						addFace(i, j, z, 1, true);
						tailSize += 6;
					}
				}
		} else {
			for (int i=rx; i<rx+xsize; i++)
				for (int j=ry; j<ry+ysize; j++){
					if (world.isAir(i,j,z)) continue;
					if (!world.isFull(i-1,j,z))
						addFace(i, j, z, 4, false);
					if (!world.isFull(i+1,j,z))
						addFace(i, j, z, 5, false);
					if (!world.isFull(i,j-1,z))
						addFace(i, j, z, 2, false);
					if (!world.isFull(i,j+1,z))
						addFace(i, j, z, 3, false);
					if (!world.isFull(i,j,z+1))
						addFace(i, j, z, 1, false);
				}

			/* now add usually invisible top sides to the end of the buffer */
			tailSize = 0;
			for (int i=rx; i<rx+xsize; i++)
				for (int j=ry; j<ry+ysize; j++){
					if (!world.isFull(i,j,z)) continue;
					if (world.isFull(i,j,z+1)){
						addFace(i, j, z, 1, false);
						tailSize += 6;
					}
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
		this.rx = x*CHUNK_SIZE;
		this.y = y;
		this.ry = y*CHUNK_SIZE;
		this.z = z;
		rebuild();
	}

	public void draw(boolean showTop) {
		int p = glGetInteger(GL_CURRENT_PROGRAM);
		m_uniform = glGetUniformLocation(p, "M");
		vp_uniform = glGetUniformLocation(p, "VP");
		t_uniform = glGetUniformLocation(p, "tex");
		v_attr = glGetAttribLocation(p, "position");
		t_attr = glGetAttribLocation(p, "texture");
		n_attr = glGetAttribLocation(p, "normal");

		glBindVertexArray(vao);
		// model matrix is identity(), because cubes are static in world
		glUniformMatrix4(m_uniform, false, Matrix4.identity().fb());
		glUniformMatrix4(vp_uniform, false, Renderer.getInstance().VP.fb());

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

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_b);
		glDrawElements(GL_TRIANGLES, ibuf.limit()-(showTop?0:tailSize), GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(n_attr);
		glDisableVertexAttribArray(t_attr);
        glDisableVertexAttribArray(v_attr);
		glBindVertexArray(0);
	}

	public boolean contains(int nx, int ny, int nz){
		return ((nx >= rx) && (nx < rx+xsize) &&
				(ny >= ry) && (ny < ry+ysize) &&
				(nz == z));
	}
}
