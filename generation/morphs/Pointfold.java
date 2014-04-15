package generation.morphs;

import stereometry.BoundBox;
import stereometry.Point;
import world.World;
/* Pointfold is a fold-type geomorphing, when the fold
 * is located circulary around it's peak/valley
 */

public class Pointfold extends Morph {
    Point p; // fault front line. Fault will be cw of front vector
    double l; // morph radius
	double h; //maximum depth of morph applying
    double d; // depth the plate has dropped down/moved up in the centre

    public Pointfold(Point p, double l, double h, double d){
        this.p = p;
        this.l = l;
        this.h = h;
        this.d = d;
		this.bb = new BoundBox(p.x-l, p.y-l, 0, p.x+l, p.y+l, World.getInstance().zsize);
    }

// Image == Preimage
	@Override
    public final boolean inImage(Point p){
		return  (p.z >= this.p.z-h)    &&
				(this.p.distProj(p) <= l);
    }

	@Override
    public final void preimage(Point p){
        if (!inImage(p)){
            return;
        }
        double r = this.p.distProj(p);
        if (r < l){
			double t = (Math.cos(Math.PI*r*r/(l*l))+1);
			t *= .5d;
			t *= d;
			if (p.z < this.p.z) // do not overextend down
				t *= (p.z-this.p.z+h)/h;
		    p.z -= t;
		}
    }
}

