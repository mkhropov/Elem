package pathfind;

import java.util.ArrayList;

import world.Block;
import world.World;
import item.ItemTemplate;
import item.Item;

public class ConditionItem implements Condition {

	public ItemTemplate t;

	public ConditionItem(ItemTemplate t){
		this.t = t;
	}

	@Override
	public boolean suits(Block b){
		ArrayList<Item> l = World.getInstance().getItem(b);
		for (int i=0; i<l.size(); ++i)
			if (t.suits(l.get(i)))
				return true;
		return false;
	}
}
