package pathfind;

import world.Block;

public class ConditionNone implements Condition {
	@Override
	public boolean suits(Block b){
		return false;
	}

	public ConditionNone(){
	}
}
