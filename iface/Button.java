package iface;

import graphics.GSList;

import org.lwjgl.opengl.GL11;
import graphics.GraphicalSurface;

public class Button extends Element {
	private int x;
	private int y;
	private int dx;
	private int dy;

	private static GraphicalSurface activeIcon;
	private static GraphicalSurface inactiveIcon;
	ButtonFace face;

	private Runnable c;
	private int mbt = 0;// TODO: Change to lists button -> action

	private static boolean initialized = false;
	private void classInit() {
		activeIcon = GSList.getInstance().get("IconActive");
		inactiveIcon = GSList.getInstance().get("IconInactive");
	}

	public Button(int x, int y, int dx, int dy, String iconName){
		if (!initialized) classInit();
		this.active = false;
		this.visible = true;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.face = new ButtonFaceIcon(iconName);
		this.c = c;
	}
	
	public Button(int x, int y, int dx, int dy, SelectorMenu menu){
		if (!initialized) classInit();
		this.active = false;
		this.visible = true;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.face = new ButtonFaceMenuState(menu);
		this.c = c;
	}
	
	public void setFace(SelectorMenu menu){
		this.face = new ButtonFaceMenuState(menu);
	}

	public void setFace(String iconName){
		this.face = new ButtonFaceIcon(iconName);
	}

	public void onClick(int mButton){
		if(mButton == mbt){
			if(c!=null && active) c.run();
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
		if (!visible) return;
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
		face.draw(x, y, dx, dy);
	}
}
