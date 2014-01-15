package physics.material;
import physics.*;

public class Stone extends Material {
	public Stone() {
        tFreeze = new Temperature(4000);
        tBoil = new Temperature(5000);
		gsid = 1;
        tBoil =  new Temperature(5000);
		hardness = HARD_STONE;
	}
}
