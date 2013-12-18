package iface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

public class Cursor {
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
		disabled = true;
	}

	public void reposition (int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		disabled = false;
	}

	public void disable() {
		disabled = true;
	}

	public void draw() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		if (!disabled) {
			GL11.glColor4d(0.0, 0.5, 0.8, 0.5);
		} else {
			GL11.glColor4d(0.8, 0.0, 0.0, 0.5);
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
