package iface;

import graphics.Renderer;
import iface.widget.*;
import world.Block;
import world.World;

public class StatsBlock extends Frame {

	private final Block b;
	private final VBox vb;

	public StatsBlock(int X, int Y, Block b)
	{
		super();
		this.b = b;

		vb = new VBox();
		this.add(vb);

		update();
	}

	public final void update()
	{
		vb.child.clear();

		Frame f = new Frame();
		f.add(new Label("Block"));
		vb.add(f);

		vb.add(new Label(String.format("X: %d| Y: %d| Z: %d", b.x, b.y, b.z)));

		if (Interface.getInstance().player.blockKnown(b.x, b.y, b.z) ||
			((Interface.getInstance().viewMode & Renderer.VIEW_MODE_MASK) == Renderer.VIEW_MODE_FULL)) {
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
		} else {
			vb.add(new Label("???"));
		}
	}

	@Override
	public void draw()
	{
		update();
		recompile();
		super.draw();
	}
}
