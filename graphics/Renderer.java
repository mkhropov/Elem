package graphics;


import world.World;
import world.Block;
import world.VeinPatch;
import core.Data;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import iface.Interface;
import iface.Camera;
import iface.FloatingText;

import graphics.shaders.ShaderLoader;
import graphics.shaders.Matrix4;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import physics.mana.ManaField;

public class Renderer {
	private World world;
	public int gChunks_size;
	private GraphicalChunk[] gChunks;
	public int fdepth;
	private int xChunkSize, yChunkSize;
	private ArrayList<GraphicalEntity> gEntities;
	public ArrayList<FloatingText> ftArray; //FIXME public

	public static final int DISPLAY_WIDTH = 800;
	public static final int DISPLAY_HEIGHT = 600;
	public static final float ratio = (1.f*DISPLAY_HEIGHT)/DISPLAY_WIDTH;

	public final static int SHADER_NONE = 0;
	public final static int SHADER_BASIC = 1;
	public final static int SHADER_HIGHLIGHT = 2;
	public final static int SHADER_GHOST = 3;
	public final static int SHADER_FADE = 4;
	public final static int SHADER_HIGHLIGHT_FLAT = 5;
	public final static int SHADER_MAX = 6;

	public final static int VIEW_MODE_MASK=63;
	public final static int VIEW_MODE_FULL=1;      //Show all blocks, debug only
	public final static int VIEW_MODE_FOW=2;       //Show only known blocks
	public final static int VIEW_MODE_FOW_OCD=3;   //Show only known blocks open to air
	public final static int VIEW_MODE_NOBLOCK = 128;
	public final static int VIEW_MODE_FLAT=64; //Should top layer be flat

	public int[] shaders;

	public int curr_z_attr;
	public int max_z_attr;
	public int z_attr;
	public int fadeout_tex_start;
	public int fadeout_tex_size;
	public int bbasic_tex_start;
	public int bbasic_tex_size;
	public int hbasic_tex_start;
	public int hbasic_tex_size;
	public int hfbasic_tex_start;
	public int hfbasic_tex_size;

	public Matrix4 view;
	public Matrix4 proj;
	public Matrix4 VP;

	public boolean draw_mana;

	private static Renderer instance = null;

	public static Renderer getInstance() {
		if (instance == null) {
			instance = new Renderer();
		}
		return instance;
	}

	private Renderer () {
		this.world = World.getInstance();
		this.draw_mana = false; //cubes only
		this.fdepth = 13;  //FIXME this exact value should present in fog.vsh

		xChunkSize = world.xsize/GraphicalChunk.CHUNK_SIZE;
		if (world.xsize%GraphicalChunk.CHUNK_SIZE!=0) xChunkSize++;
		yChunkSize = world.ysize/GraphicalChunk.CHUNK_SIZE;
		if (world.ysize%GraphicalChunk.CHUNK_SIZE!=0) yChunkSize++;

		gChunks_size = fdepth*50; //FIXME
		gChunks = new GraphicalChunk[gChunks_size];
		for (int i=0; i<gChunks_size; i++)
			gChunks[i] = new GraphicalChunk(world, 0, 0, 0);

		this.view = Matrix4.lookAt(-.25f, -.25f, -2.f, .5f, .5f, 0.f);
		// FIXME depth-scale is MAGIC
		this.proj = Matrix4.scale(new float[]{.1f*ratio, -.1f, .001f});
		this.VP = view.multR(proj);

		shaders = new int[SHADER_MAX];
		shaders[SHADER_NONE] = 0;
		shaders[SHADER_BASIC] =
			ShaderLoader.createShader("basic.vsh", "basic.fsh");
		shaders[SHADER_HIGHLIGHT] =
			ShaderLoader.createShader("highlight.vsh", "basic.fsh");
		shaders[SHADER_HIGHLIGHT_FLAT] =
			ShaderLoader.createShader("highlight_flat.vsh", "basic.fsh");
		shaders[SHADER_GHOST] =
			ShaderLoader.createShader("basic.vsh", "ghost.fsh");
		shaders[SHADER_FADE] =
			ShaderLoader.createShader("fog.vsh", "fadeout.fsh");

		curr_z_attr = glGetUniformLocation(shaders[Renderer.SHADER_FADE], "curr_z");
		max_z_attr = glGetUniformLocation(shaders[Renderer.SHADER_FADE], "max_z");
		fadeout_tex_start = glGetUniformLocation(shaders[Renderer.SHADER_FADE], "start");
		fadeout_tex_size = glGetUniformLocation(shaders[Renderer.SHADER_FADE], "size");
		bbasic_tex_start = glGetUniformLocation(shaders[Renderer.SHADER_BASIC], "start");
		bbasic_tex_size = glGetUniformLocation(shaders[Renderer.SHADER_BASIC], "size");
		hbasic_tex_start = glGetUniformLocation(shaders[Renderer.SHADER_HIGHLIGHT], "start");
		hbasic_tex_size = glGetUniformLocation(shaders[Renderer.SHADER_HIGHLIGHT], "size");
		hfbasic_tex_start = glGetUniformLocation(shaders[Renderer.SHADER_HIGHLIGHT_FLAT], "start");
		hfbasic_tex_size = glGetUniformLocation(shaders[Renderer.SHADER_HIGHLIGHT_FLAT], "size");
		z_attr = glGetUniformLocation(shaders[SHADER_HIGHLIGHT_FLAT], "z");

		gEntities = new ArrayList<>();
		ftArray = new ArrayList<>();
	}

