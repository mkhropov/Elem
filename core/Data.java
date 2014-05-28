package core;

import generation.Biome;
import graphics.Model;
import graphics.Texture;
import item.ItemTemplate;
import world.VeinPatch;
import physics.Material;
import player.ZoneTemplate;

import java.util.Arrays;
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
	public static JSONList<VeinPatch> Veins;

	private static Data instance = null;

	private Data() {
		System.out.print("Creating models...");
		Models = new JSONList<>(new Model());
		Models.load("res/models/");
		Models.initialize();
		Models.setDefault("cube");
		System.out.print(" ok\n");
		System.out.print("Creating materials...");
		Materials = new JSONList<>(new Material());
		Material air = new Material();
		air.setName("air");
		air.texture = "void";
		Arrays.fill(air.weight, 0);
		Arrays.fill(air.support[0], 0);
		Arrays.fill(air.support[1], 0);
		Materials.add(air);
		Materials.setDefault("air");
		Materials.load("res/materials");
		Materials.initialize();
		System.out.print(" ok\n");
		System.out.print("Creating textures...");
		Textures = new JSONList<>(new Texture());
		Textures.load("res/textures/");
		Textures.initialize();
		Textures.setDefault("void");
		System.out.print(" ok\n");
		System.out.print("Creating zones...");
		Zones = new JSONList<>(new ZoneTemplate());
		Zones.load("res/zones/");
		Zones.initialize();
		Zones.setDefault("stockpile");
		System.out.print(" ok\n");
		System.out.print("Creating biomes...");
		Biomes = new JSONList<>(new Biome());
		Biomes.load("res/biomes/");
		Biomes.initialize();
		System.out.print(" ok\n");
		System.out.print("Creating items...");
		Items = new JSONList<>(new ItemTemplate());
		Items.load("res/items/");
		Items.initialize();
		Items.setDefault("dirt chunk");
		System.out.print(" ok\n");
		System.out.print("Creating veins...");
		Veins = new JSONList<>(new VeinPatch());
		Veins.load("res/veins/");
		Veins.initialize();
		System.out.print(" ok\n");

		/* now to check the data integrity...
		 * list is not complete yet, can check even
		 * ranges and such, but I'm lazy atm
		 */
		System.out.print("Checking integrity: ");
		System.out.print("materials... ");
		for (Material m: Materials.asList()) {
			checkName(m.texture, Textures);
//			checkName(m.drop, Items);
			assert(m.hardness >= 0);
			assert(m.dropAmount >= 0);
		}
		System.out.print("biomes... ");
		for (Biome b: Biomes.asList()) {
			for (String s: b.stratumMat)
				checkName(s, Materials);
			for (double d: b.stratumChance)
				assert(d>=0.);
			for (int i: b.morph)
				assert(i<4);
			checkName(b.erodeMat, Materials);
		}
		System.out.print("items... ");
		for (ItemTemplate it: Items.asList()) {
			checkName(it.texture, Textures);
			checkName(it.model, Models);
		}
		System.out.print("veins... ");
		for (VeinPatch vp: Veins.asList()) {
			checkName(vp.model, Models);
			checkName(vp.texture, Textures);
			checkName(vp.drop, Items);
		}
		System.out.print(" ok\n");
	}

	public static Data getInstance() {
		if (instance == null){
			instance = new Data();
		}
		return instance;
	}

	private <T extends Named & Initializable> void
		checkName(String name, JSONList<T> list) {
		if (!list.contains(name)) {
			System.out.println("Can't find name "+name+" in list "+list.toString());
			System.exit(1);
		}
	}
}
