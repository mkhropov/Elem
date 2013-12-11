package geomorph;

public class Point {
    double x;
    double y;
    double z;
    double w;

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
}
