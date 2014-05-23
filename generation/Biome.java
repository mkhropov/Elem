package generation;

import core.Data;
import generation.morphs.Morph;
import java.util.ArrayList;
import physics.Material;

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
		stratums.add(new Stratum(x, y, 2*R/3, 4*R/3, 3, Data.Materials.getId("earth")));
	}
}
