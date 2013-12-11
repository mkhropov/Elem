import geomorph.*;
import stereometry.*;

public class BiomePlains extends Biome {

    BiomePlains(){
        this.name = "Plains biome";
    }

    public final void fillWorld(World w){
        Stratum stone = new Stratum(w.xsize, w.ysize, w.zsize/2);
        stone.drop(new MaterialStone(), w, 0, 0);
        Stratum earth = new Stratum(w.xsize, w.ysize, 4);
        earth.drop(new MaterialEarth(), w, 0, 0);
        Front f = new Front(new Point(w.xsize/4, w.ysize/4, w.zsize),
                            new Point(3*w.xsize/4, 3*w.ysize/4, w.zsize),
                            0.d);
        Slipfault morph = new Slipfault(f, 10, 20, 3);
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}
