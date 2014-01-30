package generation.morphs;

import stereometry.*;
/* Linefold is a fold-type geomorphing, when the fold
 * is linear and vertically directed
 */

public class Linefold extends Morph {
    Front f; // the fold line
    double l; // morph length
	double h; //maximum depth of morph applying
    double d; // depth the plate has dropped down/moved up in the centre

    public Linefold(Front f, double l, double h, double d){
        this.f = f;
        this.l = l;
        this.h = h;
        this.d = d;
    }


// for now Image == Preimage. Mostly - not counting bottom (d)
	@Override
    public final boolean inImage(Point p){
		return  (p.z >= this.f.p1.z-h+d)    &&
				(f.isInside(p))          &&
				(f.distProj(p) <= l);
    }

	@Override
    public final Point preimage(Point p){
        if (!inImage(p)){
            return p;
        } 
        Point pi = new Point(p);
        double r = this.f.distProj(pi);
        if(r<l){
			double t = (Math.cos(Math.PI*r*r*r/(l*l*l))+1);
			t *= .49d;
			t *= d;
		    pi.z += t;
		}
        return pi;
    }
}

