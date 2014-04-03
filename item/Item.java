package item;

import world.*;
import creature.Creature;
import physics.material.Material;

public class Item extends Entity{
	Block b;
	Creature c;
	public char m; //maybe a list in future?..
	double w;
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
			while (i > 0)
				if (w.m[b.x][b.y][--i] != Material.MATERIAL_NONE)
					break;
			if (i!=(b.z-1)){
				b = w.getBlock(b.x, b.y, i+1);
				setP(b);
			}
		}
	}
}
