package pathfind;

import world.Block;

public class ConditionItem implements Condition {
        @Override
	public boolean suits(Block b){
            return false;
        }
}
