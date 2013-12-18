package pathfind;

import world.Block;

public class ConditionNone implements Condition {
	public boolean suits(Block b){
		return false;
	}

	public ConditionNone(){
	}
}
