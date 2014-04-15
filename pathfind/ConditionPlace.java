package pathfind;

import world.Block;

public class ConditionPlace implements Condition {
	@Override
	public boolean suits(Block b){
		return this.b.isSame(b);
	}

	Block b;

	public ConditionPlace(Block b){
		this.b = b;
	}
}
