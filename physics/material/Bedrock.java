package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Bedrock extends Material {
	public Bedrock() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
		hardness = HARD_MAX;
	}
}
