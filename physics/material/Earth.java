package physics.material;
import physics.*;

public class Earth extends Material {
    public Earth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);
        gsid = 2;
	}
}
