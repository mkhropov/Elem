package graphics;

import world.Entity;
import stereometry.Point;
import physics.material.Material;

import org.lwjgl.opengl.GL11;

import world.World;

public class GraphicalEntity {
	private Entity e;
	Model m;

	public GraphicalEntity (Entity entity, World w) {
		e = entity;
		m = new graphics.models.Elemental();
	}

	void draw() { //Placeholder till we get models
		m.draw(e.p.x, e.p.y, e.p.z);
	}
}
