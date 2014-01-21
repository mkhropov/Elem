package graphics.models;

import graphics.*;
import org.lwjgl.opengl.GL11;


public class Cube extends Model {
	public Cube() {
		name = "cube";
	}

	public void draw(double x, double y, double z) {
//		System.out.println("Drawing cube @ "+x+" "+y+" "+z+" gs name "+gs.name);
//		GL11.glEnable(GL11.GL_BLEND);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x, y, z);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x+1, y, z);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x+1, y+1, z);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x, y+1, z);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x, y, z);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x, y+1, z);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x, y+1, z+1);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x, y, z+1);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x, y, z);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x+1, y, z);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x+1, y, z+1);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x, y, z+1);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x+1, y, z);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x+1, y+1, z);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x+1, y+1, z+1);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x+1, y, z+1);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x, y, z+1);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x+1, y, z+1);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x+1, y+1, z+1);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x, y+1, z+1);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(x, y+1, z);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(x+1, y+1, z);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(x+1, y+1, z+1);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(x, y+1, z+1);
		GL11.glEnd();

//		GL11.glDisable(GL11.GL_BLEND);
	}
}