	public void updateBlock (int x, int y, int z) {
		int I;
		int i, j, k;
//		System.out.println("Updating block "+x+","+y+","+z);
		for (int t=-1; t<6; ++t){
			I = 0;
			if (t>=0){
				i = x+Block.nearInd[t][0];
				j = y+Block.nearInd[t][1];
				k = z+Block.nearInd[t][2];
			} else {
				i = x;
				j = y;
				k = z;
			}
			while ((I < gChunks_size) &&
				   ((gChunks[I]==null) || (!gChunks[I].contains(i, j, k))))
				++I;
			if (I < gChunks_size){
				gChunks[I].needs_update = true;
			}
		}
	}

	public void addEntity(GraphicalEntity e) {
		gEntities.add(e);
	}

	public void removeEntity(GraphicalEntity e) {
		gEntities.remove(e);
	}

	public void addFT(FloatingText ft) {
		ArrayList<FloatingText> toRemove = new ArrayList<>();
		for (FloatingText i: ftArray)
			if (i.owner != null && i.owner.equals(ft.owner))
				toRemove.add(i);
		ftArray.removeAll(toRemove);
		ftArray.add(ft);
	}

	public void removeFT(FloatingText ft) {
		ftArray.remove(ft);
	}

	public int[] get2DCoord(double x, double y, double z){
		float[] pos = VP.multR(x, y, z);
		return (new int[]{(int)(400*(pos[0]+1)), (int)(600-300*(pos[1]+1))});
	}


	public void resetMaterial() {
		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();

		float mat_ambient[] = { 0.5f, 0.5f, 0.5f, 0.0f };
		buffer.put(mat_ambient); buffer.flip();
		glMaterial(GL_FRONT, GL_AMBIENT, buffer);
		glMaterial(GL_FRONT, GL_SPECULAR, buffer);
		glMaterial(GL_FRONT, GL_DIFFUSE, buffer);
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
	}

	public void resetMaterial(float f) {
		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();

		float mat_ambient[] = { f, f, f, 0.0f };
		buffer.put(mat_ambient); buffer.flip();
		glMaterial(GL_FRONT, GL_AMBIENT, buffer);
		glMaterial(GL_FRONT, GL_SPECULAR, buffer);
		glMaterial(GL_FRONT, GL_DIFFUSE, buffer);
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
	}

	private final Comparator<int[]> COMPARE_0 = new Comparator<int[]>() {
		@Override
		public int compare(int[] o1, int[] o2) {
			return (o1[0]-o2[0]);
		}
	};

	private final Comparator<int[]> COMPARE_1 = new Comparator<int[]>() {
		@Override
		public int compare(int[] o1, int[] o2) {
			return (o1[1]-o2[1]);
		}
	};

	public GraphicalChunk get_chunk(int x, int y, int z){
		for (int t=0; t<gChunks_size; ++t){
			if ((gChunks[t].x == x) &&
				(gChunks[t].y == y) &&
				(gChunks[t].z == z))
				return gChunks[t];
		}
		return null;
	}

	public GraphicalChunk get_unused_chunk(){
		for (int t=0; t<gChunks_size; ++t){
			if (!gChunks[t].used)
				return gChunks[t];
		}
		return null; //FIXME should be damn sure enough chunks were allocated
	}

	public void reset() {
		for (int k=0; k<gChunks_size; ++k)
			gChunks[k].needs_update = true;
		recalc_chunks();
	}

