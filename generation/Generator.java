package generation;

import core.Data;
import generation.morphs.Morph;
import java.util.ArrayList;
import utils.Random;
import stereometry.Point;
import static world.Block.nearInd;
import world.World;
import item.Item;
import graphics.Renderer;

public class Generator {

	public ArrayList<Biome> biomes;

	public ArrayList<Morph> morphs;
	public ArrayList<Stratum> stratums;

	public int x;
	public int y;
	public GenerationChunk[][]	genChunks;

	public static boolean generated = false;

	Stratum bedrock, air, erode; //virtual stratums

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
		this.x = World.getInstance().xsize/GenerationChunk.GEN_CHUNK_XSIZE + 1;
		this.y = World.getInstance().ysize/GenerationChunk.GEN_CHUNK_YSIZE + 1;
		this.genChunks = new GenerationChunk[x][y];
		for (int i=0; i<x; ++i)
			for (int j=0; j<y; ++j)
				genChunks[i][j] = new GenerationChunk(
					i*GenerationChunk.GEN_CHUNK_XSIZE,
					j*GenerationChunk.GEN_CHUNK_YSIZE);
	}

	public void addStratum(Stratum s) {
		stratums.add(s);
		for (GenerationChunk[] gca : genChunks)
			for (GenerationChunk gc : gca)
				gc.addStratum(s);
	}
	
	public void addMorph(Morph m){
		morphs.add(m);
		for (GenerationChunk[] gca : genChunks)
			for (GenerationChunk gc : gca)
				gc.addMorph(m);
	}
	
	public void generate(){
		World w = World.getInstance();
		GenerationChunk gc;
		Biome b;
		Stratum s;
		Morph m;
		Random rnd = Random.getInstance();
		int X = GenerationChunk.GEN_CHUNK_XSIZE;
		int Y = GenerationChunk.GEN_CHUNK_YSIZE;
		System.out.print("Generating biomes ...");
		this.bedrock = new Stratum(0, 0, 0, 0, 0, Data.Materials.getId("bedrock"));
		this.air = new Stratum(0, 0, 0, 0, 0, Data.Materials.getId("air"));

		biomes.add(Data.Biomes.get("hills"));
		for (int u = 0; u < x; ++u) {
			for (int v = 0; v < y; ++v) {
				gc = genChunks[u][v];
				/*
				 *FIXME needs weighted approach
				 *	this is but a dummy code
				 */
				b = biomes.get(0);
				double tV = X * Y * (b.height - b.erodeWidth);
				while ((gc.V < tV) && ((tV - gc.V) > 0.2 * tV)) {
					s = b.genStratum(gc.x + rnd.nextInt(X),
					                 gc.y + rnd.nextInt(Y));
					addStratum(s);
					gc.V += s.width*Math.PI*s.rmin*s.rmax;
				}
				double morphsNum = X * Y * b.morphDensity;
				morphsNum = Math.floor(morphsNum)
						+ ((rnd.nextDouble() > morphsNum - Math.floor(morphsNum)) ? 0 : 1);
				while (morphsNum-- > 0) {
					addMorph(b.genMorph(gc.x + rnd.nextInt(X),
										gc.y + rnd.nextInt(Y),
										b.height));
				}
			}
		}
		System.out.print(" "+biomes.size()+" biomes, "+morphs.size()+" morphs, "+
						stratums.size()+" stratums\n");
		generated = true;
		this.erode = new Stratum(0, 0, 0, 0, 0,
			Data.Materials.getId(biomes.get(0).erodeMat));
	}

	public void apply(){
		World w = World.getInstance();
		GenerationChunk gc;
		Stratum s;
		System.out.print("Filling blocks ");
		for (int u=0; u<x; ++u)
		for (int v=0; v<y; ++v){
			gc = genChunks[u][v];
			for (int i=gc.x; i<gc.x+GenerationChunk.GEN_CHUNK_XSIZE; ++i)
//			if (i%(w.xsize/30) == 0)
//				System.out.print(".");
			for (int j=gc.y; j<gc.y+GenerationChunk.GEN_CHUNK_YSIZE; ++j){
				if (i>=w.xsize || j>=w.ysize)
					continue;
				for (int k=0; k<w.zsize; ++k) {
					s = getStratum(i, j, k, gc);
					w.setMaterialID(i, j, k, s.m);
					if (s.getVeinPower(i, j) > 0) {
/*						Item node = new Item(i, j, k, Data.Items.get("raw emerald"));
						node.mid = Data.Models.getId("gem patch");
						node.gsid = Data.Textures.getId("elem");
						Renderer.getInstance().addEntity(node);
						w.item.add(node);*/
					}
				}
			}
		}
		System.out.print(" done\n");
		Biome b = biomes.get(0);
		erosion(w, b.erodeStrength, Data.Materials.getId(b.erodeMat));
	}

	public Stratum getStratum(int x, int y, int z, GenerationChunk gc) {
		if (!generated)
			generate();
		if (z == 0)
			return bedrock;
		Point p = new Point(x, y, z);
		int i;
		for (i=0; i<gc.morphs.size(); i++)
			gc.morphs.get(i).preimage(p);
		if (p.z < 1.)
			return bedrock;
		double dz = 1.;
		for (i=0; i < gc.stratums.size(); ++i){
			if (gc.stratums.get(i).isIn(p))
				dz += gc.stratums.get(i).w(p);
			if (dz > p.z)
				return gc.stratums.get(i);
		}
		if (p.z < dz+biomes.get(0).erodeWidth)
			return erode;
		return air;
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
						Data.Materials.get(w.getMaterialID(i,j,k)).hardness)
						w.setMaterialID(i,j,k, Data.Materials.getId("air"));
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
						w.setMaterialID(i, j, k, erodemat);
		}
		System.out.print(" done\n");
	}
}
