package creature;

import java.util.Random;
import org.lwjgl.opengl.GL11;

import world.*;
import physics.material.*;
import graphics.Renderer;
import stereometry.Point;
import stereometry.Vector;
import item.ItemTemplate;
import item.Item;

public class Elem extends Creature implements Worker{
//    public static double size = 0.5;
//    public Random gen;
//	Material m; //Dirty hacks here
//    Block pb;

    public Elem(Block b){
        super(b);
  //      pb = b;
        np = p;
        mv = new Vector(0., 0., 0.);
        speed = 0.001d;
  //      gen = new Random(b.x+b.y+b.z);
		this.capable = new boolean[]{true, true, true, true};
		mid = graphics.ModelList.getInstance().findId("elem");
		gsid = graphics.GSList.getInstance().findId("elem");
    }

	public Elem(){
		super();
		this.capable = new boolean[]{true, true, true, true};
	}

    @Override
    public boolean canWalk(Block b){
		World w = World.getInstance();
		return (b.z!=0) &&
			(w.m[b.x][b.y][b.z-1] != Material.MATERIAL_NONE) &&
			(w.m[b.x][b.y][b.z] == Material.MATERIAL_NONE);
    }

    @Override
    public boolean canMove(Block b1, Block b2){
		World w = World.getInstance();
        if (!canWalk(b2)) return false;
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1)) return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2) return false;
        if (dz == 0)
            return (canWalk(w.getBlock(b1.x+dx, b1.y, b1.z)) ||
                    canWalk(w.getBlock(b1.x, b1.y+dy, b1.z)));
        if (dz > 0)
            return (w.m[b1.x][b1.y][b1.z+1] == Material.MATERIAL_NONE);
        if (dz < 0)
            return (w.m[b1.x+dx][b1.y+dy][b1.z] == Material.MATERIAL_NONE);
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
            return (World.getInstance().m[b1.x+dx][b1.y+dy][b1.z] ==
					Material.MATERIAL_NONE);
        return true;
    }
}
