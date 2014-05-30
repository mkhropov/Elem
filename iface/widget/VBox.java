package iface.widget;

import java.util.ArrayList;

public class HBox extends Widget {

	public HBox() {
		super();
		this.maxChild = -1;
	}

	public void crop() {
		minX = 0;
		minY = 0;
		for (Widget w: child) {
			w.crop();
			minX = Math.max(w.minX, minX);
			minY += w.minY;
		}
	}

	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;
		/* only top align for now */
		int t = Y;
		for (Widget w: child) {
			w.compile(X, t, dX, w.minY);
			t += w.minY;
		}
	}

	public void draw() {
		for (Widget w: child) {
			w.draw();
		}
	}
}
