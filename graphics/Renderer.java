package graphics;

import world.World;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import iface.Interface;
import iface.Cursor;
import iface.Camera;

import graphics.shaders.ShaderLoader;
import graphics.shaders.Matrix4;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import world.Entity;

import physics.mana.ManaField;

public class Renderer {
	private World world;
	private Interface iface;
	public int gChunks_size;
	private GraphicalChunk[] gChunks;
	public int zdepth;
	public int fdepth;
	private int xChunkSize, yChunkSize;
	private Sun sun;
	private ArrayList<GraphicalEntity> gEntities;

	public final static int SHADER_NONE = 0;
	public final static int SHADER_BASIC = 1;
	public final static int SHADER_HIGHLIGHT = 2;
	public final static int SHADER_GHOST = 3;
	public final static int SHADER_FADE = 4;
	public final static int SHADER_MAX = 5;

	public int[] shaders;

	public int fade_attr;

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
		this.iface = Interface.getInstance();
		this.draw_mana = false; //cubes only
		this.zdepth = 1;
		this.fdepth = 12;

		xChunkSize = world.xsize/GraphicalChunk.CHUNK_SIZE;
		if (world.xsize%GraphicalChunk.CHUNK_SIZE!=0) xChunkSize++;
		yChunkSize = world.ysize/GraphicalChunk.CHUNK_SIZE;
		if (world.ysize%GraphicalChunk.CHUNK_SIZE!=0) yChunkSize++;

		gChunks_size = (zdepth+fdepth)*50; //FIXME
		gChunks = new GraphicalChunk[gChunks_size];
		for (int i=0; i<gChunks_size; i++)
			gChunks[i] = new GraphicalChunk(world, 0, 0, 0);

		this.view = Matrix4.lookAt(-.25f, -.25f, -2.f, .5f, .5f, 0.f);
		this.proj = Matrix4.scale(new float[]{.3f/4.f, -.1f, .001f});
		this.VP = view.multR(proj);

		shaders = new int[SHADER_MAX];
		shaders[SHADER_NONE] = 0;
		shaders[SHADER_BASIC] =
			ShaderLoader.createShader("basic.vsh", "basic.fsh");
//		System.out.println("[SHADER_BASIC]="+shaders[SHADER_BASIC]);
		shaders[SHADER_HIGHLIGHT] =
			ShaderLoader.createShader("highlight.vsh", "basic.fsh");
//		System.out.println("[SHADER_HIGHLIGHT]="+shaders[SHADER_HIGHLIGHT]);
		shaders[SHADER_GHOST] =
			ShaderLoader.createShader("basic.vsh", "ghost.fsh");
		shaders[SHADER_FADE] =
			ShaderLoader.createShader("basic.vsh", "fadeout.fsh");

		fade_attr = glGetUniformLocation(shaders[SHADER_FADE], "fade");

		gEntities = new ArrayList<GraphicalEntity>();
	}

	public void updateBlock (int x, int y, int z) {
		int i=0;
		while ((gChunks[i]==null) || (!gChunks[i].contains(x, y, z)))
			++i;
		if (i < gChunks_size)
			gChunks[i].needs_update = true;
/*		int chunkX = x/GraphicalChunk.CHUNK_SIZE;
		int chunkY = y/GraphicalChunk.CHUNK_SIZE;
		gChunks[chunkX][chunkY][z].rebuild();
		if (z>0)
			gChunks[chunkX][chunkY][z-1].rebuild(); */
	}

	public void addEntity (Entity e) {
		gEntities.add(new GraphicalEntity(e, world));
	}

	public void removeEntity (Entity e) {
		for (int i=0; i<gEntities.size(); i++) {
			if (gEntities.get(i).e == e) {
				gEntities.remove(i);
				return;
			}
		}
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

	public void recalc_chunks() {
		GraphicalChunk gc;
		Camera c = Interface.getInstance().camera;
		int top = Interface.getInstance().current_layer;
		int bot = Math.max(0, top - zdepth - fdepth);
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
//		if(zdepth*(startX-endX)*(startY-endY) > gChunks_size)
//			System.out.println(zdepth*(startX-endX)*(startY-endY)+">"+gChunks_size);
		for (int k=0; k<gChunks_size; ++k)
			gChunks[k].used = false;
		for (int k = top; k > bot; --k)
			for (int i = startX; i < endX; ++i)
				for (int j = startY; j < endY; ++j){
					gc = get_chunk(i, j, k);
					if (gc != null)
						gc.used = true;
				}
		for (int k = top; k > bot; --k)
			for (int i = startX; i < endX; ++i)
				for (int j = startY; j < endY; ++j){
					gc = get_chunk(i, j, k);
					if (gc == null){
						gc = get_unused_chunk();
						gc.used = true;
						//FIXME not enough chunks reserved?
						gc.rebuild(i, j, k);
					} else if (gc.needs_update)
						gc.rebuild();
				}

/*		for (int k=0; k<gChunks_size; ++k)
			if (gChunks[k].used)
				System.out.println(gChunks[k].x+" "+
						gChunks[k].y+" "+gChunks[k].z);
			else
				System.out.println("Not used");*/
	}


	public void draw () {
		int current_layer = Interface.getInstance().current_layer;
		float fade;
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		for (int i=0; i<gChunks_size; ++i)
			if ((gChunks[i] != null) && (gChunks[i].needs_update))
				gChunks[i].rebuild();
		VP = view.multR(proj);

		recalc_chunks();

		if (!draw_mana){
			glUseProgram(shaders[SHADER_HIGHLIGHT]);
			for (int i=0; i<gChunks_size; i++)
				if (gChunks[i].used && (gChunks[i].z==current_layer))
					gChunks[i].draw(true);
//			System.out.println("higlight layer printed");
			glUseProgram(shaders[SHADER_FADE]);
			for (int i=0; i<gChunks_size; i++)
				if (gChunks[i].used && (gChunks[i].z!=current_layer)){
					fade = (current_layer-gChunks[i].z) - zdepth;
					fade = (fade<0.f)?(1.f):(1.f-fade/fdepth);
					glUniform1f(fade_attr, fade);
					gChunks[i].draw(false);
				}
//			 System.out.println("regular layers printed");
		} else {
			glUseProgram(shaders[SHADER_NONE]);
			glBindTexture(GL_TEXTURE_2D, 0);
			glEnable(GL_COLOR_MATERIAL);
			glLineWidth(1);
			glColor3d(1., 1., .8);
			ManaField.getInstance().draw();
			glDisable(GL_COLOR_MATERIAL);
		}
		glUseProgram(shaders[SHADER_NONE]);
		resetMaterial();
		for (int i=0; i<gEntities.size(); i++)
			if (gEntities.get(i).e.p.z<=current_layer)gEntities.get(i).draw();
		iface.cursor.draw3d();
		iface.draw();
	}
}
