package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Marble extends Material {
	public Marble() {
		tex_u = 0.f;
		tex_v = 0.5f;
		rand = 0.3f;
		hardness = 1.5*HARD_STONE;
	}
}
