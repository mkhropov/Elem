package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Granite extends Material {
	public Granite() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
		tex_u = 0.5f;
		tex_v = 0.f;
		rand = 0.5f;
		hardness = 2*HARD_STONE;
	}
}
