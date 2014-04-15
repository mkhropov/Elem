package physics;

public class Material {
	public static final char MATERIAL_NONE = 0;
	public static final char MATERIAL_STONE = 1;
	public static final char MATERIAL_EARTH = 2;
	public static final char MATERIAL_MARBLE = 3;
	public static final char MATERIAL_GRANITE = 4;
	public static final char MATERIAL_BEDROCK = 5;
	public static final char MATERIAL_MAX = 6;
	public static float[] tex_u;
	public static float[] tex_v;
	public static float[] rand;
	public static float[] hardness;

	static {
		hardness = new float[MATERIAL_MAX];
		hardness[MATERIAL_NONE]   = 0.f;
		hardness[MATERIAL_STONE]  = 100.f;
		hardness[MATERIAL_EARTH]  = 50.f;
		hardness[MATERIAL_MARBLE] = 150.f;
		hardness[MATERIAL_GRANITE]= 200.f;
		hardness[MATERIAL_BEDROCK]= 100000.f;

		tex_u = new float[MATERIAL_MAX];
		tex_u[MATERIAL_NONE]   = 0.f;
		tex_u[MATERIAL_STONE]  = 0.5f;
		tex_u[MATERIAL_EARTH]  = 0.25f;
		tex_u[MATERIAL_MARBLE] = 0.0f;
		tex_u[MATERIAL_GRANITE]= 0.5f;
		tex_u[MATERIAL_BEDROCK]= 0.75f;

		tex_v = new float[MATERIAL_MAX];
		tex_v[MATERIAL_NONE]   = 0.0f;
		tex_v[MATERIAL_STONE]  = 0.5f;
		tex_v[MATERIAL_EARTH]  = 0.f;
		tex_v[MATERIAL_MARBLE] = 0.5f;
		tex_v[MATERIAL_GRANITE]= 0.0f;
		tex_v[MATERIAL_BEDROCK]= 0.25f;

		rand = new float[MATERIAL_MAX];
		rand[MATERIAL_NONE]   = 0.0f;
		rand[MATERIAL_STONE]  = 0.6f;
		rand[MATERIAL_EARTH]  = 0.5f;
		rand[MATERIAL_MARBLE] = 0.3f;
		rand[MATERIAL_GRANITE]= 0.5f;
		rand[MATERIAL_BEDROCK]= 0.5f;
	}

	static public double digTime(double str, int m) {
		if (str<=hardness[m])
			return -1.;
		else
			return (400./(str-hardness[m]));
	}
}
