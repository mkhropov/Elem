package world;
/**
 * Block is a class describing a minimal single 3D voxel
*/
import org.lwjgl.opengl.GL11;
import java.util.Random;
import java.util.ArrayList;

import physics.material.*;
import creature.*;
import item.*;
import graphics.Renderer;


public class Block {
	public int x, y, z; //in a chunk
	int T; //temperature
	public Substance m;
	public ArrayList<Creature> creature;
	public ArrayList<Item> item;
	public static final int[][] nearInd = new int[][]
		{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}
		,{0, 0, -1}, {-1, 0, 0}, {0, -1, 0}
		,{0, -1, -1}, {0, 1, -1}, {1, 0, -1}
		,{1, -1, 0}, {0, 1, 1}, {0, -1, 1}
		,{-1, -1, 0}, {-1, 1, 0}, {-1, 0, 1}
		,{-1, 0, -1}, {1, 1, 0}, {1, 0, 1}
		,{-1, -1, 1}, {-1, 1, -1}, {-1, 1, 1}, {1, -1, -1}
		,{1, -1, 1}, {1, 1, -1}, {1, 1, 1}, {-1, -1, -1}};

	public Block(int x, int y, int z) {
				this.x = x;
		this.y = y;
		this.z = z;
		this.m = null;
		this.creature = new ArrayList<>();
		this.item = new ArrayList<>();
	}

	public Block(Block b){
		this.x = b.x;
		this.y = b.y;
		this.z = b.z;
		this.m = b.m;
		this.creature = b.creature;
		this.item = b.item;
	}

	public void setMaterial(Material m) {
		this.m = new Substance(m, 1.d);
	}

	public void destroy(World w){
		item.add(new ItemBoulder(this, m.w, m.m));
		this.m = null;
		Renderer.getInstance().updateBlock(x, y, z);
		if ((z+1) < w.zsize)
	        w.blockArray[x][y][z+1].update(w);
	}

	public void update(World w){
		for (int i=0; i<creature.size(); ++i)
			creature.get(i).update();
		for (int i=0; i<item.size(); ++i)
			item.get(i).update(w);
	}

	public ArrayList<Block> nearest(){
		World w = World.getInstance();
		ArrayList<Block> l = new ArrayList<>(nearInd.length);
		for (int i=0; i<nearInd.length; ++i)
			if (w.isIn(this, nearInd[i]))
				l.add(w.blockArray[x+nearInd[i][0]][y+nearInd[i][1]][z+nearInd[i][2]]);
			else
				l.add(null);
		return l;
	}
}
