package item;

import core.Data;
import graphics.GraphicalEntity;
import graphics.Renderer;
import physics.Material;
import world.Block;
import world.Entity;
import world.World;

public class FallingBlock extends Entity implements GraphicalEntity{
	int m, f;
	int mid;
	int gsid;
	Block b;

	public FallingBlock(int[] coord){
	//	super(coord[0], coord[1], coord[2], null);
		b = new Block(coord[0], coord[1], coord[2]);
		World W = World.getInstance();
		this.m = W.getMaterialID(coord[0], coord[1], coord[2]);
		this.f = W.getForm(coord[0], coord[1], coord[2]);
		if (f == World.FORM_BLOCK)
			this.mid = core.Data.Models.getId("block");
		else
			this.mid = core.Data.Models.getId("floor");
		this.gsid = Data.Textures.getId(Data.Materials.get(m).texture);
		Renderer.getInstance().addEntity(this);
	}

	public void update(){
		World W = World.getInstance();
		int i = b.z;
		while (i>0 && !W.hasSolidFloor(b.x, b.y, i))
			--i;
		int mat = W.getMaterialID(b.x, b.y, i);
		int form = W.getForm(b.x, b.y, i);
		if (Data.Materials.get(mat).hardness < Data.Materials.get(m).hardness){
			W.setMaterialID(b.x, b.y, i, m);
			W.setForm(b.x, b.y, i, f);
			//spawn drop from mat, form
		} else if (mat == m) {
			W.setForm(b.x, b.y, i, Math.min(form, f));
			//spawn drop from m,max(f,form)
		} else {
			//spawn drop from m,f
		}
		Renderer.getInstance().removeEntity(this);
	}

}
