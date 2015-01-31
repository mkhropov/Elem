package pathfind;

import world.Block;

public interface MoveMethod {
    public boolean canStay(Block b);
    public boolean canMove(Block b1, Block b2);
}
