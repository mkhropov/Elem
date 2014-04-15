package pathfind;

import creature.Creature;
import world.Block;

public class ConditionReach implements Condition {
	@Override
	public boolean suits(Block b){
		return this.c.canReach(b, this.b);
	}

	Block b;
	Creature c;

	public ConditionReach(Block b, Creature c){
		this.b = b;
		this.c = c;
	}
}
