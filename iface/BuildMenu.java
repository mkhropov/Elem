package iface;

import iface.widget.*;
import world.World;
import core.Data;

public class BuildMenu extends HBox {

	public BuildMenu(int X, int Y) {
		super();

		GroupButton b;
		Image i;
		Padding p;
/* build form choice*/
		b = new GroupButton(World.FORM_FLOOR, new ValueSetter() {
			@Override public void set(int val) {
				Interface.setBuildForm(val);
			}});
		i = new Image("IconFloor", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(World.FORM_BLOCK, b);
		i = new Image("IconBlock", 30, 30);
		b.add(i);
		add(b);
/* end of build form choice */
		p = new Padding(30, 0);
		add(p);
/* build material choice */
		b = new GroupButton(Data.Materials.getId("marble"), new ValueSetter() {
			@Override public void set(int val) {
				Interface.setBuildMaterial(val);
			}});
		i = new Image("marble", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Materials.getId("earth"), b);
		i = new Image("earth", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Materials.getId("gabbro"), b);
		i = new Image("gabbro", 30, 30);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(Data.Materials.getId("granite"), b);
		i = new Image("granite", 30, 30);
		b.add(i);
		add(b);
/* end of build material choice */
		crop();
		compile(X, Y, 500, 40);
	}
}
