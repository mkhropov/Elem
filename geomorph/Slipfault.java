/* Slipfault is a fault-type extension geomorphing, when the fault angle
 * is less more then 45' and the stratums are slipping down the fault
 * It's commonly called 'dipslip'
 */

public class Slipfault extends Morph {
	double a; // fault plane angle
	Front f; // fault front line. Fault will be cw of front vector
	double l; // morph length of extending plate
	double h; // morph height of extening plate
	double d; // depth the plate has dropped down
	SlipFault(Front f, double l, double h, double d){
		this.a = a;
		this.f = f;
		this.l = l;
		this.h = h;
		this.d = d;
	}

	public final boolean morphed(P p){
		if ((P.z > f.p1.z) ||
			(!f.isUp(p)) ||
			(P.z < f.p1.z-d) ||
			(f.distProj(p) > l))
			return false;
		else
			return true;
	}

	public final WP[] preimage(P p){
		WP[] wp = new WP[8];
		if (!morphed(p)){
			wp[0].p = p;
			wp[0].w = 1.d;
			return wp;
		}
		return wp;
	}

	public final void apply(Chunk c){
	}
}

