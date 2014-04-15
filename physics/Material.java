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

	public static int BLOCK = 0;
	public static int FLOOR = 1;

	public static int[][] weight;

	public static int UP = 0;
	public static int SIDE = 1;
	public static int DOWN = 2;

	public static int[][][] support;

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

		weight = new int[2][MATERIAL_MAX];
		weight[0][MATERIAL_NONE]   = 0;
		weight[0][MATERIAL_STONE]  = 100;
		weight[0][MATERIAL_EARTH]  = 100;
		weight[0][MATERIAL_MARBLE] = 100;
		weight[0][MATERIAL_GRANITE]= 100;
		weight[0][MATERIAL_BEDROCK]= 100;
		weight[1][MATERIAL_NONE]   = 0;
		weight[1][MATERIAL_STONE]  = 60;
		weight[1][MATERIAL_EARTH]  = 60;
		weight[1][MATERIAL_MARBLE] = 60;
		weight[1][MATERIAL_GRANITE]= 60;

		support = new int[2][3][MATERIAL_MAX];
		//support UP
		support[0][0][MATERIAL_NONE]   = 0;
		support[0][0][MATERIAL_STONE]  = 150;
		support[0][0][MATERIAL_EARTH]  = 150;
		support[0][0][MATERIAL_MARBLE] = 150;
		support[0][0][MATERIAL_GRANITE]= 150;
		support[0][0][MATERIAL_BEDROCK]= 150;
		support[1][0][MATERIAL_NONE]   = 0;
		support[1][0][MATERIAL_STONE]  = 0;
		support[1][0][MATERIAL_EARTH]  = 0;
		support[1][0][MATERIAL_MARBLE] = 0;
		support[1][0][MATERIAL_GRANITE]= 0;
		support[1][0][MATERIAL_BEDROCK]= 0;
		// support SIDE
		support[0][1][MATERIAL_NONE]   = 0;
		support[0][1][MATERIAL_STONE]  = 35;
		support[0][1][MATERIAL_EARTH]  = 35;
		support[0][1][MATERIAL_MARBLE] = 35;
		support[0][1][MATERIAL_GRANITE]= 35;
		support[0][1][MATERIAL_BEDROCK]= 35;
		support[1][1][MATERIAL_NONE]   = 0;
		support[1][1][MATERIAL_STONE]  = 26;
		support[1][1][MATERIAL_EARTH]  = 26;
		support[1][1][MATERIAL_MARBLE] = 26;
		support[1][1][MATERIAL_GRANITE]= 26;
		support[1][1][MATERIAL_BEDROCK]= 26;
		support[1][1][MATERIAL_BEDROCK]= 26;
		//support DOWN
		support[0][2][MATERIAL_NONE]   = 0;
		support[0][2][MATERIAL_STONE]  = 15;
		support[0][2][MATERIAL_EARTH]  = 15;
		support[0][2][MATERIAL_MARBLE] = 15;
		support[0][2][MATERIAL_GRANITE]= 15;
		support[0][2][MATERIAL_BEDROCK]= 15;
		support[1][2][MATERIAL_NONE]   = 0;
		support[1][2][MATERIAL_STONE]  = 5;
		support[1][2][MATERIAL_EARTH]  = 5;
		support[1][2][MATERIAL_MARBLE] = 5;
		support[1][2][MATERIAL_GRANITE]= 5;
		support[1][2][MATERIAL_BEDROCK]= 5;
		support[1][2][MATERIAL_BEDROCK]= 5;
		support[1][2][MATERIAL_BEDROCK]= 5;
	}

	static public double digTime(double str, int m) {
		if (str<=hardness[m])
			return -1.;
		else
			return (400./(str-hardness[m]));
	}
}
