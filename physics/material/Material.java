package physics.material;
import physics.*;

public class Material {
    public Temperature tFreeze;
    public Temperature tBoil;
	public int gsid;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);
		gsid = 0;
    }
}
