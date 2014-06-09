package iface;

import iface.widget.*;
import static iface.Interface.*;
import world.World;
import core.Data;

public class ZoneMenu extends HBox {

	public ZoneMenu(int X, int Y) {
		super();

		GroupButton b;
		Image i;
		Padding p;
/* zone type choice*/
		b = new GroupButton(Data.Zones.getId("stockpile"),
			new ValueSetter() {
				public void set(int val) {
					Interface.setZoneType(val);
				}
			});
		i = new Image("", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Zones.getId("lumber"), b);
		i = new Image("", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Zones.getId("mason"), b);
		i = new Image("", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Zones.getId("jewel"), b);
		i = new Image("", 30, 30);
		b.add(i);
		add(b);
/* end of build form choice */
		crop();
		compile(X, Y, 500, 40);
	}
}
