package generation.morphs;

import stereometry.*;
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
		double sig = (f.isUp(pi))?(-1.):(1.);
        if(r<l){
			double t = Math.abs(Math.sin(Math.PI*r/(l*2))-
								Math.signum(Math.PI*r/(l*2)));
			t *= 2.2d*d;
			t *= Math.sin(Math.PI*r/l);
		    pi.z = pi.z+sig*t;
		}
        return pi;
    }
}

