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
    public static double size = 0.5;
    public Random gen;
	Material m; //Dirty hacks here
    Block pb;
    Vector mv;

    public Elem(Block b){
        super(b);
        pb = b;
        np = p;
        mv = new Vector(0., 0., 0.);
        speed = 0.001d;
        gen = new Random(b.x+b.y+b.z);
        action = 0;
		mid = graphics.ModelList.getInstance().findId("elem");
		gsid = graphics.GSList.getInstance().findId("elem");
    }

    @Override
    public boolean canWalk(Block b){
        return (b.m == Material.MATERIAL_NONE);
		// || (b.m.w < (1. - Elem.size));
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
    public boolean move(Block b){
        if (!canMove(this.b, b))
            return false;
        else { //delayed movement - we are in process
            pb = this.b;
            setBlock(b, true);
			turn(np);
            mv = new Vector(p, np);
            if (!mv.isZero()){
                mv.normalize();
                mv.scale(speed);
            }
            action = 1;
            return true;
        }
    }

    public boolean canReach(Block b1, Block b2){
		if (b1.isSame(b2)) return false;
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

    @Override
    public void iterate(long dT) {
        switch (action){
            case 1: //moving
                p.add(mv, (double)dT); break;
        }
        if (p.dist(np) < speed*dT){ //end of movement
            action = 0;
            pb = b;
            int dx, dy, dz;
            Block t;
            while (true) {
                dx = gen.nextInt(3)-1;
                dy = gen.nextInt(3)-1;
                dz = gen.nextInt(3)-1;
				if (!World.getInstance().isIn(b.x+dx, b.y+dy, b.z+dz))
					continue;
                t = World.getInstance().getBlock(b.x+dx, b.y+dy, b.z+dz);
                if (!canMove(b, t))
					continue;
                break;
            }
 //           int p = gen.nextInt(100);
 //           if (p < 70)
            move(t);
//            else if (p < 85)
 //               placeBlock(t, w.material[0]);
    //        else
    //            destroyBlock(t);
        }
    }

    @Override
    public final boolean destroyBlock(Block b){
        if (!canReach(this.b, b))
            return false;
        else {
			if (canDig(b)){
				turn(new Point(b));
				World.getInstance().destroyBlock(b);
				return true;
			}
			return false;
        }
    }

    @Override
    public final boolean placeBlock(Block b, char m){
        if ((!canReach(this.b, b)) || (b.m != Material.MATERIAL_NONE) ||
			!((item.type == Item.TYPE_BUILDABLE) && (item.m == m)))
            return false;
        else {
			turn(new Point(b));
            World.getInstance().m[b.x][b.y][b.z] = m;
			item = null;
            Renderer.getInstance().updateBlock(b.x, b.y, b.z);
            return true;
        }
    }
}
