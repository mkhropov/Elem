public class Plane {
	double a;
	double b;
	double c;

	Point p; //any point belonging to the plane

	Plane(Point p, double a, double b, double c){
		this.p = new Point(p);
		this.a = a;
		this.b = b;
		this.c = c;
	}

	Plane(Plane pl){
		this.p = new Point(pl.p);
		this.a = pl.a;
		this.b = pl.b;
		this.c = pl.c;
	}

	Plane(Point p, Vector v){
		this.p = new Point(p);
		this.a = v.x;
		this.b = v.y;
		this.c = v.z;
	}

	Plane(Vector v1, Vector v2){ //v1 is considered primary
		this(v1.p1, v1.cross(v2));
	}

	double calcP(Point p){
		return (a * (p.x - this.p.x) +
                b * (p.y - this.p.y) +
                c * (p.z - this.p.z));
	}

	public boolean isUp(Point p){
		Point u = new Point(this.p); u.z += 1.d;
		double cu = calcP(u);
		double cp = calcP(p);
		return (cu*cp > 0);
	}
}
