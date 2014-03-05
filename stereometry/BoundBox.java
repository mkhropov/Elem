package stereometry;

public class BoundBox {
	public Point p1;
	public Point p2;

	public BoundBox(Point p1, Point p2){
		this.p1 = new Point(Math.min(p1.x, p2.x),
							Math.min(p1.y, p2.y),
							Math.min(p1.z, p2.z));
		this.p2 = new Point(Math.max(p1.x, p2.x),
							Math.max(p1.y, p2.y),
							Math.max(p1.z, p2.z));
	}

	public BoundBox(double x1, double y1, double z1, double x2, double y2, double z2){
		this(new Point(x1, y1, z1), new Point(x2, y2, z2));
	}

	public BoundBox intersect(BoundBox t){
		Point a = new Point(Math.max(this.p1.x, t.p1.x),
							Math.max(this.p1.y, t.p1.y),
							Math.max(this.p1.z, t.p1.z));
		Point b = new Point(Math.min(this.p2.x, t.p2.x),
							Math.min(this.p2.y, t.p2.y),
							Math.min(this.p2.z, t.p2.z));
		if (a.x<b.x && a.y<b.y && a.z<b.z)
			return new BoundBox(a, b);
		else
			return new BoundBox(a, a);
	}

	public boolean isIn(Point p){
		return ((p.x>=p1.x) && (p.y>=p1.y) && (p.z>=p1.z) &&
				(p.x<=p2.x) && (p.y<=p2.y) && (p.z<=p2.z));
	}
}
