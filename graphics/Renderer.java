package graphics;

import world.World;
import org.lwjgl.opengl.GL11;

public class Renderer {
	private World world;
	private GraphicalChunk[] gChunks;
	private GraphicalChunk[] gChunksFull;
	private Sun sun;

	public Renderer (World world) {
		this.world = world;
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
	}

	public void update	(int x, int y, int z) {
		gChunks[z].rebuild();
		gChunksFull[z].rebuild();
		if (z>0) {
			gChunks[z-1].rebuild();
			gChunksFull[z-1].rebuild();
		}
	}

	public void draw (int current_layer) {
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
	}
}
