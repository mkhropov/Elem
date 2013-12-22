package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Mramor extends Material {
	public Mramor() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
        gs = new GraphicalSurface("mramor", 0.3);
		hardness = 1.5*HARD_STONE;
	}
}
