package generation;

import java.util.ArrayList;
import generation.morphs.*;
import physics.material.Material;
import stereometry.*;
import world.World;

public class TestBiome extends Biome {

    public TestBiome(int x, int y, int R){
		super(x, y, R);
        this.name = "test biome";
    }

	public void generate(){
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 10, World.getInstance().material[Material.MATERIAL_STONE]));
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 3, World.getInstance().material[Material.MATERIAL_GRANITE]));
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 3, World.getInstance().material[Material.MATERIAL_MARBLE]));
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 3, World.getInstance().material[Material.MATERIAL_EARTH]));


		morphs.add(new Slipfault(new Front(new Point(15, -1, 29), new Point(15, 30, 29), 0), 12, 30, 3));

		morphs.add(new Pointfold(new Point(x, y, World.getInstance().zsize),
					R/5, World.getInstance().zsize-5, 8));

		morphs.add(new Linefold(new Front(new Point(-1, -1, 29), new Point(30, 30, 29), 0), 15, 30, -5));

		morphs.add(new Wavefold(new Front(new Point(30, -1, 29), new Point(-1, 30, 29), 0), 15, 30, 2));

	}
}
