package world;

import stereometry.Point;

public class Entity {
	public Point p;
	public int mid;
	public int gsid;

	public Entity(Block b, int mid, int gsid) {
		p = new Point(b);
		this.mid = mid;
		this.gsid = gsid;
	}

	public Entity(Block b) {
		p = new Point(b);
		mid = graphics.ModelList.getInstance().findId("void");
		gsid = graphics.GSList.getInstance().findId("void");
	}

	public Entity() {
		mid = graphics.ModelList.getInstance().findId("void");
		gsid = graphics.GSList.getInstance().findId("void");
	}
}
