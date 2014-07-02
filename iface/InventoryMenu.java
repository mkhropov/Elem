package iface;

import iface.widget.*;
import item.Item;
import item.Inventory;

public class InventoryMenu extends Frame {

	public static InventoryMenu(Inventory I, String header)
	{
		super(header);

		VBox vb = new VBox();
		this.add(vb);

		HBox hb;
		int i = 0;
		while (I.get(i) != null) {
			hb = new HBox();
			hb.add(new Label(String.format("%05d", i.amount)));
			hb.add(new Label(i.type.title));
			vb.add(hb);
			i++;
		}
	}
}