	public void recalc_chunks() {
		GraphicalChunk gc;
		Camera c = Interface.getInstance().camera;
		int top = Interface.getInstance().current_layer;
		int bot = Math.max(0, top - fdepth);
		int startX, endX, startY, endY;
		int pos[][] = new int[8][2];
		pos[0] = c.resolvePixel(0, 0, bot);
		pos[1] = c.resolvePixel(800, 0, bot);
		pos[2] = c.resolvePixel(0, 600, bot);
		pos[3] = c.resolvePixel(800, 600, bot);
		pos[4] = c.resolvePixel(0, 0, top);
		pos[5] = c.resolvePixel(800, 0, top);
		pos[6] = c.resolvePixel(0, 600, top);
		pos[7] = c.resolvePixel(800, 600, top);
		Arrays.sort(pos, COMPARE_0);
		startX = Math.max(pos[0][0],0)/GraphicalChunk.CHUNK_SIZE;
		endX = Math.min(pos[7][0]/GraphicalChunk.CHUNK_SIZE+1,xChunkSize);
		Arrays.sort(pos, COMPARE_1);
		startY = Math.max(pos[0][1],0)/GraphicalChunk.CHUNK_SIZE;
		endY = Math.min(pos[7][1]/GraphicalChunk.CHUNK_SIZE+1,yChunkSize);

		for (int k=0; k<gChunks_size; ++k)
			gChunks[k].used = false;
		for (int k = top; k >= bot; --k)
			for (int i = startX; i < endX; ++i)
				for (int j = startY; j < endY; ++j){
					gc = get_chunk(i, j, k);
					if (gc != null)
						gc.used = true;
				}
		for (int k = top; k >= bot; --k)
			for (int i = startX; i < endX; ++i)
				for (int j = startY; j < endY; ++j){
					gc = get_chunk(i, j, k);
					if (gc == null){
						gc = get_unused_chunk();
						gc.used = true;
						//FIXME not enough chunks reserved?
						gc.rebuild(i, j, k);
					} else if (gc.needs_update) {
						gc.rebuild();
					}
				}
	}


	public void draw () {
		Texture.reset();
		int current_layer = Interface.getInstance().current_layer;
		float z;
		glClearColor(.9f*(current_layer-fdepth)/(world.zsize-fdepth),
					 .9f*(current_layer-fdepth)/(world.zsize-fdepth),
					 1.f*(current_layer-fdepth)/(world.zsize-fdepth),
					 1.f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		for (int i=0; i<gChunks_size; ++i)
			if ((gChunks[i] != null) && (gChunks[i].needs_update)) {
				gChunks[i].rebuild();
			}
		VP = view.multR(proj);

		recalc_chunks();

		if ((Interface.getInstance().viewMode & VIEW_MODE_NOBLOCK) == 0){
			if ((Interface.getInstance().viewMode & VIEW_MODE_FLAT)!=0) {
				z = current_layer + 0.5f;
				glUseProgram(shaders[SHADER_HIGHLIGHT_FLAT]);
				glUniform1f(z_attr, z);
				glUniform2f(hfbasic_tex_start, 0.f, 0.f);
				glUniform2f(hfbasic_tex_size, 1.f, 1.f);
			} else {
				glUseProgram(shaders[SHADER_HIGHLIGHT]);
				glUniform2f(hbasic_tex_start, 0.f, 0.f);
				glUniform2f(hbasic_tex_size, 1.f, 1.f);
			}
			for (int i=0; i<gChunks_size; i++)
				if (gChunks[i].used && (gChunks[i].z==current_layer)) {
					gChunks[i].draw(true);
				}
//			System.out.println("higlight layer printed");
			glUseProgram(shaders[SHADER_FADE]);
			glUniform2f(fadeout_tex_start, 0.f, 0.f);
			glUniform2f(fadeout_tex_size, 1.f, 1.f);
			glUniform1f(max_z_attr, (float)world.zsize);
			glUniform1f(curr_z_attr, (float)current_layer);
			for (int i=0; i<gChunks_size; i++)
				if (gChunks[i].used && (gChunks[i].z!=current_layer))
					gChunks[i].draw(false);
//			 System.out.println("regular layers printed");
		} else {/*
			glUseProgram(shaders[SHADER_NONE]);
			glBindTexture(GL_TEXTURE_2D, 0);
			glEnable(GL_COLOR_MATERIAL);
			glLineWidth(1);
			glColor3d(1., 1., .8);
			ManaField.getInstance().draw();
			glDisable(GL_COLOR_MATERIAL);*/
		}
		glUseProgram(shaders[Renderer.SHADER_FADE]);
		for (int i=0; i<gEntities.size(); i++){
			gEntities.get(i).draw();
		}
		for (VeinPatch vp: Data.Veins.asList()) {
			vp.draw(current_layer, 0);
		}
		world.items.draw();
		if (Interface.getCommandMode() != Interface.COMMAND_MODE_SELECT) {
			glUseProgram(shaders[SHADER_GHOST]);
			Interface.getInstance().cursor.draw3d();
		}
		glUseProgram(shaders[SHADER_NONE]);

		//ATTENTION
		//ftArray is drawn in Interface.draw()
		Interface.getInstance().draw();
	}
}
