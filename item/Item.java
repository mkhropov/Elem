package item;

import world.*;
import creature.Creature;

public class Item extends Entity{
	Block b;
	Creature c;
	double w;

	public Item(Block b, double w){
		this.b = b;
		this.c = null;
		this.w = w;
	}

	public Item(Creature c, double w){
		this.b = null;
		this.c = c;
		this.w = w;
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
