package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Granite extends Material {
	public Granite() {
		tex_u = 0.5f;
		tex_v = 0.f;
		rand = 0.5f;
		hardness = 2*HARD_STONE;
	}
}
