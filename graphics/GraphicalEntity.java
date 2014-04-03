package graphics;

import world.Entity;
import physics.material.Material;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import world.World;

public class GraphicalEntity {
	Entity e;
//	Model m;
//	GraphicalSurface gs;

	public GraphicalEntity (Entity entity, World w) {
		e = entity;
//		m = ModelList.getInstance().get(entity.mid);
	//	gs = GSList.getInstance().get(entity.gsid);
	}

	void draw() {
		ModelList.getInstance().get(e.mid).draw(
				(float)e.p.x, (float)e.p.y, (float)e.p.z, e.a, 
				GSList.getInstance().get(e.gsid));
	}
}
