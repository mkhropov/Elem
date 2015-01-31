package pathfind;

import world.Block;
import world.World;

public class MoveMethodWalk implements MoveMethod {
	public MoveMethodWalk() {};

@Override
    public boolean canStay(Block b)
	{
		World w = World.getInstance();
		return w.isIn(b.x, b.y, b.z) &&
			(b.z != 0) &&
			w.hasSolidFloor(b.x, b.y, b.z) &&
			w.isEmpty(b.x, b.y, b.z);
    }

@Override
    public boolean canMove(Block b1, Block b2)
	{
		World w = World.getInstance();
        if (!canStay(b2) || !canStay(b1))
			return false;
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1))
			return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2)
			return false;
        if (dz == 0)
            return (canStay(w.getBlock(b1.x+dx, b1.y, b1.z)) ||
                    canStay(w.getBlock(b1.x, b1.y+dy, b1.z)));
        if (dz > 0)
            return (!w.hasSolidFloor(b1.x, b1.y, b1.z+1));
        if (dz < 0)
            return (!w.hasSolidFloor(b1.x+dx, b1.y+dy, b1.z));
        return true;
    }
}
