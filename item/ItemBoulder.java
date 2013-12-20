package item;

import physics.material.Material;
import world.Block;
import creature.Creature;

public class ItemBoulder extends Item{

	Material m;
	double scale;

	public ItemBoulder(Block b, double w, Material m){
		super(b, w);
		this.m = m;
		this.scale = Math.cbrt(w);
	}

	public ItemBoulder(Creature c, double w, Material m){
		super(c, w);
		this.m = m;
		this.scale = Math.cbrt(w);
	}
}
