package generation;

import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

import world.Block;
import static world.Block.nearInd;
import world.World;
import physics.material.Material;
import physics.material.Substance;
import stereometry.Point;
import generation.morphs.Morph;

public class Generator {

	public ArrayList<Biome> biomes;

	public ArrayList<Morph> morphs;
	public ArrayList<Stratum> stratums;

	public Random rnd;

	public boolean generated = false;

	private static Generator instance = null;

	public static Generator getInstance() {
		if (instance == null) {
			instance = new Generator();
		}
		return instance;
	}

	private Generator() {
		this.biomes = new ArrayList<>();
		this.morphs = new ArrayList<>();
		this.stratums = new ArrayList<>();
		this.rnd = new Random((new Date()).getTime());
	}

	public void generate(){
		World w = World.getInstance();
		biomes.add(new Hills(w.xsize/2, w.ysize/2, 2*w.xsize, 15));
		for (Biome b : biomes){
			b.generate();
			for (Morph m : b.morphs)
				morphs.add(m);
			for (Stratum s : b.stratums)
				stratums.add(s);
		}
		generated = true;
	}

	public void apply(){
		World w = World.getInstance();
		Material m;
		Block b;
//		System.out.print("Total "+stratums.size()+" stratums\n");
		for (int i=0; i<w.xsize; ++i)
			for (int j=0; j<w.ysize; ++j)
				for (int k=0; k<w.zsize; ++k){
					b =  w.blockArray[i][j][k];
					m = getMaterial(b);
					if (m != null)
						b.m = new Substance(getMaterial(b), 1.);
//					System.out.print(m+" ");
				}
		erosion(w, 2000., w.material[Material.MATERIAL_EARTH]);
	}

	public Material getMaterial(Block b) {
		if (!generated)
			generate();
		if (b.z == 0)
			return World.getInstance().material[Material.MATERIAL_BEDROCK];
		Point p = new Point(b);
		int i;
		for (i=0; i<morphs.size(); i++)
			morphs.get(i).preimage(p);
		if (p.z < 1.)
			return World.getInstance().material[Material.MATERIAL_BEDROCK];
		i = 0;
		double z = 1.;
		for (i=0; i < stratums.size(); ++i){
			if (stratums.get(i).isIn(p))
				z += stratums.get(i).w(p);
			if (z > p.z)
				return stratums.get(i).m;
		}
		return null;
	}


	int blockCover(Block b, World w){
		int t = 0;
		for (int i=0; i<nearInd.length; ++i)
			if (w.isIn(b, nearInd[i]))
				t += (w.blockArray[b.x+nearInd[i][0]]
								  [b.y+nearInd[i][1]]
								  [b.z+nearInd[i][2]].m != null) ?
						(1):(0);
			else
				t += (nearInd[i][2]<=0)?(1):(0);
		return t;
	}

	public void erosion(World w, double power, Material erodemat){
		Block b;
		for (int k=0; k<w.zsize; ++k)
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j){
					b = w.blockArray[i][j][k];
					if (b.m != null)
						if ((17-blockCover(b, w))*power > b.m.m.hardness)
							b.m = null;
				}
		for (int k=0; k<w.zsize; ++k)
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j){
					b = w.blockArray[i][j][k];
					if (b.m == null)
						if (blockCover(b, w)>= 17)
							b.setMaterial(erodemat);
				}
	}
}
