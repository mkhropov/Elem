package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Bedrock extends Material {
	public Bedrock() {
		tex_u = 0.75f;
		tex_v = 0.25f;
		rand = 0.5f;
		hardness = HARD_MAX;
	}
}
