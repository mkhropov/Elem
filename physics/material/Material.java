package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Material {
    public Temperature tFreeze;
    public Temperature tBoil;
	public GraphicalSurface gs;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);
		gs = new GraphicalSurface("void", 0.5);
    }
}
