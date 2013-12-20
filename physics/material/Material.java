package physics.material;
import physics.*;
import graphics.GraphicalSurface;

public class Material {
    public Temperature tFreeze;
    public Temperature tBoil;
	public GraphicalSurface gs;
	public double hardness;
	public static final double HARD_NONE = 0.d;
	public static final double HARD_EARTH = 50.d;
	public static final double HARD_STONE = 100.d;
	public static final double HARD_STEEL = 500.d;
	public static final double HARD_MAX = 100000.d;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);
		gs = new GraphicalSurface("void", 0.5);
		hardness = HARD_NONE;
    }
}
