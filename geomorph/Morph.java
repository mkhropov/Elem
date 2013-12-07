import Math.*;
abstract public class Morph {

	public class P{
		int x;
		int y;
		int z;
	}

	public class WP { //weighted block, used in transforms
		P p;
		double w;
	}

	public class Front { // NOTE: it's an oriented vector!
		P p1;
		P p2;
		Front(P p1, P p2){
			this.p1 = p1;
			this.p2 = p2;
			this.p2.z = this.p1.z; //it should be in a horizontal plane!
		}
	}

	abstract WP[] preimage(P p); //a point preimage - weighted by volume

	abstract void apply();
}
