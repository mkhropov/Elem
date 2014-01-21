package graphics;

import world.World;
import org.lwjgl.opengl.GL11;
import iface.Interface;
import iface.Cursor;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import world.Entity;


public class Renderer {
	private World world;
	private Interface iface;
	private GraphicalChunk[][][] gChunks;
	private GraphicalChunk[][][] gChunksFull;
	private int xChunkSize, yChunkSize;
	private Sun sun;
	private ArrayList<GraphicalEntity> gEntities;

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
		xChunkSize = world.xsize/GraphicalChunk.CHUNK_SIZE;
		if (world.xsize%GraphicalChunk.CHUNK_SIZE!=0) xChunkSize++;
		yChunkSize = world.ysize/GraphicalChunk.CHUNK_SIZE;
		if (world.ysize%GraphicalChunk.CHUNK_SIZE!=0) yChunkSize++;
		gChunks = new GraphicalChunk[xChunkSize][yChunkSize][world.zsize];
		for (int i=0; i<xChunkSize; i++)
		for (int j=0; j<yChunkSize; j++)
		for (int k=0; k<world.zsize; k++) {
			gChunks[i][j][k] = new GraphicalChunk(world, i*GraphicalChunk.CHUNK_SIZE,
					j*GraphicalChunk.CHUNK_SIZE, k, GraphicalChunk.MODE_TOP_VIEW);
		}
		gChunksFull = new GraphicalChunk[xChunkSize][yChunkSize][world.zsize];
		for (int i=0; i<xChunkSize; i++)
		for (int j=0; j<yChunkSize; j++)
		for (int k=0; k<world.zsize; k++) {
			gChunksFull[i][j][k] = new GraphicalChunk(world, i*GraphicalChunk.CHUNK_SIZE,
					j*GraphicalChunk.CHUNK_SIZE, k, GraphicalChunk.MODE_SHOW_ALL);
		}
		this.sun = new Sun();
		sun.update();
		gEntities = new ArrayList<GraphicalEntity>();
	}

	public void updateBlock (int x, int y, int z) {
		int chunkX = x/GraphicalChunk.CHUNK_SIZE;
		int chunkY = y/GraphicalChunk.CHUNK_SIZE;
		gChunks[chunkX][chunkY][z].rebuild();
		gChunksFull[chunkX][chunkY][z].rebuild();
		if (z>0) {
			gChunks[chunkX][chunkY][z-1].rebuild();
			gChunksFull[chunkX][chunkY][z-1].rebuild();
		}
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
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, buffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, buffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, buffer);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);
	}

	public void resetMaterial(float f) {
		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();

		float mat_ambient[] = { f, f, f, 0.0f };
		buffer.put(mat_ambient); buffer.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, buffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, buffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, buffer);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);
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

	public void draw (int current_layer) {
		resetMaterial();
		sun.update();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		int startX, endX, startY, endY;
		int pos[][] = new int[8][2];
		pos[0] = iface.camera.resolvePixel(0,0,0);
		pos[1] = iface.camera.resolvePixel(800,0,0);
		pos[2] = iface.camera.resolvePixel(0,600,0);
		pos[3] = iface.camera.resolvePixel(800,600,0);
		pos[4] = iface.camera.resolvePixel(0,0,current_layer);
		pos[5] = iface.camera.resolvePixel(800,0,current_layer);
		pos[6] = iface.camera.resolvePixel(0,600,current_layer);
		pos[7] = iface.camera.resolvePixel(800,600,current_layer);
		Arrays.sort(pos, COMPARE_0);
		startX = Math.max(pos[0][0],0)/GraphicalChunk.CHUNK_SIZE;
		endX = Math.min(pos[7][0]/GraphicalChunk.CHUNK_SIZE+1,xChunkSize);
		Arrays.sort(pos, COMPARE_1);
		startY = Math.max(pos[0][1],0)/GraphicalChunk.CHUNK_SIZE;
		endY = Math.min(pos[7][1]/GraphicalChunk.CHUNK_SIZE+1,yChunkSize);
		for (int k=current_layer; k>=0; k--) {
			if (k==current_layer) {
				GL11.glEnable(GL11.GL_LIGHT1);
				for (int i=startX; i<endX; i++)
				for (int j=startY; j<endY; j++)
					gChunksFull[i][j][k].draw();
				GL11.glDisable(GL11.GL_LIGHT1);
			} else {
				for (int i=startX; i<endX; i++)
				for (int j=startY; j<endY; j++)
					gChunks[i][j][k].draw();
			}
		}
		for (int i=0; i<gEntities.size(); i++)
			if (gEntities.get(i).e.p.z<=current_layer)gEntities.get(i).draw();
		iface.cursor.draw3d();
		iface.draw();
	}
}
