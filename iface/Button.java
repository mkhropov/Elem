package iface;

import org.lwjgl.opengl.GL11;
import java.util.concurrent.Callable;
import graphics.GraphicalSurface;
import graphics.Renderer;

public class Button {
	public int x;
	public int y;
	public int dx;
	public int dy;
	public boolean active;

	public GraphicalSurface activeIcon;
	public GraphicalSurface inactiveIcon;

	public Command c;

	public Button(int x, int y, int dx, int dy, String activeIcon, String inactiveIcon, Command c){
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.activeIcon = new GraphicalSurface(activeIcon, 0);
		this.inactiveIcon = new GraphicalSurface(inactiveIcon, 0);
		this.c = c;
		this.active = false;
	}

	public void onPress(){
		c.execute();
	}

	public boolean isIn(int x, int y){
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
	}
}
