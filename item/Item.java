package item;

import creature.Creature;
import world.Block;
import world.Entity;
import world.World;

public class Item extends Entity{
	Block b;
	Creature c;
	double w;
	public int m; //maybe a list in future?..
	public int type;

	public static final int TYPE_NONE = 0;
	public static final int TYPE_BUILDABLE = 1;

	public Item(Block b, double w){
		super(b);
		this.b = b;
		this.c = null;
		this.w = w;
		this.type = TYPE_NONE;
	}

	public Item(int x, int y, int z, double w){
		this(World.getInstance().getBlock(x, y, z), w);
	}

	public Item(Creature c, double w){
		super();
		this.b = null;
		this.c = c;
		this.w = w;
		this.type = TYPE_NONE;
	}

	public boolean isIn(int x, int y, int z){
		return (b.x==x && b.y==y && b.z==z);
	}

	public boolean isIn(Block t){
		return (b.x==t.x && b.y==t.y && b.z==t.z);
	}

	public void update(){
		World w = World.getInstance();
		if (b!=null){
			int i = b.z;
			while (i > 0 && !w.hasSolidFloor(b.x, b.y, i)){
				--i;
			}
			if (i!=b.z){
				b = w.getBlock(b.x, b.y, i);
				setP(b);
			}
		}
	}
}
