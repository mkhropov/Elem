package generation;

import java.util.ArrayList;

import stereometry.Point;
import generation.morphs.Morph;
import utils.Random;

public class Vein {

	public String name;

	public static double SPLIT_CHANCE = .05;

	public ArrayList<VeinNode> nodes;
	public double maxL;

	public Vein (Point p, double phi, double l){
		nodes = new ArrayList<>(0);
		maxL = l;
		VeinNode n = new VeinNode(p, phi);
		nodes.add(n);
	}

	public void grow(Stratum s){
		VeinNode n = nodes.get(0);
		nodes.clear();
		nodes.add(n);
		grow(s, n);
	}

	public void morph (ArrayList<Morph> morphs){
		for (int i=0; i<nodes.size(); ++i)
			for (int j=0; j<morphs.size(); ++j)
				morphs.get(j).preimage(nodes.get(i));
	}

// CAREFUL recursion incoming
	private void addNode(Stratum s, VeinNode n) {
		if (s.isIn(n) && (n.l<maxL)){
			nodes.add(n);
			grow(s, n);
		}
	}

	private void grow(Stratum s, VeinNode n){
		double phi = n.phi1 - n.phi0;
		if ((Random.getInstance().nextDouble() < SPLIT_CHANCE) &&
			(phi > Math.PI/4.)                              ){
			VeinNode rn = new VeinNode(n, true);
			VeinNode ln = new VeinNode(n, false);
			addNode(s, rn); addNode(s, ln);
		} else {
			phi *= Random.getInstance().nextDouble();
			phi += n.phi0;
			VeinNode nn = new VeinNode(n, phi);
			addNode(s, nn);
		}
	}
}
