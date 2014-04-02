package world;
/**
 * Block is a class describing utility for a minimal single 3D voxel
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
	public char m; //material code from Material

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
		this.m = World.getInstance().m[x][y][z];
	}

	public Block(Block b){
		this.x = b.x;
		this.y = b.y;
		this.z = b.z;
		this.m = b.m;
	}

	public ArrayList<Block> nearest(){
		World w = World.getInstance();
		ArrayList<Block> l = new ArrayList<>(nearInd.length);
		for (int i=0; i<nearInd.length; ++i)
			if (w.isIn(x, y, z, nearInd[i]))
				l.add(new Block(x+nearInd[i][0], y+nearInd[i][1], z+nearInd[i][2]));
			else
				l.add(null);
		return l;
	}
}
