/* Slipfault is a fault-type extension geomorphing, when the fault angle
 * is less more then 45' and the stratums are slipping down the fault
 * It's commonly called 'dipslip'
 */

public class Slipfault extends Morph {
	Front f; // fault front line. Fault will be cw of front vector
	double l; // morph length of extending plate
	double h; // morph height of extening plate
	double d; // depth the plate has dropped down

	SlipFault(Front f, double l, double h, double d){
		this.f = f;
		this.l = l;
		this.h = h;
		this.d = d;
	}


// for now Image == Preimage
	public final boolean inImage(Point p){
		if ((p.z > f.p1.z)      ||
			(!f.isUp(p))        ||
			(p.z < f.p1.z-d)    ||
			(f.distProj(p) > l) ||
            (!f.isInside(p)       )
			return false;
		else
			return true;
	}

	public final Point preimage(Point p){
		if (!inImage(p)){
			return p;
		}
	}

    public final Point image(Point p){
        if (!inImage(p)){
            return p;
        }
    }

	public final void apply(Chunk c){
	}
}

