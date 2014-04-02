package item;

import physics.material.Material;

public class ItemTemplate{

	public int type;
	public char m; //material code

	public ItemTemplate(int type, char m){
		this.type = type;
		this.m = m;
	}

	public boolean suits(Item i){
		if (i==null)
			return false;
		return (((i.type == type) ||
				 (type == Item.TYPE_NONE)) &&
				((i.m == m) ||
				 (m == Material.MATERIAL_NONE)));
	}
}
