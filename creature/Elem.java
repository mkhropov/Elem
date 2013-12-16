package creature;

import java.util.Random;
import org.lwjgl.opengl.GL11;

import world.*;
import physics.material.*;


public class Elem extends Creature implements Worker{
    public static double size = 0.5;
    public Random gen;
	Material m; //Dirty hacks here

    public Elem(World w, Block b){
        super(w, b);
		m = w.material[2]; //because fuck you. That's why
        gen = new Random(b.x+b.y+b.z);
    }

    @Override
    public boolean canWalk(Block b){
        return (b.m == null) || (b.m.w < (1. - Elem.size));
    }

    @Override
    public boolean canMove(Block b1, Block b2){
        if (!canWalk(b2)) return false;
        return canReach(b1, b2);
    }

    boolean canReach(Block b1, Block b2){
        int dx = b2.x-b1.x;
        int dy = b2.y-b1.y;
        int dz = b2.z-b1.z;
        if ((Math.abs(dx)>1) || (Math.abs(dy)>1) || (Math.abs(dz)>1)) return false;
        if ((Math.abs(dx)+Math.abs(dy)+Math.abs(dz)) > 2) return false;
        if (dz == 0)
            return ((w.blockArray[b1.x+dx][b1.y][b1.z].m==null) ||
                    (w.blockArray[b1.x][b1.y+dy][b1.z].m==null));
        if (dz > 0)
            return (w.blockArray[b1.x][b1.y][b1.z+1].m==null);
        if (dz < 0)
            return (w.blockArray[b1.x+dx][b1.y+dy][b1.z].m == null);
        return true;
    }

    @Override
    public void iterate() {
        int dx = 2*gen.nextInt(2)-1;
        int dy = 2*gen.nextInt(2)-1;
        int dz = 2*gen.nextInt(2)-1;
        if (!((b.x+dx>=0)&&(b.x+dx<w.xsize))) return;
        if (!((b.y+dy>=0)&&(b.y+dy<w.ysize))) return;
        if (!((b.z+dz>=0)&&(b.z+dz<w.zsize))) return;
        Block t = w.blockArray[b.x+dx][b.y+dy][b.z+dz];
        int p = gen.nextInt(100);
        if ((p < 60) && (canMove(b, t)))
            move(t);
        else if (p < 70)
            placeBlock(t, w.material[0]);
        else
            destroyBlock(t);
    }

    @Override
	public void draw() {
		m.texture.bind();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.2);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.2);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.2);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.8);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.8);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.2);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.2);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.8);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.2, b.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.2, b.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.8);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.8);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,0.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.2);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,0.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.2);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(1.0,1.0);
		GL11.glVertex3d(b.x+0.8, b.y+0.8, b.z+0.8);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glTexCoord2d(0.0,1.0);
		GL11.glVertex3d(b.x+0.2, b.y+0.8, b.z+0.8);
		GL11.glEnd();

	}

    @Override
    public final boolean destroyBlock(Block b){
        if (!canReach(this.b, b))
            return false;
        else {
            b.m = null;
            return true;
        }
    }

    @Override
    public final boolean placeBlock(Block b, Material m){
        if ((!canReach(this.b, b)) || (b.m != null))
            return false;
        else {
            b.m = new Substance(m, 1.d);
            return true;
        }
    }
}
