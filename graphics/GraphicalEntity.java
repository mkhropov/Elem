package graphics;

import world.Entity;
import physics.material.Material;

import org.lwjgl.opengl.GL11;

import world.World;

public class GraphicalEntity {
	private Entity e;
	Material m;

	public GraphicalEntity (Entity entity, World w) {
		e = entity;
		m = w.material[Material.MATERIAL_NONE];
	}

	void draw() { //Placeholder till we get models
		m.gs.bind();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.2);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.2);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.8);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.8);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.2);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.2);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.8);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.2, e.p.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.2, e.p.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.2);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.2);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(e.p.x+0.8, e.p.y+0.8, e.p.z+0.8);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(e.p.x+0.2, e.p.y+0.8, e.p.z+0.8);
		GL11.glEnd();
	}
}
