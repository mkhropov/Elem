package item;

import physics.Material;
import utils.Initializable;
import utils.Named;

public class ItemTemplate implements Initializable, Named{
	public String name;
	public String title;
	public String description;
	public String model;
	public String texture;
	public int weight;
	
	@Override
	public void initialize() {
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean suitsCondition(String c) {
		return name.equalsIgnoreCase(c); //For now
	}
}
