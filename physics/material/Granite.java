package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Granite extends Material {
	public Granite() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
        gsid = 5;
		hardness = 2*HARD_STONE;
	}
}
