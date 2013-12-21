package geomorph;

import world.*;
import stereometry.*;
import physics.Temperature;
import java.util.Random;
import java.util.Date;

public class BiomeRough extends Biome {

    public BiomeRough(){
        this.name = "Rough terrain biome";
    }

	@Override
    public final void fillWorld(World w){
        gen = new Random((new Date()).getTime());
        Stratum s;
		s = new Stratum(4*w.xsize, 4*w.ysize, 1);
		s.drop(w.material[3], w, -w.xsize, -w.ysize);
        s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(w.material[0], w, -w.xsize, -w.ysize);
        for (int i=0; i<2*w.zsize/3; ++i){
            s = new Stratum(w.xsize/2, w.ysize/2, 1);
            s.drop(w.material[0], w,
                    gen.nextInt(3*w.xsize/2)-w.xsize/2,
                    gen.nextInt(3*w.ysize/2)-w.ysize/2);
        }
        s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(w.material[1], w, -w.xsize, -w.ysize);
        for (int i=0; i<w.zsize/2; ++i){
            s = new Stratum(w.xsize/2, w.ysize/2, 1);
            s.drop(w.material[1], w,
                    gen.nextInt(3*w.xsize/2)-w.xsize/2,
                    gen.nextInt(3*w.ysize/2)-w.ysize/2);
        }
   }

	@Override
    Temperature getTemperature(){
        return new Temperature(300);
    }
}
