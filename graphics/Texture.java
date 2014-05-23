package graphics;

import utils.Initializable;
import utils.Named;

public class Texture implements Initializable, Named{
	String name;
	public float tex_u;
	public float tex_v;
	public float rand;
	
	
	@Override
	public void initialize() {
	}

	@Override
	public String getName() {
		return name;
	}
}
