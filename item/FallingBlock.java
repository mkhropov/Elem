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

	@Override
	public void update(){
		World W = World.getInstance();
		int i = b.z;
		while (i>0 && !W.hasSolidFloor(b.x, b.y, i))
			--i;
		int mat = W.getMaterialID(b.x, b.y, i);
		int form = W.getForm(b.x, b.y, i);
		if (Material.hardness[mat] < Material.hardness[m]){
			W.setMaterialID(b.x, b.y, i, m);
			W.setForm(b.x, b.y, i, f);
			//spawn drop from mat, form
		} else if (mat == m) {
			W.setForm(b.x, b.y, i, Math.min(form, f));
			//spawn drop from m,max(f,form)
		} else {
			//spawn drop from m,f
		}
		W.item.remove(this);
		Renderer.getInstance().removeEntity(this);
	}

}
