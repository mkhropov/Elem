package world;

import stereometry.Point;

public class Entity {
	public Point p;
	public float a; //orientation
	public int mid;
	public int gsid;

	public Entity(Block b, int mid, int gsid) {
		this.p = new Point(b);
		this.mid = mid;
		this.gsid = gsid;
		this.a = 0.f;
	}

	public Entity(Block b) {
		p = new Point(b);
		mid = graphics.ModelList.getInstance().findId("box");
		gsid = graphics.GSList.getInstance().findId("marble");
	}

	public Entity() {
		mid = graphics.ModelList.getInstance().findId("box");
		gsid = graphics.GSList.getInstance().findId("stone");
	}
}
