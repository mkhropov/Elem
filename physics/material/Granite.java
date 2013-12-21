package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Granite extends Material {
	public Granite() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
        gs = new GraphicalSurface("Carmen_Red_Granite", 0.3);
		hardness = HARD_STONE;
	}
}
