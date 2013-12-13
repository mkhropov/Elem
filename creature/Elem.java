package creature;

import world.*;
import java.util.Random;

public class Elem extends Creature {

    public static double size = 0.5;
    public Random gen;

    public Elem(World w, Block b){
        super(w, b);
        gen = new Random(b.x+b.y+b.z);
    }

    public final boolean canWalk(Block b){
        if (b.m.w < (1. - this.size))
            return true;
        else
            return false;
    }

    public final boolean canMove(Block b1, Block b2){
        if (!canWalk(b2)) return false;
        if (Math.abs(b1.x-b2.x)>1) return false;
        if (Math.abs(b1.y-b2.y)>1) return false;
        if (Math.abs(b1.z-b2.z)>1) return false;
        return true;
    }

    public void iterate() {
        int dx = 2*gen.nextInt(1)-1;
        int dy = 2*gen.nextInt(1)-1;
        int dz = 2*gen.nextInt(1)-1;
        if (!((b.x+dx>=0)&&(b.x+dx<w.xsize))) return;
        if (!((b.y+dy>=0)&&(b.y+dy<w.ysize))) return;
        if (!((b.z+dz>=0)&&(b.z+dz<w.zsize))) return;
        Block t = w.blockArray[b.x+dx][b.y+dy][b.z+dz];
        if (canMove(b, t))
            move(t);
    }
}
