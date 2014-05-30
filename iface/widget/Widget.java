package iface.widget;

import java.util.ArrayList;

public class Widget {
	int X, Y;
	int dX, dY;

	public int minX, minY;

	ArrayList<Widget> child;
	int maxChild;

	public Widget parent;

	public Widget() {
		this.maxChild = 0;
		this.child = new ArrayList<>();
	}

	public void add(Widget w) {
		if (child.size() == maxChild) {
			/* System.exit(1); */
			return;
		}
		child.add(w);
		w.parent = this;
	}

	public void crop() {
		minX = 0;
		minY = 0;
	}

	public void compile(int X, int Y, int dX, int dY) {
	}

	public boolean hover(int x, int y) {
		return (X<=x && x<X+dX &&
				Y<=y && y<Y+dY);
	}

/*
 *	returns widget that was actually clicked at -
 *	one that can actually process the click
 */
	public Widget onPress(int x, int y) {
		Widget res;
		for (Widget w: child) {
			if (w.hover(x, y)) {
				res = w.onPress(x, y);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}

	public Widget onRelease(int x, int y) {
		return onPress(x, y);
	}

	public void draw() {
	}
}
