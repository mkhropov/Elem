package generation;

import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

import world.Block;
import static world.Block.nearInd;
import world.World;
import physics.Material;
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
				for (int k=0; k<w.zsize; ++k)
					w.setMaterial(i,j,k,getMaterial(i, j, k, gc));
			}
		}
		System.out.print(" done\n");
		erosion(w, 2000., Material.MATERIAL_EARTH);
	}

	public int getMaterial(int x, int y, int z, GenerationChunk gc) {
		if (!generated)
			generate();
		if (z == 0)
			return Material.MATERIAL_BEDROCK;
		Point p = new Point(x, y, z);
		int i;
		for (i=0; i<gc.morphs.size(); i++)
			gc.morphs.get(i).preimage(p);
		if (p.z < 1.)
			return Material.MATERIAL_BEDROCK;
		double dz = 1.;
		for (i=0; i < gc.stratums.size(); ++i){
			if (gc.stratums.get(i).isIn(p))
				dz += gc.stratums.get(i).w(p);
			if (dz > p.z)
				return gc.stratums.get(i).m;
		}
		return Material.MATERIAL_NONE;
	}


	int blockCover(int x, int y, int z, World w){
		int t = 0;
		for (int i=0; i<nearInd.length; ++i)
			if (w.isIn(x, y, z, nearInd[i]))
				t += (w.isAir(x+nearInd[i][0], 
						 y+nearInd[i][1],
						 z+nearInd[i][2]))?
						(0):(1);
			else
				t += (nearInd[i][2]<=0)?(1):(0);
		return t;
	}

	public void erosion(World w, double power, int erodemat){
		System.out.print("Applying erosion ");
		for (int k=0; k<w.zsize; ++k){
//			if (k%(w.xsize/30) == 0)
//				System.out.print(".");
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j){
					if ((17-blockCover(i, j, k, w))*power >
						Material.hardness[w.getMaterialID(i,j,k)])
						w.setMaterial(i,j,k, Material.MATERIAL_NONE);
				}
		}
		System.out.print(" done\nFilling gaps ");
		for (int k=0; k<w.zsize; ++k){
//			if (k%(w.xsize/30) == 0)
//				System.out.print(".");
			for (int i=0; i<w.xsize; ++i)
				for (int j=0; j<w.ysize; ++j)
					if ((w.isAir(i, j, k)) &&
							(blockCover(i, j, k, w)>= 17))
						w.setMaterial(i, j, k, erodemat);
		}
		System.out.print(" done\n");
	}
}
