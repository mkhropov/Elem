import java.util.Random;
import physics.material.*;
import java.util.ArrayList;
import stereometry.*;
import geomorph.Morph;

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
        ArrayList<Block> worklist = new ArrayList<Block>();
        ArrayList<Substance> sublist = new ArrayList<Substance>();
        for (int i=0; i<w.xsize; ++i)
            for (int j=0; j<w.ysize; ++j)
                for (int k=0; k<w.zsize; ++k){
                    b = w.blockArray[i][j][k];
                    p = new Point(i, j, k);
          //          if (m.inImage(p)) {
 //                       System.out.printf("%d %d %d\n", i, j, k);
                        worklist.add(b);
                        q = m.preimage(p);
                        if (q.x < 0.d) q.x = 1.d; if (q.x>w.xsize) q.x = w.xsize-2;
                        if (q.y < 0.d) q.y = 1.d; if (q.y>w.ysize) q.y = w.ysize-2;
                        if (q.z < 0.d) q.z = 1.d; if (q.z>w.zsize) q.z = w.zsize-2;
                        s = w.blockArray[(int)q.x][(int)q.y][(int)q.z].m;
                        if (s != null)
                            sublist.add(new Substance(s));
                        else
                            sublist.add(null);
//                    }
                }
        for (int i=0; i<worklist.size(); ++i)
            worklist.get(i).m = sublist.get(i);
    }
}
