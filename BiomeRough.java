import geomorph.*;
import stereometry.*;
import java.util.Random;
import java.util.Date;

public class BiomeRough extends Biome {

    BiomeRough(){
        this.name = "Rough terrain biome";
    }

    public final void fillWorld(World w){
        Random gen = new Random((new Date()).getTime());
        Stratum s;
        s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(new MaterialStone(), w, -w.xsize, -w.ysize);
        for (int i=0; i<2*w.xsize/3; ++i){
            s = new Stratum(w.xsize/2, w.ysize/2, 1);
            s.drop(new MaterialStone(), w,
                    gen.nextInt(3*w.xsize/2)-w.xsize/2,
                    gen.nextInt(3*w.ysize/2)-w.ysize/2);
        }
        s = new Stratum(4*w.xsize, 4*w.ysize, 2);
        s.drop(new MaterialEarth(), w, -w.xsize, -w.ysize);
        for (int i=0; i<w.xsize/2; ++i){
            s = new Stratum(w.xsize/2, w.ysize/2, 1);
            s.drop(new MaterialEarth(), w,
                    gen.nextInt(3*w.xsize/2)-w.xsize/2,
                    gen.nextInt(3*w.ysize/2)-w.ysize/2);
        }
   }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}