package generation.morphs;

import stereometry.BoundBox;
import stereometry.Front;
import stereometry.Point;
import stereometry.Vector;
import world.World;
/* Wavefold is a fold-type geomorphing, when the fold
 * is linear and vertically directed, and goes both up and down
 */

public class Wavefold extends Morph {
    Front f; // the fold line
    double l; // morph length
	double h; //maximum depth of morph applying
    double d; // depth the plate has dropped down/moved up in the centre

    public Wavefold(Front f, double l, double h, double d){
        this.f = f;
        this.l = l;
        this.h = h;
        this.d = d;
		Vector v = new Vector(f.y, -f.x, 0);
		v.normalize(); v.scale(l);
		this.bb = new BoundBox(Math.min(f.p1.x, f.p2.x)-Math.abs(v.x),
                               Math.min(f.p1.y, f.p2.y)-Math.abs(v.y),
                               0,
                               Math.max(f.p1.x, f.p2.x)+Math.abs(v.x),
                               Math.max(f.p1.y, f.p2.y)+Math.abs(v.y),
							   World.getInstance().zsize);

    }


// for now Image == Preimage. Mostly - not counting bottom (d)
	@Override
    public final boolean inImage(Point p){
		return  (p.z >= this.f.p1.z-h)    &&
				(f.isInside(p))          &&
				(f.distProj(p) <= l);
    }

	@Override
    public final void preimage(Point p){
        if (!inImage(p)){
            return;
        }
        double r = this.f.distProj(p);
		double sig = (f.isUp(p))?(-1.):(1.);
        if(r<l){
			double t = Math.abs(Math.sin(Math.PI*r/(l*2))-
								Math.signum(Math.PI*r/(l*2)));
			t *= 2.2d*d;
			t *= Math.sin(Math.PI*r/l);
		    p.z -= sig*t;
		}
    }
}

