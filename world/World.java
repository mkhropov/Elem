package world;

import generation.*;
import physics.material.*;
import item.*;
import creature.*;
import pathfind.Pathfinder;
import graphics.Renderer;

import java.util.ArrayList;
import stereometry.*;

import physics.mana.*;

public class World {
    public static int DEFAULT_XSIZE = 1024;
    public static int DEFAULT_YSIZE = 1024;
    public static int DEFAULT_ZSIZE = 32;
    public int xsize, ysize, zsize;
    public char[][][] m;
    public Material[] material;
    public ArrayList<Creature> creature;
	public ArrayList<Item> item;
	public Vector gravity;
	public BoundBox bb;

	private static World instance = null;

	public static World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

    private World() {
		this.xsize = World.DEFAULT_XSIZE;
		this.ysize = World.DEFAULT_YSIZE;
		this.zsize = World.DEFAULT_ZSIZE;

		this.bb = new BoundBox(0, 0, 0, xsize, ysize, zsize);

		this.gravity = new Vector(0., 0., -4.9);

		System.out.print("Reserving world space ...");
        this.m = new char[xsize][ysize][zsize];
        for (int i=0; i<xsize; i++)
            for (int j=0; j<ysize; j++)
                for (int k=0; k<zsize; k++)
                    m[i][j][k] = Material.MATERIAL_NONE;
		System.out.print(" done\n");

        this.material = new Material[Material.MATERIAL_MAX];
        material[Material.MATERIAL_STONE] = new Stone();
        material[Material.MATERIAL_EARTH] = new Earth();
        material[Material.MATERIAL_NONE] = new Material();
		material[Material.MATERIAL_BEDROCK] = new Bedrock();
		material[Material.MATERIAL_MARBLE] = new Marble();
		material[Material.MATERIAL_GRANITE] = new Granite();

        this.creature = new ArrayList<>();
		this.item = new ArrayList<>();
    }

	public void init(){
//		ManaField.getInstance().addSource(
//			new ManaSource(blockArray[20][10][10],
//				new Vector(1., 1., 1.),
//				2.));
/*		ManaField.getInstance().addSource(
            new ManaSource(getBlock(10, 20, 15),
				new Vector(1., 1., 1.),
				5.));*/
		Generator.getInstance().generate();
		Generator.getInstance().apply();
	}

    public void iterate(long dT){
		if (Renderer.getInstance().draw_mana)
			ManaField.getInstance().iterate(dT);

        for (int i=0; i<creature.size(); ++i){
            creature.get(i).iterate(dT);
        }
    }

	public Block getBlock(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return null;
		if ((y<0) || (y>=ysize)) return null;
		if ((z<0) || (z>=zsize)) return null;
		return (new Block(x, y, z));
	}

	public ArrayList<Creature> getCreature(int x, int y, int z){
		ArrayList<Creature> res = new ArrayList<>(0);
		for (int i=0; i<creature.size(); ++i)
			if (creature.get(i).isIn(x, y, z))
				res.add(creature.get(i));
		return res;
	}

	public ArrayList<Creature> getCreature(Block b){
		return getCreature(b.x, b.y, b.z);
	}

	public ArrayList<Item> getItem(int x, int y, int z){
		ArrayList<Item> res = new ArrayList<>(0);
		for (int i=0; i<item.size(); ++i)
			if (item.get(i).isIn(x, y, z))
				res.add(item.get(i));
		return res;
	}

	public ArrayList<Item> getItem(Block b){
		return getItem(b.x, b.y, b.z);
	}

	public void destroyBlock(int x, int y, int z){
		item.add(new ItemBoulder(x, y, z, 1., m[x][y][z]));
		m[x][y][z] = Material.MATERIAL_NONE;
		Renderer.getInstance().updateBlock(x, y, z);
		if ((z+1) < zsize)
			updateBlock(x, y, z+1);
	}

	public void destroyBlock(Block b){
		destroyBlock(b.x, b.y, b.z);
	}

	public void updateBlock(int x, int y, int z){
		for (int i=0; i<creature.size(); ++i)
			if (creature.get(i).isIn(x, y, z))
				creature.get(i).update();
		for (int i=0; i<item.size(); ++i)
			if (item.get(i).isIn(x, y, z))
				item.get(i).update();
	}

	public void updateBlock(Block b){
		updateBlock(b.x, b.y, b.z);
	}

	public boolean isIn(int x, int y, int z){
	    if ((x < 0) || (x >= xsize)) return false;
		if ((y < 0) || (y >= ysize)) return false;
		if ((z < 0) || (z >= zsize)) return false;
		return true;
	}

	public boolean isIn(int x, int y, int z, int[] offset){
		return isIn(x+offset[0], y+offset[1], z+offset[2]);
	}

	public boolean isIn(double x, double y, double z){
	    if ((x < 0) || (x >= xsize)) return false;
		if ((y < 0) || (y >= ysize)) return false;
		if ((z < 0) || (z >= zsize)) return false;
		return true;
	}

	public boolean empty(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return true;
		if ((y<0) || (y>=ysize)) return true;
		if ((z<0) || (z>=zsize)) return true;
		return (m[x][y][z] == Material.MATERIAL_NONE);
	}

}
