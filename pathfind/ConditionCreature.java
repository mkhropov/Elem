package pathfind;

import java.util.ArrayList;

import world.World;
import world.Block;
import creature.Creature;

public class ConditionCreature implements Condition {

	@Override
	public boolean suits(Block b){
		ArrayList<Block> near = b.nearest(c.w);
		Block t;
		for (int i=0; i<near.size(); ++i){
			t = near.get(i);
			if (this.c.canReach(b, t) &&
				World.getInstance().getCreature(b).contains(target))
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
