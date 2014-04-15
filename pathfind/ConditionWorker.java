package pathfind;

import creature.Creature;
import creature.Worker;
import player.Order;
import world.Block;
import world.World;

public class ConditionWorker implements Condition {
	@Override
	public boolean suits(Block b){
		for (Creature c: World.getInstance().getCreature(b))
			if ((c instanceof Worker) &&
				(c.capableOf(o)))
				return true;
		return false;
	}

	Order o;

	public ConditionWorker(Order o){
		this.o = o;
	}
}
