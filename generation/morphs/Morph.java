package generation.morphs;

import stereometry.BoundBox;
import stereometry.Point;

abstract public class Morph {

	public BoundBox bb;

    public abstract boolean inImage(Point p);

    public abstract void preimage(Point p);

	public boolean isIn(Point p){
		return bb.isIn(p);
	}
}
