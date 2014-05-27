package world;
/**
 * Block is a class describing utility for a minimal single 3D voxel
*/
import java.util.ArrayList;
import item.Item;
import item.ItemTemplate;

public class Block {
	public int x, y, z; //in a chunk
	public int m; //material code from Material

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
		this.m = World.getInstance().getMaterialID(x, y, z);
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

	public ArrayList<Item> markItems(int N, ItemTemplate it) {
		ArrayList<Item> res = new ArrayList<>();
		int k = 0;
		for (Item i: World.getInstance().getItem(this)) {
			if (it.suits(i)) {
				res.add(i);
				i.mark();
				k++;
			}
			if (k >= N)
				break;
		}
		return res;
	}

	public int amount(ItemTemplate it) {
		int res = 0;
		for (Item i: World.getInstance().getItem(this)) {
			if (it.suits(i)) {
				res++;
			}
		}
		return res;
	}

	public boolean isSame(Block b){
		return (x==b.x && y==b.y && z==b.z);
	}
}
