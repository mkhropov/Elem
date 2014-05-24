package player;

import utils.Initializable;
import utils.Named;


public class ZoneTemplate implements Initializable, Named{
	public String name;
	public float[] color;
	
	public ZoneTemplate() {
		color = new float[3];
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 1.0f;
	}
	
	@Override
	public void initialize() {
	}

	@Override
	public String getName() {
		return name;
	}
	
}
