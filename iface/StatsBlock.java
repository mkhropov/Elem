package iface;

import iface.widget.*;
import world.Block;
import world.World;

public class StatsBlock extends Frame {

	private Block b;
	private VBox vb;

	public StatsBlock(int X, int Y, Block b)
	{
		super();
		this.b = b;

		vb = new VBox();
		this.add(vb);

		update();
	}

	public void update()
	{
		vb.child.clear();
		HBox hb;

		Frame f = new Frame();
		f.add(new Label("Block"));
		vb.add(f);

		vb.add(new Label(String.format("X: %d| Y: %d| Z: %d", b.x, b.y, b.z)));

		if (World.getInstance().isFull(b.x, b.y, b.z)) {
			vb.add(new Label(String.format("Solid block of %s",
				core.Data.Materials.get(b.m).getName())));
		} else if (!World.getInstance().isAir(b.x, b.y, b.z)) {
			vb.add(new Label(String.format("Floor made of %s",
				core.Data.Materials.get(b.m).getName())));
		} else {
			vb.add(new Label("Empty air"));
		}
		if (World.getInstance().isEmpty(b.x, b.y, b.z)) {
			InventoryMenu im = new InventoryMenu(b.getInventory());
			vb.add(im);
		}
	}

	public void draw()
	{
		update();
		recompile();
		super.draw();
	}
}
