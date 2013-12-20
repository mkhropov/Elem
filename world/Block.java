package world;
/**
 * Block is a class describing a minimal single 3D voxel
*/
import org.lwjgl.opengl.GL11;
import java.util.Random;
import java.util.ArrayList;

import physics.material.*;
import creature.*;


public class Block {
	// this properties are given for example
	public int x, y, z; //in a chunk
	int T; //temperature
	public Substance m; //to be changed to a class
//	int liquidLevel;
//	int liquidId;
//	Gase gase;
//	void[] items;
	public ArrayList<Creature> creature;
	public static int[][] nearInd = new int[][]
		{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}
		,{0, 0, -1}, {-1, 0, 0}, {0, -1, 0}
		,{0, -1, -1}, {0, 1, -1}, {1, 0, -1}
		,{1, -1, 0}, {0, 1, 1}, {0, -1, 1}
		,{-1, -1, 0}, {-1, 1, 0}, {-1, 0, 1}
		,{-1, 0, -1}, {1, 1, 0}, {1, 0, 1}
		,{-1, -1, 1}, {-1, 1, -1}, {-1, 1, 1}, {1, -1, -1}
		,{1, -1, 1}, {1, 1, -1}, {1, 1, 1}, {-1, -1, -1}};
	 /* public static int[][] nearInd = new int[][]
		{{1, 1, 0}, {1, 0, 1}, {1, 0, 0}, {1, 0, -1}
		,{1, -1, 0}, {0, 1, 1}, {0, 1, 0}, {0, 1, -1}
		,{0, 0, 1}, {0, 0, -1}, {0, -1, 1}, {0, -1, 0}
		,{0, -1, -1}, {-1, 1, 0}, {-1, 0, 1}, {-1, 0, 0}
		,{-1, 0, -1}, {-1, -1, 0}};*/
	/*public static int[][] nearInd = new int[][]
		{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {0, 0, -1},
		 {0, -1, 0}, {-1, 0, 0}};*/

	public Block(int x, int y, int z) {
				this.x = x;
		this.y = y;
		this.z = z;
		this.m = null;
		this.creature = new ArrayList<>();
	}

	void setMaterial(Material m) {
		this.m = new Substance(m, 1.d);
	}

	boolean nearFits(int[] ind, World w){
		if ((x+ind[0]<0) || (x+ind[0]>=w.xsize)) return false;
		if ((y+ind[1]<0) || (y+ind[1]>=w.ysize)) return false;
		if ((z+ind[2]<0) || (z+ind[2]>=w.zsize)) return false;
		return true;
	}

	public void update(){
		if (creature != null)
			for (int i=0; i<creature.size(); ++i)
				creature.get(i).update();
	}

	public ArrayList<Block> nearest(World w){
		ArrayList<Block> l = new ArrayList<>(nearInd.length);
		for (int i=0; i<nearInd.length; ++i)
			if (nearFits(nearInd[i], w))
				l.add(w.blockArray[x+nearInd[i][0]][y+nearInd[i][1]][z+nearInd[i][2]]);
			else
				l.add(null);
		return l;
	}
}
