package item;

import physics.material.Material;
import world.Block;
import creature.Creature;
import world.EventHandler;

public class ItemBoulder extends Item{

	double scale;

	public ItemBoulder(Block b, double w, Material m){
		super(b, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
		this.gsid = m.gsid;
		EventHandler.getInstance().addEntity(this);
	}

	public ItemBoulder(Creature c, double w, Material m){
		super(c, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
	}
}
