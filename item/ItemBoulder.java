package item;

import physics.Material;
import world.Block;
import creature.Creature;
import graphics.Renderer;
import world.EventHandler;

public class ItemBoulder extends Item{
	double scale;
	static final String[] assoc;

	static {
		assoc = new String[Material.MATERIAL_MAX];
		assoc[Material.MATERIAL_STONE] = "stone";
		assoc[Material.MATERIAL_MARBLE] = "marble";
		assoc[Material.MATERIAL_GRANITE] = "granite";
		assoc[Material.MATERIAL_EARTH] = "earth";
		assoc[Material.MATERIAL_BEDROCK] = "bedrock";
	}

	public ItemBoulder(Block b, double w, int m){
		super(b, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
		this.gsid = graphics.GSList.getInstance().findId(assoc[m]);
		Renderer.getInstance().addEntity(this);
	}

	public ItemBoulder(int x, int y, int z, double w, int m){
		super(x, y, z, w);
		this.m = m;
		this.type = Item.TYPE_BUILDABLE;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
		this.gsid = graphics.GSList.getInstance().findId(assoc[m]);
		Renderer.getInstance().addEntity(this);
	}

	public ItemBoulder(Creature c, double w, int m){
		super(c, w);
		this.type = Item.TYPE_BUILDABLE;
		this.m = m;
		this.scale = Math.cbrt(w);
		this.mid = graphics.ModelList.getInstance().findId("boulder");
		this.gsid = graphics.GSList.getInstance().findId(assoc[m]);
	//	Renderer.getInstance().addEntity(this);
	}
}
