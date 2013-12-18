package graphics;

import world.World;
import org.lwjgl.opengl.GL11;
import iface.Interface;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import world.Entity;


public class Renderer {
	private World world;
	private Interface iface;
	private GraphicalChunk[] gChunks;
	private GraphicalChunk[] gChunksFull;
	private Sun sun;
	private ArrayList<GraphicalEntity> gEntities;

	public Renderer (World world, Interface iface) {
		this.world = world;
		this.iface = iface;
		gChunks = new GraphicalChunk[world.zsize];
		for (int i=0; i<world.zsize; i++) {
			gChunks[i] = new GraphicalChunk(world, i, GraphicalChunk.MODE_TOP_VIEW);
		}
		gChunksFull = new GraphicalChunk[world.zsize];
		for (int i=0; i<world.zsize; i++) {
			gChunksFull[i] = new GraphicalChunk(world, i, GraphicalChunk.MODE_SHOW_ALL);
		}
		this.sun = new Sun();
		sun.update();
		gEntities = new ArrayList<GraphicalEntity>();
	}

	public void updateBlock (int x, int y, int z) {
		gChunks[z].rebuild();
		gChunksFull[z].rebuild();
		if (z>0) {
			gChunks[z-1].rebuild();
			gChunksFull[z-1].rebuild();
		}
	}

	public void addEntity (Entity e) {
		gEntities.add(new GraphicalEntity(e, world));
	}

	public void removeEntity (Entity e) {
		System.out.println("Renderer: Entity removal NYI!");
	}

	void resetMaterial() {
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

	public void draw (int current_layer) {
		resetMaterial();
		sun.update();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		for (int k=current_layer; k>=0; k--) {
			if (k==current_layer) {
				GL11.glEnable(GL11.GL_LIGHT1);
				gChunksFull[k].draw();
				GL11.glDisable(GL11.GL_LIGHT1);
			} else {
				gChunks[k].draw();
			}
		}
		for (int i=0; i<gEntities.size(); i++) gEntities.get(i).draw();
		iface.cursor.draw();
	}
}
