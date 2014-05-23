package generation;

import stereometry.Point;

/* a simple node of a mineral vein
 * It's inherently 2d - all veins are generated
 * in a horizontal startum, and bent only later,
 * with geomorphs
 */

public class VeinNode extends Point {

	public static double step = .4;

	public double phi0; // angles describing the general vein
	public double phi1; // direction in this node

	public double l; // length from root to this node

	public VeinNode(Point p, double phi){
		super(p);
		this.phi0 = phi - 5*Math.PI/11;
		this.phi1 = phi + 5*Math.PI/11;
		this.l = 0;
	}

	public VeinNode(VeinNode parent, double phi){
		super(parent);
		this.x = parent.x + step*Math.cos(phi);
		this.y = parent.y + step*Math.sin(phi);
		this.z = parent.z;
		this.phi0 = parent.phi0;
		this.phi1 = parent.phi1;
		this.l = parent.l + step;
	}

// a special constructor for splitting the vein
	public VeinNode(VeinNode parent, boolean right){
		super(parent);
		double m_phi = (parent.phi0 + parent.phi1)/2.;
		if (right) {
			this.phi0 = parent.phi0;
			this.phi1 = m_phi;
			this.x = parent.x + step*Math.cos(this.phi0);
			this.y = parent.y + step*Math.sin(this.phi0);
		} else {
			this.phi0 = m_phi;
			this.phi1 = parent.phi1;
			this.x = parent.x + step*Math.cos(this.phi1);
			this.y = parent.y + step*Math.sin(this.phi1);
		}
		this.z = parent.z;
		this.l = parent.l + step;
	}
}

