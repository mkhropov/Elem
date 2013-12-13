package graphics;

import java.util.Random;
import org.lwjgl.opengl.GL11;

import physics.material.*;
import world.Block;

class GraphicalCube {
	int x, y, z;
	Substance m;
	double texU, texV; //Maybe different for each quad?
	boolean[] visible = {false, false, false, false, false, false};

	static final int BOTTOM = 0;
	static final int WEST = 1;
	static final int NORTH = 2;
	static final int EAST = 3;
	static final int SOUTH = 4;
	static final int TOP = 5;

	GraphicalCube(Block b) {
		x = b.x;
		y = b.y;
		z = b.z;
		m = b.m;
		Random r = new Random();
		this.texU = r.nextDouble()/2;
		this.texV = r.nextDouble()/2;
	}

	void draw() {
		m.m.texture.bind();

		// draw quads
		if (visible[BOTTOM]){
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(0.0, 0.0, -1.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(0.0, 0.0, -1.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(0.0, 0.0, -1.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
			GL11.glNormal3d(0.0, 0.0, -1.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
			GL11.glEnd();
		}

		if (visible[WEST]) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(-1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(-1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
			GL11.glNormal3d(-1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
			GL11.glNormal3d(-1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
			GL11.glEnd();
		}

		if (visible[SOUTH]) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(0.0, -1.0, 0.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(0.0, -1.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(0.0, -1.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
			GL11.glNormal3d(0.0, -1.0, 0.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
			GL11.glEnd();
		}

		if (visible[EAST]) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
			GL11.glNormal3d(1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
			GL11.glNormal3d(1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
			GL11.glNormal3d(1.0, 0.0, 0.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
			GL11.glEnd();
		}

		if (visible[TOP]) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(0.0, 0.0, 1.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
			GL11.glNormal3d(0.0, 0.0, 1.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
			GL11.glNormal3d(0.0, 0.0, 1.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
			GL11.glNormal3d(0.0, 0.0, 1.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
			GL11.glEnd();
		}

		if (visible[NORTH]) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3d(0.0, 1.0, 0.0);
			GL11.glTexCoord2d(texU,texV);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
			GL11.glNormal3d(0.0, 1.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
			GL11.glNormal3d(0.0, 1.0, 0.0);
			GL11.glTexCoord2d(texU+0.5,texV+0.5);
			GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
			GL11.glNormal3d(0.0, 1.0, 0.0);
			GL11.glTexCoord2d(texU,texV+0.5);
			GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
			GL11.glEnd();
		}
	}
}
