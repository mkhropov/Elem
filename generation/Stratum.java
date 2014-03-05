package generation;

import physics.material.Material;
import stereometry.Point;

public class Stratum {
    public int x;
    public int y;
	public Point O;
	public double rmin;
	public double rmax;
	public double width;
	public Material m;
	public static final int C_NUM=3;
	public static final int C_MAX=5;
	public int coef[];
	public double offset[];

    public Stratum(int x, int y, double rmin, double rmax, double width, Material m) {
        this.x = x;
        this.y = y;
		this.m = m;
		this.O = new Point(x, y, 0);
		this.rmin = rmin;
		this.rmax = rmax;
		this.width = width;
		this.coef = new int[C_NUM];
		for (int i=0; i<C_NUM; i++)
			coef[i] = Generator.getInstance().rnd.nextInt(C_MAX);
		this.offset = new double[C_NUM];
		for (int i=0; i<C_NUM; i++)
			offset[i] = Generator.getInstance().rnd.nextDouble()*2*Math.PI;
	}

	public double R(double phi) { // border
		double res = 1.;
		for (int i=0; i<C_NUM; i++)
			res *= (Math.cos(coef[i]*phi+offset[i])+1.)/2.;
//		res += 1;
		return rmin+(rmax-rmin)*res;
	}

	public boolean isIn(Point p) {
		double r = O.distProj(p);
		double phi = Math.acos((p.x-x)/r)*Math.signum(p.y-y);
		if (r < 0.01)
			return true;
		else
			return (r <= R(phi));
	}

	public double w(double a) { // a in [0, 1]
		return Math.cos(a*a*a*a*Math.PI/2.);
	}

	public double w(Point p) { //assuming isIn(p)==true NO MORE
		double r = O.distProj(p);
		double phi = Math.acos((p.x-x)/r)*Math.signum(p.y-y);
		if (r < 0.1)
			return width;
		else
			return width*w(Math.min(r/R(phi), 1.));
	}
}
