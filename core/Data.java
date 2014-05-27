package core;

import generation.Biome;
import graphics.Model;
import graphics.Texture;
import item.ItemTemplate;
import java.util.Arrays;
import physics.Material;
import player.ZoneTemplate;
import utils.Initializable;

import utils.JSONList;
import utils.Named;

public class Data{
	public static JSONList<Model> Models;
	public static JSONList<Material> Materials;
	public static JSONList<Texture> Textures;
	public static JSONList<ZoneTemplate> Zones;
	public static JSONList<Biome> Biomes;
	public static JSONList<ItemTemplate> Items;

	private static Data instance = null;

	private Data(){
		Models = new JSONList<>(new Model());
		Models.load("res/models/");
		Models.initialize();
		Models.setDefault("cube");

		Materials = new JSONList<>(new Material());
		Material air = new Material();
		air.setName("air");
		Arrays.fill(air.weight, 0);
		Arrays.fill(air.support[0], 0);
		Arrays.fill(air.support[1], 0);
		Materials.add(air);
		Materials.setDefault("air");
		Materials.load("res/materials");
		Materials.initialize();

		Textures = new JSONList<>(new Texture());
		Textures.load("res/textures/");
		Textures.initialize();
		Textures.setDefault("void");

		Zones = new JSONList<>(new ZoneTemplate());
		Zones.load("res/zones/");
		Zones.initialize();
		Zones.setDefault("stockpile");

		Biomes = new JSONList<>(new Biome());
		Biomes.load("res/biomes/");
		Biomes.initialize();
		
		Items = new JSONList<>(new ItemTemplate());
		Items.load("res/items/");
		Items.initialize();
		Items.setDefault("dirt chunk");

		/* now to check the data integrity...
		 * list is not complete yet, can check even
		 * ranges and such, but I'm lazy atm
		 */
		for (Material m: Materials.asList()) {
			checkName(m.texture, Textures);
//			checkName(m.drop, Items);
			assert(m.hardness >= 0);
			assert(m.dropAmount >= 0);
		}

		for (Biome b: Biomes.asList()) {
			for (String s: b.stratumMat)
				checkName(s, Materials);
			for (double d: b.stratumChance)
				assert(d>=0.);
			for (int i: b.morph)
				assert(i<4);
			checkName(b.erodeMat, Materials);
		}
	}

	public static Data getInstance() {
		if (instance == null){
			instance = new Data();
		}
		return instance;
	}

	private <T extends Named & Initializable> void
		checkName(String name, JSONList<T> list) {
		assert (list.contains(name));
	}
}
