package world;

import generation.*;
import physics.material.*;
import creature.*;
import pathfind.Pathfinder;
import graphics.Renderer;

import java.util.ArrayList;
import stereometry.*;

import physics.mana.*;

public class World {
    public static int DEFAULT_XSIZE = 64;
    public static int DEFAULT_YSIZE = 64;
    public static int DEFAULT_ZSIZE = 32;
    public int xsize, ysize, zsize;
    public Block[][][] blockArray;
    public Wall[][][][] wallArray;
    public Biome biome;
    public Material[] material;
    public ArrayList<Creature> creature;
    public Pathfinder pf;
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

		System.out.print("Reserving world space ");
        this.blockArray = new Block[this.xsize][this.ysize][this.zsize];
        for (int i=0; i<this.xsize; i++){
//			if (i%(this.xsize/30) == 0)
//				System.out.print(".");
            for (int j=0; j<this.ysize; j++)
                for (int k=0; k<this.zsize; k++)
                    this.blockArray[i][j][k] = new Block(i, j, k);
		}
		System.out.print(" done\n");

//FIXME how to make C-like enums, that are compatible with int?
/*        this.wallArray = new Wall[this.xsize][this.ysize][this.zsize][3];
        for (int i=0; i<this.xsize; i++)
            for (int j=0; j<this.ysize; j++)
                for (int k=0; k<this.zsize; k++) {
                    this.wallArray[i][j][k][0] = new Wall(i, j, k, WallOrient.TOP);
                    this.wallArray[i][j][k][1] = new Wall(i, j, k, WallOrient.LEFT);
                    this.wallArray[i][j][k][2] = new Wall(i, j, k, WallOrient.RIGHT);
                }
*/
        this.material = new Material[Material.MATERIAL_MAX];
        this.material[Material.MATERIAL_STONE] = new Stone();
        this.material[Material.MATERIAL_EARTH] = new Earth();
        this.material[Material.MATERIAL_NONE] = new Material();
		this.material[Material.MATERIAL_BEDROCK] = new Bedrock();
		this.material[Material.MATERIAL_MARBLE] = new Marble();
		this.material[Material.MATERIAL_GRANITE] = new Granite();

//        this.biome = new BiomePlains();
//        this.biome.fillWorld(this);

        this.creature = new ArrayList<>();
    }

	public void init(){
//		ManaField.getInstance().addSource(
//			new ManaSource(blockArray[20][10][10],
//				new Vector(1., 1., 1.),
//				2.));
/*		ManaField.getInstance().addSource(
            new ManaSource(blockArray[10][20][15],
				new Vector(1., 1., 1.),
				5.));*/
		Generator.getInstance().generate();
		Generator.getInstance().apply();
	}

    public void iterate(long dT){
		if (Renderer.getInstance().draw_mana)
			ManaField.getInstance().iterate(dT);
        for (int i=0; i<creature.size(); ++i){
//            System.out.printf("Iterate #%d\n", i);
            creature.get(i).iterate(dT);
        }
    }

	public boolean isIn(Block b, int[] offset){
	    if ((b.x+offset[0] < 0) || (b.x+offset[0] >= xsize)) return false;
		if ((b.y+offset[1] < 0) || (b.y+offset[1] >= ysize)) return false;
		if ((b.z+offset[2] < 0) || (b.z+offset[2] >= zsize)) return false;
		return true;
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
		return (blockArray[x][y][z].m == null);
	}

	public Block getBlock(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return null;
		if ((y<0) || (y>=ysize)) return null;
		if ((z<0) || (z>=zsize)) return null;
		return (blockArray[x][y][z]);
	}
}
