package core;

import generation.Biome;
import graphics.Model;
import graphics.Texture;
import java.util.Arrays;
import physics.Material;
import player.ZoneTemplate;

import utils.JSONList;

public class Data{
	public static JSONList<Model> Models;
	public static JSONList<Material> Materials;
	public static JSONList<Texture> Textures;
	public static JSONList<ZoneTemplate> Zones;
	public static JSONList<Biome> Biomes;
	static Data instance = null;
	
	private Data(){
		Models = new JSONList<Model>(new Model());
		Models.load("res/models/");
		Models.initialize();
		Models.setDefault("cube");

		Materials = new JSONList<Material>(new Material());
		Material air = new Material();
		air.setName("air");
		Arrays.fill(air.weight, 0);
		Arrays.fill(air.support[0], 0);
		Arrays.fill(air.support[1], 0);
		Materials.add(air);
		Materials.setDefault("air");
		Materials.load("res/materials");
		Materials.initialize();

		Textures = new JSONList<Texture>(new Texture());
		Textures.load("res/textures/");
		Textures.initialize();
		Textures.setDefault("void");

		Zones = new JSONList<ZoneTemplate>(new ZoneTemplate());
		Zones.load("res/zones/");
		Zones.initialize();
		Zones.setDefault("stockpile");

		Biomes = new JSONList<Biome>(new Biome());
		Biomes.load("res/biomes/");
		Biomes.initialize();
	}
	
	public static Data getInstance() {
		if (instance == null){
			instance = new Data();
		}
		return instance;
	}
}
