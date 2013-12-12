import geomorph.*;
import stereometry.*;

public class BiomePlains extends Biome {

    BiomePlains(){
        this.name = "Plains biome";
    }

    public final void fillWorld(World w){
        Stratum stone = new Stratum(4*w.xsize, 4*w.ysize, w.zsize/2);
        stone.drop(w.material[0], w, -w.xsize, -w.ysize);
        Stratum earth = new Stratum(4*w.xsize, 4*w.ysize, 3);
        earth.drop(w.material[1], w, -w.xsize, -w.ysize);
/*        Front f = new Front(new Point(w.xsize/4, w.ysize/4, w.zsize),
                            new Point(3*w.xsize/4, 3*w.ysize/4, w.zsize),
                            0.d);
        Slipfault morph = new Slipfault(f, 10, 20, 3);*/
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}
