package physics.material;
import physics.*;

public class Stone extends Material {
	public Stone() {
        tFreeze = new Temperature(4000);
        tBoil = new Temperature(5000);
        tBoil =  new Temperature(5000);
		tex_u = 0.5f;
		tex_v = 0.5f;
		rand = 0.6f;
		hardness = HARD_STONE;
	}
}
