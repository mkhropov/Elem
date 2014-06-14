package iface.widget;

public class VBox extends Widget {

	public VBox() {
		super();
		this.maxChild = -1;
	}

	@Override
	public void crop() {
		minX = 0;
		minY = 0;
		for (Widget w: child) {
			w.crop();
			minX = Math.max(w.minX, minX);
			minY += w.minY;
		}
	}

	@Override
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

	@Override
	public void draw() {
        if (!visible)
			return;

		for (Widget w: child) {
			w.draw();
		}
	}
}
