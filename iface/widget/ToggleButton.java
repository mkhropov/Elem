package iface.widget;

import graphics.Texture;
import core.Data;
import org.lwjgl.opengl.GL11;

public class ToggleButton extends TogglableWidget {

	private static Texture activeIcon;
	private static Texture inactiveIcon;

	private Runnable c;

	public ToggleButton(Runnable c) {
		super();
		this.maxChild = 1;

		this.active = false;
		this.c = c;
		activeIcon = Data.Textures.get("IconActive");
		inactiveIcon = Data.Textures.get("IconInactive");
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

        if (child.size() > 0) {
            Widget w = child.get(0);
            w.compile(X+5, Y+5, dX-10, dY-10);
        }
	}

/* Buttons are clickable, so we stop the chain of requests */
	@Override
	public Widget onPress(int x, int y) {
		setActive(active);
		return this;
	}

	@Override
	public Widget onRelease(int x, int y) {
		if (hover(x, y)) {
			c.run();
		} else {
			/* no actual click, return to last state */
			setActive(!active);
		}
		return this;
	}

	public void draw(){
        if (!visible)
			return;

		if (active) {
			activeIcon.bind();
		} else {
			inactiveIcon.bind();
		}

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0., 0.);
		GL11.glVertex2d(X, Y);
		GL11.glTexCoord2d(1., 0.);
		GL11.glVertex2d(X+dX, Y);
		GL11.glTexCoord2d(1., 1.);
		GL11.glVertex2d(X+dX, Y+dY);
		GL11.glTexCoord2d(0., 1.);
		GL11.glVertex2d(X, Y+dY);
		GL11.glEnd();

		if (child.size() > 0) {
			child.get(0).draw();
		}
	}
}
