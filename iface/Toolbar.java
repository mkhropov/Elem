package iface;

import iface.widget.*;
import static iface.Interface.*;

public class Toolbar extends HBox {

	public Toolbar(int X, int Y) {
		super();

		GroupButton b;
		Image i;
		Padding p;

		b = new GroupButton(new Runnable() {
			@Override public void run() {
				Interface.getInstance().setCommandMode(COMMAND_MODE_SPAWN);
			}});
		i = new Image("IconSummon", 50, 50);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(new Runnable() {
			@Override public void run() {
				Interface.getInstance().setCommandMode(COMMAND_MODE_DIG);
			}}, b);
		i = new Image("IconDig", 50, 50);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(new Runnable() {
			@Override public void run() {
				Interface.getInstance().setCommandMode(COMMAND_MODE_BUILD);
			}}, b);
		i = new Image("IconBuild", 50, 50);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(new Runnable() {
			@Override public void run() {
				Interface.getInstance().setCommandMode(COMMAND_MODE_ZONE);
			}}, b);
		i = new Image("IconZone", 50, 50);
		b.add(i);
		add(b);
		p = new Padding(10, 0);
		add(p);
		b = new GroupButton(new Runnable() {
			@Override public void run() {
				Interface.getInstance().setCommandMode(COMMAND_MODE_CANCEL);
			}}, b);
		i = new Image("IconCancel", 50, 50);
		b.add(i);
		add(b);
		crop();
		compile(X, Y, 500, 60);
	}
}
