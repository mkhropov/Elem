package generation;

import stereometry.BoundBox;
import stereometry.Point;
import world.World;

public class Stratum {
    public int x;
    public int y;
	public Point O;
	public double rmin;
	public double rmax;
	public double width;
	public int m;
	public static final int C_NUM=3;
	public static final int C_MAX=5;
	public int coef[];
	public double offset[];
	public BoundBox bb;
	public Vein vein;

    public Stratum(int x, int y, double rmin, double rmax, double width, int m) {
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
		this.bb = new BoundBox(x-rmax, y-rmax, 0, x+rmax, y+rmax, World.getInstance().zsize);
		this.vein = null;
	}

	public double R(double phi) { // border
		double res = 1.;
		for (int i=0; i<C_NUM; i++)
			res *= (Math.cos(coef[i]*phi+offset[i])+1.)/2.;
//		res += 1;
		return rmin+(rmax-rmin)*res;
	}

	public boolean isIn(Point p) {
		return bb.isIn(p);
	}

	public double w(double a) { // a in [0, 1]
		return Math.cos(a*a*a*a*Math.PI/2.);
	}

	public double w(Point p) { //assuming isIn(p)==true NO MORE
		double r = O.distProj(p);
		double phi = Math.atan2(p.x-x, p.y-y);
		if (r < 0.1)
			return width;
		else
			return width*w(Math.min(r/R(phi), 1.));
	}

	public int getVeinPower(int i, int j) {
		if (vein == null)
			return 0;
		for (VeinNode n: vein.nodes)
			if (i<=n.x && n.x<i+1 && j<=n.y && n.y<j+1)
				return 1;
		return 0;
	}
}
