package iface;

import iface.widget.*;
import item.Item;
import item.Inventory;

public class InventoryMenu extends Frame {

	static String emptyInvStr = "--- no items ---";

	private Inventory inv;
	private VBox vb;

	public InventoryMenu(Inventory I)//, String header)
	{
		super();

		vb = new VBox();
		this.add(vb);

//Frame f = new Frame();
//f.add(new Label(header));
//vb.add(f);

		mapInventory(I);

		update();
	}

	public void mapInventory(Inventory I)
	{
		this.inv = I;
	}

	public void update()
	{
		vb.child.clear();

		HBox hb;
		if (inv.amount() == 0) {
//			System.out.printf("Empty inventory, initiating with \"%s\"", emptyInvStr);
			hb = new HBox();
			hb.add(new Label(emptyInvStr));
			vb.add(hb);
			return;
		}
		int i = 0;
		while (inv.get(i) != null) {
			hb = new HBox();
			hb.add(new Label(String.format("%05d", inv.get(i).amount)));
			hb.add(new Label(inv.get(i).type.title));
			vb.add(hb);
			i++;
		}
	}

	public void draw()
	{
		update();
		recompile();
		super.draw();
	}
}
