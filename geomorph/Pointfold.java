package geomorph;

import stereometry.*;
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
		this.bb = new BoundBox(p.x-l, p.y-l, p.z-h, p.x+l, p.y+l, p.z+1);
    }

// for now Image == Preimage. Mostly - not counting bottom (d)
	@Override
    public final boolean inImage(Point p){
		return  (p.z >= this.p.z-h+d)    &&
				(this.p.distProj(p) <= l);
    }

	@Override
    public final Point preimage(Point p){
        if (!inImage(p)){
            return p;
        } 
        Point pi = new Point(p);
        double r = this.p.distProj(pi);
        if(r<l){
			double t = (Math.cos(Math.PI*r*r*r/(l*l*l))+1);
			t *= .5d;
			t *= d;
		    pi.z += t;
		}
        return pi;
    }
	
	@Override
	public final BoundBox affected(){
		return bb;
	}
}

