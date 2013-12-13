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

    public final boolean canWalk(Block b){
        if ((b.m == null) || (b.m.w < (1. - this.size)))
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

    boolean canReach(Block b){
        if (b == this.b) return false;
        if (Math.abs(this.b.x-b.x)>1) return false;
        if (Math.abs(this.b.y-b.y)>1) return false;
        if (Math.abs(this.b.z-b.z)>1) return false;
        return true;
    }

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

    public final boolean destroyBlock(Block b){
        if (!canReach(b))
            return false;
        else {
            b.m = null;
            return true;
        }
    }

    public final boolean placeBlock(Block b, Material m){
        if ((!canReach(b)) || (b.m != null))
            return false;
        else {
            b.m = new Substance(m, 1.d);
            return true;
        }
    }
}
