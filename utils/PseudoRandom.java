package utils;

import stereometry.Point;
import stereometry.Vector;

public class PseudoRandom {
	private static final Vector[] basePos = {new Vector(0.2, 0.2, 0.0), 
									new Vector(0.2, -0.2, 0.0),
									new Vector(-0.2, 0.2, 0.0),
									new Vector(-0.2, -0.2, 0.0),
									new Vector(0.0, 0.0, 0.2)};

	public static int pick(int max, int num) {
		return ((num+1)*1447)%max;
	}

	public static Vector vector2d(int x, int y, int z, int num){
		return new Vector(Math.sin(1.7*x + 0.5*y +1.1*z - num),
						Math.cos(2.1*x - 1.9*y +1.1*z - num),
						0);
	}


	public static Point position(int x, int y, int z, int num){
		Point p = new Point(x,y,z);
		if(num>=5) return p;
		p.add(basePos[num], 1.);
		p.add(vector2d(x,y,z,num), 0.1);
		return p;
	}
}
