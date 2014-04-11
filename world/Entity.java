package world;

import stereometry.Point;
import graphics.*;

public class Entity implements GraphicalEntity {
	public Point p;
	public float a; //orientation
	public int mid;
	public int gsid;
	Model model;
	GraphicalSurface gs;

	public Entity(Block b, int mid, int gsid) {
		this.p = new Point(b);
		this.mid = mid;
		this.gsid = gsid;
		this.a = 0.f;
	}

	public void setP(Block b){
		this.p = new Point(b);
	}

	public Point getP(){
		return p;
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

	public void setModel(int mid) {
		this.mid = mid;
		model = ModelList.getInstance().get(mid);
	}

	public void setGS(int gsid) {
		this.gsid = gsid;
		gs = GSList.getInstance().get(gsid);
	}

	public void draw(){
//		m.draw((float)p.x, (float)p.y, (float)p.z, a, gs);
//FIXME
		ModelList.getInstance().get(mid).draw(
			(float)p.x, (float)p.y, (float)p.z, a,
			GSList.getInstance().get(gsid));
	}
}
