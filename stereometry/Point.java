package stereometry;

import world.Block;

public class Point {
    public double x;
    public double y;
    public double z;
    public double w;

    public Point(int x, int y, int z){
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
        this.w = 1.d;
    }

    public Point(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.d;
    }

    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.w = p.w;
    }

    public Point(Point p, Vector v){
        this(p.x+v.x, p.y+v.y, p.z+v.z);
    }

    public Point(Block b){
        this(b.x, b.y, b.z);
    }

    public void add(Vector v, double alpha){
        x += alpha*v.x; y += alpha*v.y; z += alpha*v.z;
    }

    public double dist(Point p){
        return Math.sqrt((x-p.x)*(x-p.x)+ (y-p.y)*(y-p.y)+(z-p.z)*(z-p.z));
    }

	public double distProj(Point p){
        return Math.sqrt((x-p.x)*(x-p.x)+ (y-p.y)*(y-p.y));
    }

	public boolean sameBlock(Point p) {
		return ((Math.floor(p.x) == Math.floor(x)) &&
				(Math.floor(p.y) == Math.floor(y)) &&
				(Math.floor(p.z) == Math.floor(z)));
	}
}
