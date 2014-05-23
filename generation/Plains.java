package generation;

import core.Data;
import java.util.Random;
import physics.Material;

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
		int m;
		while (hn < 4*h/5.-2){
			switch (rnd.nextInt(3)){
				case 0:
					m = Data.Materials.getId("stone");
					break;
				case 1:
					m = Data.Materials.getId("marble");
					break;
				case 2:
				default:
					m = Data.Materials.getId("granite");
					break;
			}
			w = rnd.nextInt(3)+1;
			s = new Stratum(x, y, R*.9, R*1.1, w, m);
			stratums.add(s);
			hn += w;
		}
		double V = R*R*(h-hn); // volume/PI
		double v = 0;
		m = Data.Materials.getId("earth");
		while (v < V){
			dR = rnd.nextDouble();
			s = new Stratum(x+rnd.nextInt(2*R)-R, y+rnd.nextInt(2*R)-R, stratumR*(1+dR/5-.25)*.9, stratumR*(1+dR/5-.25)*1.1, 1, m);
			stratums.add(s);
			v += s.rmin*s.rmax; // estimated volume/PI
		}
	}
}
