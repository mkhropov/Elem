package generation;

import core.Data;
import generation.morphs.*;
import utils.Random;
import stereometry.Front;
import stereometry.Point;
import utils.Initializable;
import utils.Named;

public class Biome implements Named, Initializable {

	public String name;
	public String title;
	public String description;

	public int height;
	public String stratumMat[];
	public double stratumChance[];
	public int stratumHeight[][];
	public int stratumMinR[][];
	public int stratumMaxR[][];

	public String vein[];
	public double veinChance[];

	public double morphDensity;
	public int morph[];
	public double morphParam[][][];
	public double morphChance[];

	public String erodeMat;
	public int erodeWidth;
	public int erodeStrength;

	public Biome() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void initialize() {
	}

	public Stratum genStratum(int x, int y) {
		int m;
		double rmin;
		double rmax;
		double width;
		Random rnd = Random.getInstance();
		int i = rnd.getWeighted(stratumChance);
		m = Data.Materials.getId(stratumMat[i]);
		rmin = rnd.avg(stratumMinR[i]);
		rmax = rnd.avg(stratumMaxR[i]);
		width = rnd.avg(stratumHeight[i]);
		Stratum s = new Stratum(x, y, rmin, rmax, width, m);
		if (rnd.nextDouble() < veinChance[i]) {
			Vein v = new Vein(s.O, 2 * Math.PI * rnd.nextDouble(), rmax);
			v.name = vein[i];
			v.grow(s);
			s.vein = v;
		}
		return s;
	}

	public Morph genMorph(int x, int y, int z) {
		Morph m;
		Random rnd = Random.getInstance();
		int i = rnd.getWeighted(morphChance);
		switch (morph[i]) {
		case 0: /* Pointfold */
			Pointfold pf;
			pf = new Pointfold(new Point(x, y, z),
								rnd.avg(morphParam[i][0]), // l
								rnd.avg(morphParam[i][1]), // h
								rnd.avg(morphParam[i][2])); // d
//			System.out.printf("Morph params %f %f %f\n", pf.l, pf.h, pf.d);
			m = pf;
			break;
		case 1: /* Linefold */
			Linefold lf;
			lf = new Linefold(new Front(new Point(x, y, z),
										2*Math.PI*rnd.nextDouble(),
										rnd.avg(morphParam[i][3]), 0.),
								rnd.avg(morphParam[i][0]), // l
								rnd.avg(morphParam[i][1]), // h
								rnd.avg(morphParam[i][2])); // d
			m = lf;
			break;
		case 2: /* Wavefold */
			Wavefold wf;
			wf = new Wavefold(new Front(new Point(x, y, z),
										2*Math.PI*rnd.nextDouble(),
										rnd.avg(morphParam[i][3]), 0.),
								rnd.avg(morphParam[i][0]), // l
								rnd.avg(morphParam[i][1]), // h
								rnd.avg(morphParam[i][2])); // d
			m = wf;
			break;
		case 3: /* Slipfault */
			Slipfault sf;
			sf = new Slipfault(new Front(new Point(x, y, z),
										2*Math.PI*rnd.nextDouble(),
										rnd.avg(morphParam[i][3]), 0.),
								rnd.avg(morphParam[i][0]), // l
								rnd.avg(morphParam[i][1]), // h
								rnd.avg(morphParam[i][2])); // d
			m = sf;
			break;
		default:
			m = null;
			break;
		}
		return m;
	}
}
