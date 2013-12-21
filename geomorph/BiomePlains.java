package geomorph;

import java.util.Date;
import java.util.Random;
import world.*;
import stereometry.*;
import physics.Temperature;

public class BiomePlains extends Biome {

    public BiomePlains(){
        this.name = "Plains biome";
    }

	@Override
    public final void fillWorld(World w){
		Stratum s;
		int i, k;
		gen = new Random((new Date()).getTime());
		s = new Stratum(4*w.xsize, 4*w.ysize, 1);
		s.drop(w.material[3], w, -w.xsize, -w.ysize);
		for (i=0; i<2*w.zsize/3;){
			k = gen.nextInt(2)+1; i+=k;
            s = new Stratum(4*w.xsize, 4*w.ysize, k);
            s.drop(w.material[gen.nextInt(2)*4], w,
                    -w.xsize,
                    -w.ysize);
        }
		s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(w.material[1], w, -w.xsize, -w.ysize);

        Front f = new Front(new Point(3*w.xsize/4, w.ysize/4, w.zsize),
                            new Point(w.xsize/4, 3*w.ysize/4, w.zsize),
                            -0.3d);
		Slipfault morph = new Slipfault(f, 20, 30, 4);
		applyMorph(morph, w);
		Point p = new Point(w.xsize/2, w.ysize/2, w.zsize);
		Pointfold pmorph = new Pointfold(p, 10, 30, -4);
		applyMorph(pmorph, w);
		f = new Front(new Point(0, 0, w.zsize),
                            new Point(w.xsize, w.ysize, w.zsize),
                            0.d);
        morph = new Slipfault(f, 20, 30, 3);
        applyMorph(morph, w);
    }

	@Override
    Temperature getTemperature(){
        return new Temperature(300);
    }
}
