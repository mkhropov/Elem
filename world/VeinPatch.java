package world;

import java.util.ArrayList;
import utils.Named;
import utils.Initializable;
import core.Data;
import stereometry.Point;
import graphics.Model;
import graphics.Texture;

public class VeinPatch implements Named, Initializable {

	public String name;
	public String model;
	public String texture;
	public String drop;

	private ArrayList<Point> patches;
	private Model m;
	private Texture t;

	public VeinPatch(){
		this.patches = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void initialize() {
		m = Data.Models.get(model);
		t = Data.Textures.get(texture);
	}

	public void addPatch(Point p) {
		patches.add(p);
	}

	public boolean removePatch(Point p) {
		boolean res = false;
		ArrayList<Point> toRemove = new ArrayList<>();
		for (Point t: patches)
			if (t.sameBlock(p))
				toRemove.add(t);
		patches.removeAll(toRemove);
		return (toRemove.size()>0);
	}

	public void draw(float high, float low) {
		for (Point p: patches) {
			if (p.z<= high && p.z>=low)
				m.draw((float)p.x, (float)p.y, (float)p.z, 0.f, t);
		}
	}
}
