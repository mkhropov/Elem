package physics.material;
import physics.*;

public class Stone extends Material {
	public Stone() {
		tex_u = 0.5f;
		tex_v = 0.5f;
		rand = 0.6f;
		hardness = HARD_STONE;
	}
}
