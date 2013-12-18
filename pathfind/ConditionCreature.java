package pathfind;

import world.Block;
import creature.Creature;
import java.util.ArrayList;

public class ConditionCreature implements Condition {

        @Override
	public boolean suits(Block b){
		ArrayList<Block> near = b.nearest(c.w);
		Block t;
		for (int i=0; i<near.size(); ++i){
			t = near.get(i);
			if (this.c.canReach(b, t) &&
				t.creature.contains(target))
				return true;
		}
		return false;
	}

	Creature c, target;

	public ConditionCreature(Creature c, Creature target){
		this.c = c;
		this.target = target;
	}
}
