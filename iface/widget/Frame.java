package iface.widget;

//import graphics.Texture;
//import core.Data;
import org.lwjgl.opengl.GL11;

public class Frame extends Widget {

	public Frame() {
		super();
		this.maxChild = 1;

	}

	@Override
	public void crop() {
		/* approximate size of borders */
		minX = 10;
		minY = 10;
		if (child.size() > 0) {
			Widget w = child.get(0);
			w.crop();
			minX += w.minX;
			minY += w.minY;
		}
	}

	@Override
	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;
//		System.out.printf("Frame %d %d %d %d\n", X, Y, dX, dY);
		if (child.size() > 0) {
			Widget w = child.get(0);
			w.compile(X+5, Y+5, dX-10, dY-10);
		}

	}

	@Override
	public void draw(){
		if (!visible)
			return;

		Border.draw(X, Y, dX, dY);
		if (child.size() > 0) {
			child.get(0).draw();
		}
	}
}
