package item;

import physics.material.Material;
import world.Block;
import creature.Creature;
import world.EventHandler;

public class ItemBoulder extends Item{

	double scale;

	public ItemBoulder(Block b, double w, char m){
		super(b, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
//		this.gsid = m.gsid;
		EventHandler.getInstance().addEntity(this);
	}

	public ItemBoulder(int x, int y, int z, double w, char m){
		super(x, y, z, w);
		this.m = m;
		this.type = Item.TYPE_BUILDABLE;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
		EventHandler.getInstance().addEntity(this);
	}

	public ItemBoulder(Creature c, double w, char m){
		super(c, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
	}
}
