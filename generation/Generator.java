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

	public int x;
	public int y;
	public GenerationChunk[][]	genChunks;

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
		this.x = World.getInstance().xsize/GenerationChunk.GEN_CHUNK_XSIZE + 1;
		this.y = World.getInstance().ysize/GenerationChunk.GEN_CHUNK_YSIZE + 1;
		this.genChunks = new GenerationChunk[x][y];
		for (int i=0; i<x; ++i)
			for (int j=0; j<y; ++j)
				genChunks[i][j] = new GenerationChunk(
					i*GenerationChunk.GEN_CHUNK_XSIZE,
					j*GenerationChunk.GEN_CHUNK_YSIZE);
	}

	public void generate(){
		World w = World.getInstance();
		System.out.print("Generating biomes ...");
		biomes.add(new Hills(w.xsize/2, w.ysize/2, 2*w.xsize, 15));
		for (Biome b : biomes){
			b.generate();

			for (Morph m : b.morphs){
				morphs.add(m);
				for (GenerationChunk[] gca : genChunks)
					for (GenerationChunk gc : gca)
						gc.addMorph(m);
			}
			for (Stratum s : b.stratums){
				stratums.add(s);
				for (GenerationChunk[] gca : genChunks)
					for (GenerationChunk gc : gca)
						gc.addStratum(s);
			}
		}
		System.out.print(" "+biomes.size()+" biomes, "+morphs.size()+" morphs, "+
						stratums.size()+" stratums\n");
		generated = true;
	}

	public void apply(){
		World w = World.getInstance();
		GenerationChunk gc;
		Material m;
		Block b;
		System.out.print("Filling blocks ");
		for (int t=0; t<x; ++t)
		for (int s=0; s<y; ++s){
			gc = genChunks[t][s];
			for (int i=gc.x; i<gc.x+GenerationChunk.GEN_CHUNK_XSIZE; ++i)
//			if (i%(w.xsize/30) == 0)
//				System.out.print(".");
			for (int j=gc.y; j<gc.y+GenerationChunk.GEN_CHUNK_YSIZE; ++j){
				if (i>=w.xsize || j>=w.ysize)
					continue;
				for (int k=0; k<w.zsize; ++k){
					b =  w.blockArray[i][j][k];
					m = getMaterial(b, gc);
					if (m != null)
						b.m = new Substance(m, 1.);
//				System.out.print(m+" ");
				}
			}
		}
		System.out.print(" done\n");
		erosion(w, 2000., w.material[Material.MATERIAL_EARTH]);
	}

	public Material getMaterial(Block b, GenerationChunk gc) {
		if (!generated)
			generate();
		if (b.z == 0)
			return World.getInstance().material[Material.MATERIAL_BEDROCK];
		Point p = new Point(b);
		int i;
		for (i=0; i<gc.morphs.size(); i++)
			gc.morphs.get(i).preimage(p);
		if (p.z < 1.)
			return World.getInstance().material[Material.MATERIAL_BEDROCK];
		i = 0;
		double z = 1.;
		for (i=0; i < gc.stratums.size(); ++i){
			if (gc.stratums.get(i).isIn(p))
				z += gc.stratums.get(i).w(p);
			if (z > p.z)
				return gc.stratums.get(i).m;
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
		System.out.print("Applying erosion ");
		for (int k=0; k<w.zsize; ++k){
			if (k%(w.xsize/30) == 0)
				System.out.print(".");
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j){
					b = w.blockArray[i][j][k];
					if (b.m != null)
						if ((17-blockCover(b, w))*power > b.m.m.hardness)
							b.m = null;
				}
		}
		System.out.print(" done\nFilling gaps ");
		for (int k=0; k<w.zsize; ++k){
			if (k%(w.xsize/30) == 0)
				System.out.print(".");
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j){
					b = w.blockArray[i][j][k];
					if (b.m == null)
						if (blockCover(b, w)>= 17)
							b.setMaterial(erodemat);
				}
		}
		System.out.print(" done\n");
	}
}
