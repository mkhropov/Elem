package generation;

import java.util.ArrayList;
import generation.morphs.Morph;
import physics.material.Material;
import world.World;

public class Biome {

	int x, y, R;

	public ArrayList<Morph> morphs;
	public ArrayList<Stratum> stratums;

    public String name;

    public Biome(int x, int y, int R){
		this.x = x;
		this.y = y;
		this.R = R;
		this.morphs = new ArrayList<>();
		this.stratums = new ArrayList<>();
        this.name = "";
    }

	public void generate(){
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 3, Material.MATERIAL_EARTH));
	}
}
