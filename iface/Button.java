package iface;

import graphics.GSList;

import org.lwjgl.opengl.GL11;
import java.util.concurrent.Callable;
import graphics.GraphicalSurface;
import graphics.Renderer;

public class Button {
	private int x;
	private int y;
	private int dx;
	private int dy;

	public boolean active = false;

	private static GraphicalSurface activeIcon;
	private static GraphicalSurface inactiveIcon;
	GraphicalSurface icon;

	private Runnable c;
	private int mbt = 0;// TODO: Change to lists button -> action

	private static boolean initialized = false;
	private void classInit() {
		activeIcon = GSList.getInstance().get("IconActive");
		inactiveIcon = GSList.getInstance().get("IconInactive");
	}

	public Button(int x, int y, int dx, int dy, String iconName){
		if (!initialized) classInit();
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.icon = GSList.getInstance().get(iconName);
		this.c = c;
	}

	public void onClick(int mButton){
		if(mButton == mbt){
			if(c!=null) c.run();
		}
	}

	public void bindAction(Runnable command, int mButton) {
		c = command;
		mbt = mButton;
	}

	public boolean hover(int x, int y){
//		System.out.printf("%d %d %d %d %d %d\n", x, y, this.x, this.y, dx, dy);
		return ((x>=this.x) && (x<this.x+dx) &&
				(y>=this.y) && (y<this.y+dy));
	}

	public void draw(){
		if (active) {
			activeIcon.bind();
		} else {
			inactiveIcon.bind();
		}
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0., 0.);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2d(1., 0.);
		GL11.glVertex2d(x+dx, y);
		GL11.glTexCoord2d(1., 1.);
		GL11.glVertex2d(x+dx, y+dy);
		GL11.glTexCoord2d(0., 1.);
		GL11.glVertex2d(x, y+dy);
		GL11.glEnd();
		icon.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0., 0.);
		GL11.glVertex2d(x+0.1*dx, y+0.1*dy);
		GL11.glTexCoord2d(1., 0.);
		GL11.glVertex2d(x+0.9*dx, y+0.1*dy);
		GL11.glTexCoord2d(1., 1.);
		GL11.glVertex2d(x+0.9*dx, y+0.9*dy);
		GL11.glTexCoord2d(0., 1.);
		GL11.glVertex2d(x+0.1*dx, y+0.9*dy);
		GL11.glEnd();
	}
}
