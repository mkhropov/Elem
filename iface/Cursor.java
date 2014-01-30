package iface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import graphics.Renderer;

public class Cursor {

	public static final int STATE_ENABLED  = 0;
	public static final int STATE_DISABLED = 1;
	public static final int STATE_IFACE    = 2;
	public static final int STATE_MAX      = 3;
	public int state;

	public int x, y, z;
	public boolean disabled;

	public Cursor(){
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		try {
			org.lwjgl.input.Cursor emptyCursor = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			 e.printStackTrace();
			 System.exit(0);
		}
	//	icon = 
		state = STATE_IFACE;
	}

	public void reposition (int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		disabled = false;
	}

	public void draw2d(){
		if (state != STATE_IFACE)
			return; //or exception
//		GL11.glDisable(GL11.GL_LIGHT0);
//		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//		GL11.Color.white.bind();
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3d(1., 1., 1.);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x, 600-y);
		GL11.glVertex2d(x+20, 600-y+14);
		GL11.glVertex2d(x+7, 600-y+12);
		GL11.glVertex2d(x, 600-y+23);
		GL11.glEnd();
		GL11.glColor3d(0., 0., 0.);
		GL11.glLineWidth(2);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(x, 600-y);
		GL11.glVertex2d(x+20, 600-y+14);
		GL11.glVertex2d(x+7, 600-y+12);
		GL11.glVertex2d(x, 600-y+23);
		GL11.glVertex2d(x, 600-y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
//		GL11.glEnable(GL11.GL_LIGHT0);
	}

	public void draw3d(){
		if (state == STATE_IFACE)
			return; //or exception

		Renderer.getInstance().resetMaterial();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		if (state == STATE_ENABLED){
			GL11.glColor4d(0.0, 0.5, 0.8, 0.5);
		} else if (state == STATE_DISABLED){
			GL11.glColor4d(0.8, 0.0, 0.0, 0.5);
		} else {
			return; //or exception
		}

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glVertex3d(x-0.01, y-0.01, z-0.01);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glVertex3d(x+1.01, y-0.01, z-0.01);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glVertex3d(x+1.01, y+1.01, z-0.01);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glVertex3d(x-0.01, y+1.01, z-0.01);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glVertex3d(x-0.01, y-0.01, z-0.01);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glVertex3d(x-0.01, y+1.01, z-0.01);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glVertex3d(x-0.01, y+1.01, z+1.01);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glVertex3d(x-0.01, y-0.01, z+1.01);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glVertex3d(x-0.01, y-0.01, z-0.01);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glVertex3d(x+1.01, y-0.01, z-0.01);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glVertex3d(x+1.01, y-0.01, z+1.01);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glVertex3d(x-0.01, y-0.01, z+1.01);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glVertex3d(x+1.01, y-0.01, z-0.01);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glVertex3d(x+1.01, y+1.01, z-0.01);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glVertex3d(x+1.01, y+1.01, z+1.01);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glVertex3d(x+1.01, y-0.01, z+1.01);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glVertex3d(x-0.01, y-0.01, z+1.01);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glVertex3d(x+1.01, y-0.01, z+1.01);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glVertex3d(x+1.01, y+1.01, z+1.01);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glVertex3d(x-0.01, y+1.01, z+1.01);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glVertex3d(x-0.01, y+1.01, z-0.01);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glVertex3d(x+1.01, y+1.01, z-0.01);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glVertex3d(x+1.01, y+1.01, z+1.01);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glVertex3d(x-0.01, y+1.01, z+1.01);
		GL11.glEnd();

		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
