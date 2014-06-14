package iface.widget;

import java.util.ArrayList;

public class Widget {
	int X, Y;
	int dX, dY;

	public boolean visible;

	public int minX, minY;

	public ArrayList<Widget> child;
	int maxChild;

	public Widget parent;

	public Widget() {
		this.visible = true;
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
		return (getVisible() &&
				X<=x && x<X+dX &&
				Y<=y && y<Y+dY);
	}

/*
 *	returns widget that was actually clicked at -
 *	one that can actually process the click
 */
	public Widget onPress(int x, int y) {
//		System.out.println("Generic widget pressed at "+x+", "+y);
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
//		System.out.println("Generic widget released at "+x+", "+y);
		Widget res;
		for (Widget w: child) {
			if (w.hover(x, y)) {
				res = w.onRelease(x, y);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

/* going up on hierarchy */
	public boolean getVisible() {
		Widget w = this;
		while (w.visible && w.parent != null) {
			w = w.parent;
		}

		return w.visible;
	}

	public void draw() {
	}
}
