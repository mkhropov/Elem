import Math.*;

public class Front extends Vector {

	double a; //angle of front plane, right-side screw from horizontal

	Front(Point p1, Point p2, double a){
		super(p1, new Point(p2.x, p2.y, p1.z));
		// because it should be in a horizontal plane!
		this.a = a;
	}

    public Plane plane(){
        Vector hv = new Vector(this.p1, new Point(this.p1.y, this.p1.x, this.p1.z));
        Vector vv = new Vector(this.p1, new Point(this.p1.x, this.p1.y, this.p1.x-1.d));
        hv.normalize(); vv.normalize();
        double sa = Math.sin(a); double ca = Math.cos(a);
        Vector av = new Vector(this.p1, sa*hv.x+ca*vv.x, sa*hv.y+ca*vv.y, sa*hb.z+ca*vv.z);
        return new Plane(this, av);
    }
     
	public boolean isUp(Point p){
		return this.plane().isUp(p);
	}

    public boolean isInside(point P){
    //is >= a bad style when using double?
        return ((this.dot(new Vector(this.p1, P) >= 0.d) &&
                (this.dot(new Vector(this.p2, P) <= 0.d));
    }
}

