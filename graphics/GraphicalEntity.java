package graphics;

import world.Entity;
import physics.material.Material;

import org.lwjgl.opengl.GL11;

import world.World;

public class GraphicalEntity {
	Entity e;
	Model m;
	GraphicalSurface gs;

	public GraphicalEntity (Entity entity, World w) {
		e = entity;
		m = ModelList.getInstance().get(entity.mid);
		gs = GSList.getInstance().get(entity.gsid);
	}

	void draw() {
		gs.bind();
		m.draw(e.p.x, e.p.y, e.p.z);
	}
}
