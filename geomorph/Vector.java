//import Math.*;

public class Vector {
// NOTE: it's an oriented vector in 3D space
	Point p1;
	Point p2;
	double x;
	double y;
	double z;

	Vector(Point p1, Point p2){
		this.p1 = p1;
		this.p2 = p2;
		x = p2.x-p1.x;
		y = p2.y-p1.y;
		z = p2.z-p1.z;
	}

	Vector(double x, double y, double z){
		p1 = new Point(0.d, 0.d, 0.d);
		p2 = new Point(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	Vector(Point p, double x, double y, double z){
        p1 = new Point(p);
        p2 = new Point(p.x+x, p.y+y, p.x+z);
        this.x = x;
        this.y = y;
        this.z = z;
    }


	public double len(){
		return Math.sqrt(x*x + y*y + z*z);
	}

	public double dot(Vector v){
		return (x*v.x + y*v.y + z*v.z);
	}

	public Vector cross(Vector v){
		return new Vector(y*v.z - z*v.y,
						  z*v.x - x*v.z,
						  z*v.y - y*v.z);
	}

//distance from front to P
	public double dist(Point p){
		return (new Vector(p, this.p1)).cross(this).len()/this.len();
	}

//distance from front to projection of P on horizontal plane
	public double distProj(Point p) {
		Point t = p; t.z = this.p1.z;
		return dist(t);
	}
}

