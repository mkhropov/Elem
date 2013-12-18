package pathfind;

import world.Block;
import creature.Creature;

public class ConditionReach implements Condition {

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
