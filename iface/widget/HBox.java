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
			minX += w.minX;
			minY = Math.max(w.minY, minY);
		}
	}

	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;
		/* only left align for now */
		int t = X;
		for (Widget w: child) {
			w.compile(t, Y, w.minX, dY);
			t += w.minX;
		}
	}

	public void draw() {
        if (!visible)
            return;

		for (Widget w: child) {
			w.draw();
		}
	}
}
