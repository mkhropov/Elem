package physics.material;
import physics.*;

public class Material {
	public static final char MATERIAL_NONE = 0;
	public static final char MATERIAL_STONE = 1;
	public static final char MATERIAL_EARTH = 2;
	public static final char MATERIAL_MARBLE = 3;
	public static final char MATERIAL_GRANITE = 4;
	public static final char MATERIAL_BEDROCK = 5;
	public static final char MATERIAL_MAX = 6;
    public Temperature tFreeze;
    public Temperature tBoil;
	public float tex_u;
	public float tex_v;
	public double hardness;
	public static final double HARD_NONE = 0.d;
	public static final double HARD_EARTH = 50.d;
	public static final double HARD_STONE = 100.d;
	public static final double HARD_STEEL = 500.d;
	public static final double HARD_MAX = 100000.d;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);
		tex_u = 0.f;
		tex_v = 0.f;
		hardness = HARD_NONE;
    }

	public double digTime(double str) {
		if (str<=hardness)
			return -1.;
		else
			return (400./(str-hardness));
	}
}
