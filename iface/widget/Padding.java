package iface.widget;

public class Padding extends Widget {

	private int w, h;

	public Padding(int width, int height) {
		super();

		w = width;
		h = height;
	}

	@Override
	public void crop() {
		minX = w;
		minY = h;
	}

	@Override
	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;
	}

	/* a padding does not display */
}
