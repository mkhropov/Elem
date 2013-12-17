package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Earth extends Material {
    public Earth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);
        gs = new GraphicalSurface("earth", 0.5);
	}
}
