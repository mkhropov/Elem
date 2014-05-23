package item;

import core.Data;
import creature.Creature;
import graphics.Renderer;
import physics.Material;
import world.Block;

public class ItemBoulder extends Item{
	double scale;

	public ItemBoulder(Block b, double w, int m){
		super(b, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = Data.Models.getId("boulder");
		this.gsid = Data.Textures.getId(Data.Materials.get(m).texture);
		Renderer.getInstance().addEntity(this);
	}

	public ItemBoulder(int x, int y, int z, double w, int m){
		super(x, y, z, w);
		this.m = m;
		this.type = Item.TYPE_BUILDABLE;
		this.scale = Math.cbrt(w);
		this.mid = Data.Models.getId("boulder");
		this.gsid = Data.Textures.getId(Data.Materials.get(m).texture);
		Renderer.getInstance().addEntity(this);
	}

	public ItemBoulder(Creature c, double w, int m){
		super(c, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = Data.Models.getId("boulder");
		this.gsid = Data.Textures.getId(Data.Materials.get(m).texture);
	//	Renderer.getInstance().addEntity(this);
	}
}
