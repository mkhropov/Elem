package pathfind;

import item.Inventory;
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
		return World.getInstance().items.getInventory(b).suitsConditionFree(t);
	}
}
