package generation;

import java.util.Random;
import physics.Temperature;
import physics.material.*;
import java.util.ArrayList;
import stereometry.*;
import world.*;
import static world.Block.nearInd;
import generation.morphs.Morph;

public abstract class Biome {

    public String name;
    public Random gen;

    Biome(){
        name = "abstract biome";
        gen = new Random();
    }

    public abstract void fillWorld(World w);

    Temperature getTemperature(){
        return new Temperature(0);
    }

    public void applyMorph(Morph m, World w){
        System.out.println("In applyMorph");
        Point p, q;
        Block b;
        Substance s;
        ArrayList<Block> worklist = new ArrayList<>();
        ArrayList<Substance> sublist = new ArrayList<>();
        for (int i=0; i<w.xsize; ++i)
            for (int j=0; j<w.ysize; ++j)
                for (int k=0; k<w.zsize; ++k){
                    b = w.blockArray[i][j][k];
                    p = new Point(i, j, k);
          //          if (m.inImage(p)) {
 //                       System.out.printf("%d %d %d\n", i, j, k);
                    worklist.add(b);
                    q = m.preimage(p);
                    if (q.x < 0.d) q.x = 1.d; if (q.x>w.xsize-1) q.x = w.xsize-2;
                    if (q.y < 0.d) q.y = 1.d; if (q.y>w.ysize-1) q.y = w.ysize-2;
                    if (q.z < 0.d) q.z = 1.d; if (q.z>w.zsize-1) q.z = w.zsize-2;
                    s = w.blockArray[(int)q.x][(int)q.y][(int)q.z].m;
                    if (s != null)
                        sublist.add(new Substance(s));
                    else
                        sublist.add(null);
                }
        for (int i=0; i<worklist.size(); ++i)
            worklist.get(i).m = sublist.get(i);
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
