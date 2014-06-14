package iface.widget;

public class HBox extends Widget {

	public HBox() {
		super();
		this.maxChild = -1;
	}

	@Override
	public void crop() {
		minX = 0;
		minY = 0;
		for (Widget w: child) {
			w.crop();
			minX += w.minX;
			minY = Math.max(w.minY, minY);
		}
	}

	@Override
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

	@Override
	public void draw() {
        if (!visible)
            return;

		for (Widget w: child) {
			w.draw();
		}
	}
}
