package item;

import graphics.Renderer;
import physics.Material;
import world.World;

public class FallingBlock extends Item{

	static final String[] assoc;

	int m, f;

	static {
		assoc = new String[Material.MATERIAL_MAX];
		assoc[Material.MATERIAL_STONE] = "stone";
		assoc[Material.MATERIAL_MARBLE] = "marble";
		assoc[Material.MATERIAL_GRANITE] = "granite";
		assoc[Material.MATERIAL_EARTH] = "earth";
		assoc[Material.MATERIAL_BEDROCK] = "bedrock";
	}

	public FallingBlock(int[] coord){
		super(coord[0], coord[1], coord[2], 0.);
		World W = World.getInstance();
		this.type = Item.TYPE_NONE;
		this.m = W.getMaterialID(coord[0], coord[1], coord[2]);
		this.f = W.getForm(coord[0], coord[1], coord[2]);
		if (f == World.FORM_BLOCK)
			this.mid = graphics.ModelList.getInstance().findId("block");
		else
			this.mid = graphics.ModelList.getInstance().findId("floor");
		this.gsid = graphics.GSList.getInstance().findId(assoc[m]);
		Renderer.getInstance().addEntity(this);
	}
}
