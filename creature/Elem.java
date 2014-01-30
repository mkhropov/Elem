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

    public Elem(World w, Block b){
        super(w, b);
        pb = b;
        np = p;
        mv = new Vector(0., 0., 0.);
        speed = 0.001d;
        gen = new Random(b.x+b.y+b.z);
        action = 0;
    }

    @Override
    public boolean canWalk(Block b){
        return (b.m == null) || (b.m.w < (1. - Elem.size));
    }

    @Override
    public boolean canMove(Block b1, Block b2){
        if (!canWalk(b2)) return false;
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1)) return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2) return false;
        if (dz == 0)
            return (canWalk(w.blockArray[b1.x+dx][b1.y][b1.z]) ||
                    canWalk(w.blockArray[b1.x][b1.y+dy][b1.z]));
        if (dz > 0)
            return (w.blockArray[b1.x][b1.y][b1.z+1].m==null);
        if (dz < 0)
            return (w.blockArray[b1.x+dx][b1.y+dy][b1.z].m == null);
        return true;
    }

    @Override
    public boolean move(Block b){
        if (!canMove(this.b, b))
            return false;
        else { //delayed movement - we are in process
            pb = this.b;
            setBlock(b, true);
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
		if (b1.equals(b2)) return false;
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
            return (w.blockArray[b1.x+dx][b1.y+dy][b1.z].m == null);
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
                if (!((b.x+dx>=0)&&(b.x+dx<w.xsize))) continue;
                if (!((b.y+dy>=0)&&(b.y+dy<w.ysize))) continue;
                if (!((b.z+dz>=0)&&(b.z+dz<w.zsize))) continue;
                t = w.blockArray[b.x+dx][b.y+dy][b.z+dz];
                if (!canMove(b, t)) continue;
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
				b.destroy(w);
				return true;
			}
			return false;
        }
    }

    @Override
    public final boolean placeBlock(Block b, Material m){
        if ((!canReach(this.b, b)) || (b.m != null) ||
			!((item.type == Item.TYPE_BUILDABLE) && (item.m.equals(m))))
            return false;
        else {
            b.m = new Substance(m, 1.d);
			item = null;
            Renderer.getInstance().updateBlock(b.x, b.y, b.z);
            return true;
        }
    }
}
