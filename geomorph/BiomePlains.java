package geomorph;

import java.util.Date;
import java.util.Random;
import world.*;
import stereometry.*;
import physics.Temperature;
import physics.material.Material;

public class BiomePlains extends Biome {

    public BiomePlains(){
        this.name = "Plains biome";
    }

	@Override
    public final void fillWorld(World w){
		Stratum s;
		int i, k;
		Material m;
		gen = new Random((new Date()).getTime());
		s = new Stratum(4*w.xsize, 4*w.ysize, 1);
		s.drop(w.material[3], w, -w.xsize, -w.ysize);
		for (i=0; i<w.zsize/2;){
			k = gen.nextInt(2)+1; i+=k;
            s = new Stratum(4*w.xsize, 4*w.ysize, k);
			m = w.material[(gen.nextInt(3)+4) % 6];
            s.drop(m, w, -w.xsize,- w.ysize);
        }
		Front f; Point p;
		f = new Front(new Point(w.xsize/2, 0, w.zsize),
                            new Point(w.xsize/2, w.ysize, w.zsize),
                            0.d);
		Wavefold wmorph = new Wavefold(f, 15, 30, -3);
		applyMorph(wmorph, w);
		f = new Front(new Point(3*w.xsize/4, w.ysize/4, w.zsize),
                            new Point(w.xsize/4, 3*w.ysize/4, w.zsize),
                            0.d);
		Linefold lmorph = new Linefold(f, 15, 30, -3);
		applyMorph(lmorph, w);
        f = new Front(new Point(3*w.xsize/4, w.ysize/4, w.zsize),
                            new Point(w.xsize/4, 3*w.ysize/4, w.zsize),
                            -0.3d);
		Slipfault smorph = new Slipfault(f, 20, 30, 4);
		applyMorph(smorph, w);
		p = new Point(w.xsize/2, w.ysize/2, w.zsize);
		Pointfold pmorph = new Pointfold(p, 10, 30, -4);
		applyMorph(pmorph, w);
	/*	f = new Front(new Point(0, 0, w.zsize),
                            new Point(w.xsize, w.ysize, w.zsize),
                            0.d);
        smorph = new Slipfault(f, 20, 30, 3);
        applyMorph(smorph, w);*/
		erosion(w, 2000., w.material[1]);
		s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(w.material[1], w, -w.xsize, -w.ysize);
    }

	@Override
    Temperature getTemperature(){
        return new Temperature(300);
    }
}
