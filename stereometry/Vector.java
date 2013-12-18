package stereometry;

//import Math.*;

public class Vector {
// NOTE: it's an oriented vector in 3D space
    public Point p1;
    public Point p2;
    public double x;
    public double y;
    public double z;

    public Vector(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        this.x = p2.x-p1.x;
        this.y = p2.y-p1.y;
        this.z = p2.z-p1.z;
    }

    public Vector(double x, double y, double z){
        this.p1 = new Point(0.d, 0.d, 0.d);
        this.p2 = new Point(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Point p, double x, double y, double z){
      this.p1 = new Point(p);
      this.p2 = new Point(p.x+x, p.y+y, p.x+z);
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Vector(Point p, Vector v){
        this(p, v.x, v.y, v.z);
    }

    public double len(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public void scale(double a){
        x *= a;
        y *= a;
        z *= a;
        p2 = new Point(p1, this);
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
        return (new Vector(p, p1)).cross(this).len()/len();
    }

//distance from front to projection of P on horizontal plane
    public double distProj(Point p) {
        Point t = p; t.z = p1.z;
        return dist(t);
    }

    public void normalize(){
        double a = this.len();
        x /= a;
        y /= a;
        z /= a;
        p2 = new Point(p1.x+x, p1.y+y, p1.z+z);
    }
    
    public boolean zero(){
        return (x==0.) && (y==0.) && (z==0.);
    }
    
    public void toZero(){
        x = 0.;
        y = 0.;
        z = 0.;
    }
	
	public void add(Vector v, double a){
		this.x += v.x*a;
		this.y += v.y*a;
		this.z += v.z*a;
	}
}

