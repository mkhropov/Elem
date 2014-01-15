package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Marble extends Material {
	public Marble() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
        gsid = 3;
		hardness = 1.5*HARD_STONE;
	}
}
