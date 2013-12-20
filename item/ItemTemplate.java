package item;

import physics.material.Material;

public class ItemTemplate{

	public int type;
	public Material m;

	public ItemTemplate(int type, Material m){
		this.type = type;
		this.m = m;
	}

	public boolean suits(Item i){
		if (i==null)
			return false;
		return (((i.type == type) ||
				 (type == Item.TYPE_NONE)) &&
				((i.m.equals(m)) ||
				 (m == null)));
	}
}
