package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Stone extends Material {
	public Stone() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);
        gs = new GraphicalSurface("stone", 0.5);
	}
}
