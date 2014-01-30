package physics.mana;

import stereometry.*;
import world.Block;


public class ManaSource extends Vector {


	double power;
	double depletion;

//	Block b;

	public ManaSource(Block b, Vector v, double power){
		super(new Point(b), v);
		this.normalize();
		this.power = power;
		this.depletion = 0.;
		this.scale(power);
		System.out.printf("p1 = %f %f %f\n", p1.x, p1.y, p1.z);
//		this.b = b;
	}

	public Vector getField(Point p){
		//temporary
		//until implement double ellipse in corresponding plane
		Vector v;
		double d;
		v = new Vector(p1, p);
		d = v.len(); d *= d;
		v.normalize();
		v.scale((power-depletion)/(d+1));
		return v;
	}

	public Vector getField(Block b){
		//temporary
		//until implement double ellipse in corresponding plane
		Vector v;
		Point p = new Point(b);
		double d;
		v = new Vector(p1, p);
		d = v.len();// d *= d;
		v.normalize();
		v.scale((power-depletion)/(d+1));
		return v;
	}

/*
	public Vector getFullField(Point p){//for special cases?
		//temporary
		//until implement double ellipse in corresponding plane
		Vector v;
		double l;
		v = new Vector(p1, p);
		d = v.len(); d *= d;
		v.alpha(power/(d+1));
		return v;
	}
*/

	public void deplete(Point p){
		depletion += getField(p).len();
		if (depletion > power)
			depletion = power;
	}

	public void deplete(Block b){
		depletion += getField(b).len();
		if (depletion > power)
			depletion = power;
	}
}
