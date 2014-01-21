package geomorph;

import stereometry.*;

abstract public class Morph {
	
	public BoundBox bb;

    public abstract boolean inImage(Point p);

    public abstract Point preimage(Point p);
  //  public abstract Point image(Point P);
	public BoundBox affected(){
		return bb;
	}
}
