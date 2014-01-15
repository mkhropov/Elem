package pathfind;

import world.Block;
import item.ItemTemplate;

public class ConditionItem implements Condition {

	public ItemTemplate t;

	public ConditionItem(ItemTemplate t){
		this.t = t;
	}

	@Override
	public boolean suits(Block b){
		for (int i=0; i<b.item.size(); ++i)
			if (t.suits(b.item.get(i)))
				return true;
		return false;
	}
}
