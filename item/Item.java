package item;

import world.*;
import creature.Creature;
import physics.material.Material;

public class Item extends Entity{
	Block b;
	Creature c;
	public Material m; //maybe a list in future?..
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

	public Item(Creature c, double w){
		super();
		this.b = null;
		this.c = c;
		this.w = w;
		this.type = TYPE_NONE;
	}

	public void update(World world){
		if (b!=null){
			int i = b.z;
			while (i > 0)
				if (world.blockArray[b.x][b.y][--i].m != null)
					break;
			if (i!=(b.z-1)){
				b.item.remove(this);
				b = world.blockArray[b.x][b.y][i+1];
				b.item.add(this);
			}
		}
	}
}
