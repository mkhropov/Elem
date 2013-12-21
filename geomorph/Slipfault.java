package geomorph;

import stereometry.*;
/* Slipfault is a fault-type extension geomorphing, when the fault angle
 * is less more then 45' and the stratums are slipping down the fault
 * It's commonly called 'dipslip'
 */

public class Slipfault extends Morph {
    Front f; // fault front line. Fault will be cw of front vector
    double l; // morph length of extending plate
    double h; // morph height of extening plate
    double d; // depth the plate has dropped down

    public Slipfault(Front f, double l, double h, double d){
        this.f = f;
        this.l = l;
        this.h = h;
        this.d = d;
    }


// for now Image == Preimage. Mostly - not counting bottom (d)
	@Override
    public final boolean inImage(Point p){
		return  (p.z <= f.p1.z)      &&
	     		(f.isUp(p))          &&
				(p.z >= f.p1.z-h-d)    &&
				(f.distProj(p) <= l) &&
				(f.isInside(p));
    }

	@Override
    public final Point preimage(Point p){
        if (!inImage(p)){
            return p;
        } 
        Point pi = new Point(p);
        double r = f.distProj(pi);
        if(r<l){
			double t = (Math.cos(Math.PI*r*r*r/(l*l*l))+1);
			t *= .5d;
			t *= d;
		    pi.z += t;
		}
        return pi;
    }
/*
	@Override
    public final Point image(Point p){
        if (!inImage(p)){
            return p;
        }
        Point ip = new Point(p);
        double r = f.distProj(ip);
        ip.z -= d*.5d*(Math.cos(r*r*r/(Math.PI*Math.PI))+1);
        return ip;
    }*/
}

