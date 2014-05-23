package creature;

import java.util.Random;
import stereometry.Vector;
import world.Block;
import world.World;

public class Elem extends Creature implements Worker{
    public static double size = 0.5;
    public Random gen;

    public Elem(Block b){
        super(b);
        np = p;
        mv = new Vector(0., 0., 0.);
        speed = 0.001d;
		this.capable = new boolean[]{true, true, true, true};
		mid = core.Data.Models.getId("elem");
		gsid = graphics.GSList.getInstance().findId("elem");
    }

	public Elem(){
		super();
		this.capable = new boolean[]{true, true, true, true};
	}

@Override
    public boolean canWalk(Block b){
		World w = World.getInstance();
		return (b.z!=0) && (w.hasSolidFloor(b.x, b.y, b.z)) && (w.isEmpty(b.x, b.y, b.z));
    }

    @Override
    public boolean canMove(Block b1, Block b2){
		World w = World.getInstance();
        if (!canWalk(b2) || !canWalk(b1)) return false;
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1)) return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2) return false;
        if (dz == 0)
            return (canWalk(w.getBlock(b1.x+dx, b1.y, b1.z)) ||
                    canWalk(w.getBlock(b1.x, b1.y+dy, b1.z)));
        if (dz > 0)
            return (!w.hasSolidFloor(b1.x, b1.y, b1.z+1));
        if (dz < 0)
            return (!w.hasSolidFloor(b1.x+dx, b1.y+dy, b1.z));
        return true;
    }

	@Override
    public boolean canReach(Block b1, Block b2){
		if (b1.isSame(b2)) return false;
		if (!canWalk(b1)) return false;
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1)) return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2) return false;
        if (dz == 0)
            return ((dx==0) || (dy==0));
        if (dz > 0)
            return ((dx==0) && (dy==0));
        if (dz < 0)
            return (World.getInstance().isAir(b1.x+dx, b1.y+dy, b1.z));
        return true;
    }
}
