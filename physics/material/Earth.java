package physics.material;
import physics.*;

public class Earth extends Material {
    public Earth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);
		tex_u = 0.25f;
		tex_v = 0.f;
		rand = 0.5f;
		hardness = HARD_EARTH;
	}
}
