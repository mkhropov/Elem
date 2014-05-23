package physics;

import utils.Initializable;
import utils.Named;

public class Material implements Initializable, Named {
	String name;
	public String texture;
	public float hardness;
	public int[] weight;
	public int[][] support;

	public static final int BLOCK = 0;
	public static final int FLOOR = 1;
	public static final int UP = 0;
	public static final int SIDE = 1;
	public static final int DOWN = 2;

	
	public Material(){
		weight = new int[2];
		weight[BLOCK] = 100;
		weight[FLOOR] = 60;
		support = new int[2][3];
		support[BLOCK][UP] = 150;
		support[FLOOR][UP] = 0;
		support[BLOCK][SIDE] = 35;
		support[FLOOR][SIDE] = 26;
		support[BLOCK][DOWN] = 15;
		support[FLOOR][DOWN] = 5;
	}

	@Override
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public void initialize(){
	}
	
	public double digTime(double str) {
		if (str<=hardness)
			return -1.;
		else
			return (400./(str-hardness));
	}
}
