package generation;

import java.util.ArrayList;
import java.util.Random;

import generation.morphs.Morph;
import generation.morphs.Pointfold;
import physics.Material;
import stereometry.Point;
import world.World;

public class Hills extends Biome {

	public static int stratumR = 30;

	public int h;

    public Hills(int x, int y, int R, int h){
		super(x, y, R);
        this.name = "hills";
		this.h = h;
    }

	public void generate(){
		Random rnd = Generator.getInstance().rnd;
		Stratum s;
		double dR;
		int hn = 0;
		int w;
		int m;
		while (hn < 4*h/5.-2){
			switch (rnd.nextInt(3)){
				case 0:
					m = Material.MATERIAL_STONE;
					break;
				case 1:
					m = Material.MATERIAL_MARBLE;
					break;
				case 2:
				default:
					m = Material.MATERIAL_GRANITE;
					break;
			}
			w = rnd.nextInt(3)+1;
			s = new Stratum(x, y, R*.9, R*1.1, w, m);
			stratums.add(s);
			hn += w;
		}
		double V = R*R*(h-hn); // volume/PI
		double v = 0;
		m = Material.MATERIAL_EARTH;
		while (v < V){
			dR = rnd.nextDouble();
			s = new Stratum(x+rnd.nextInt(2*R)-R, y+rnd.nextInt(2*R)-R, stratumR*(1+dR/5-.25)*.9, stratumR*(1+dR/5-.25)*1.1, 1, m);
			stratums.add(s);
			v += s.rmin*s.rmax; // estimated volume/PI
		}
		s = new Stratum(x, y, 2*R, 2*R, 2, m);
		stratums.add(s);
		Morph mr;
		int nmorph = (int)(Math.PI*R*R/400.); //subject to change
		for (int i=0; i<nmorph; ++i){
			mr = new Pointfold(new Point(x+rnd.nextInt(R)-R/2,
										 y+rnd.nextInt(R)-R/2, h),
							  10+rnd.nextInt(10), h, rnd.nextInt(4));
			morphs.add(mr);
		}
	}
}
