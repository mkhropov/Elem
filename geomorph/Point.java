class Point {
	double x;
	double y;
	double z;

	Point(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	Point(double x, double y, double z){
		this.x = x;
        this.y = y;
        this.z = z;
    }

	Point(Point p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	Vector vecTo(Point p) {
		return new Vector(this, p);
	}
}
