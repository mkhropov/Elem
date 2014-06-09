package iface;

import iface.widget.*;
import static iface.Interface.*;

public class Toolbar extends HBox {

	private BuildMenu buildMenu;
	private DigMenu digMenu;
	private ZoneMenu zoneMenu;

	public Toolbar(int X, int Y) {
		super();

		buildMenu = new BuildMenu(X+65, Y-50);
//		buildMenu.visible =
//			(Interface.getCommandMode() == COMMAND_MODE_BUILD);
		Interface.menu.add(buildMenu);
		digMenu = new DigMenu(X+120, Y-50);
//		digMenu.visible =
//			(Interface.getCommandMode() == COMMAND_MODE_DIG);
		Interface.menu.add(digMenu);
		zoneMenu = new ZoneMenu(X+150, Y-50);
		Interface.menu.add(zoneMenu);

		GroupButton b;
		Image i;
		Padding p;
		GroupReflection gr;

		b = new GroupButton(COMMAND_MODE_SPAWN, new ValueSetter() {
			@Override public void set(int val) {
				Interface.setCommandMode(val);
			}});
		i = new Image("IconSummon", 50, 50);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(COMMAND_MODE_DIG, b);
		b.addSlave(digMenu);
		b.setActive(Interface.getCommandMode() == COMMAND_MODE_DIG);
		/* CARE digMenu must consist of GroupButtons! */
		gr = new GroupReflection((GroupButton)digMenu.child.get(0), 50, 50);
		b.add(gr);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(COMMAND_MODE_BUILD, b);
		b.addSlave(buildMenu);
		b.setActive(Interface.getCommandMode() == COMMAND_MODE_BUILD);
		gr = new GroupReflection((GroupButton)buildMenu.child.get(0), 50, 50);
		b.add(gr);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(COMMAND_MODE_ZONE, b);
		b.addSlave(zoneMenu);
		b.setActive(Interface.getCommandMode() == COMMAND_MODE_ZONE);
		gr = new GroupReflection((GroupButton)zoneMenu.child.get(0), 50, 50);
		b.add(gr);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(COMMAND_MODE_CANCEL, b);
		b.setActive(Interface.getCommandMode() == COMMAND_MODE_CANCEL);
		i = new Image("IconCancel", 50, 50);
		b.add(i);
		add(b);

		crop();
		compile(X, Y, 500, 60);

		visible = true;
	}
}
