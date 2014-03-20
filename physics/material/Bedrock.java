package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Bedrock extends Material {
	public Bedrock() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
		tex_u = 0.75f;
		tex_v = 0.25f;
		hardness = HARD_MAX;
	}
}
