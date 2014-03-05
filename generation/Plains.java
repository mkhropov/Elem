package generation;

import java.util.ArrayList;
import java.util.Random;

import generation.morphs.Morph;
import physics.material.Material;
import world.World;

public class Plains extends Biome {

	public static int stratumR = 40;

	public int h;

    public Plains(int x, int y, int R, int h){
		super(x, y, R);
        this.name = "plains";
		this.h = h;
    }

	public void generate(){
		Random rnd = Generator.getInstance().rnd;
		Stratum s;
		double dR;
		int hn = 0;
		int w;
		Material m;
		while (hn < 4*h/5.-2){
			switch (rnd.nextInt(3)){
				case 0:
					m = World.getInstance().material[Material.MATERIAL_STONE];
					break;
				case 1:
					m = World.getInstance().material[Material.MATERIAL_MARBLE];
					break;
				case 2:
				default:
					m = World.getInstance().material[Material.MATERIAL_GRANITE];
					break;
			}
			w = rnd.nextInt(3)+1;
			s = new Stratum(x, y, R*.9, R*1.1, w, m);
			stratums.add(s);
			hn += w;
		}
		double V = R*R*(h-hn); // volume/PI
		double v = 0;
		m = World.getInstance().material[Material.MATERIAL_EARTH];
		while (v < V){
			dR = rnd.nextDouble();
			s = new Stratum(x+rnd.nextInt(2*R)-R, y+rnd.nextInt(2*R)-R, stratumR*(1+dR/5-.25)*.9, stratumR*(1+dR/5-.25)*1.1, 1, m);
			stratums.add(s);
			v += s.rmin*s.rmax; // estimated volume/PI
		}
	}
}