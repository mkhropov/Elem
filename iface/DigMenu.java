package iface;

import iface.widget.*;
import world.World;

public class DigMenu extends HBox {

	public DigMenu(int X, int Y) {
		super();

		GroupButton b;
		Image i;
		Padding p;
/* dig form choice*/
		b = new GroupButton(World.FORM_FLOOR, new ValueSetter() {
			@Override public void set(int val) {
				Interface.setDigForm(val);
			}});
		i = new Image("IconDig", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(World.FORM_BLOCK, b);
		i = new Image("IconChannel", 30, 30);
		b.add(i);
		add(b);
/* end of build form choice */
		crop();
		compile(X, Y, 500, 40);
	}
}
