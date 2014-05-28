package pathfind;

import item.Item;
import item.ItemTemplate;
import java.util.ArrayList;
import world.Block;
import world.World;

public class ConditionItem implements Condition {

	public String t;

	public ConditionItem(String t){
		this.t = t;
	}

	@Override
	public boolean suits(Block b){
		ArrayList<Item> l = World.getInstance().getItem(b);
		for (int i=0; i<l.size(); ++i)
			if (l.get(i).suitsConditionFree(t))
				return true;
		return false;
	}
}
