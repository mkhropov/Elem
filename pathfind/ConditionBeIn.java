package pathfind;

import world.Block;

public class ConditionBeIn implements Condition {

	public boolean suits(Block b){
		return this.b.isSame(b);
	}

	Block b;

	public ConditionBeIn(Block b){
		this.b = b;
	}
}
