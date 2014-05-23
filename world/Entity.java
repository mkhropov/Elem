package world;

import graphics.Texture;
import graphics.GraphicalEntity;
import graphics.Model;
import core.Data;
import stereometry.Point;

public class Entity implements GraphicalEntity {
	public Point p;
	public float a; //orientation
	public int mid;
	public int gsid;
	Model model;
	Texture gs;

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
		mid = Data.Models.getId("box");
		gsid = Data.Textures.getId("marble");
	}

	public Entity() {
		mid = Data.Models.getId("box");
		gsid = Data.Textures.getId("stone");
	}

	public void setModel(int mid) {
		this.mid = mid;
		model = Data.Models.get(mid);
	}

	public void setGS(int gsid) {
		this.gsid = gsid;
		gs = Data.Textures.get(gsid);
	}

	public void draw(){
//		m.draw((float)p.x, (float)p.y, (float)p.z, a, gs);
//FIXME
		Data.Models.get(mid).draw(
			(float)p.x, (float)p.y, (float)p.z, a,
			Data.Textures.get(gsid));
	}
}
