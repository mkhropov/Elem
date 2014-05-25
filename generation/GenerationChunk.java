package generation;

import generation.morphs.Morph;
import java.util.ArrayList;
import stereometry.BoundBox;
import world.World;

public class GenerationChunk {

	public static final int GEN_CHUNK_XSIZE = 16;
	public static final int GEN_CHUNK_YSIZE = 16;

	public ArrayList<Morph> morphs;
	public ArrayList<Stratum> stratums;

	public int x, y;
	
	public double V;

	public BoundBox bb;

	public GenerationChunk(int x, int y){
		World w = World.getInstance();
		assert((x>=0)&&(x<w.xsize));
		assert((y>=0)&&(x<w.ysize));

		this.x = x; this.y = y;
		this.bb = new BoundBox(x, y, 0,
							   x+GEN_CHUNK_XSIZE,
							   y+GEN_CHUNK_YSIZE,
							   w.zsize);
		this.morphs = new ArrayList<>(0);
		this.stratums = new ArrayList<>(0);
		this.V = 0;
	}

	public void addStratum(Stratum s){
		if (s.bb.intersects(bb)) {
			stratums.add(s);
			// FIXME we should add only the volume that is inside chunk
			V += s.width*Math.PI*(s.rmax+s.rmin)*(s.rmax+s.rmin)/4;
		}
	}

	public void addMorph(Morph m){
		if (m.bb.intersects(bb))
			morphs.add(m);
	}
}
