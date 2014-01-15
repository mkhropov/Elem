package physics.material;
import physics.*;

public class Material {
	public static final int MATERIAL_NONE = 0;
	public static final int MATERIAL_STONE = 1;
	public static final int MATERIAL_EARTH = 2;
	public static final int MATERIAL_MARBLE = 3;
	public static final int MATERIAL_GRANITE = 4;
	public static final int MATERIAL_BEDROCK = 5;
	public static final int MATERIAL_MAX = 6;
    public Temperature tFreeze;
    public Temperature tBoil;
	public int gsid;
	public double hardness;
	public static final double HARD_NONE = 0.d;
	public static final double HARD_EARTH = 50.d;
	public static final double HARD_STONE = 100.d;
	public static final double HARD_STEEL = 500.d;
	public static final double HARD_MAX = 100000.d;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);
		gsid = 0;
		hardness = HARD_NONE;
    }
}
