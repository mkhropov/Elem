package world;

import geomorph.*;
import physics.material.*;
import creature.*;
import pathfind.Pathfinder;
import graphics.Renderer;

import java.util.ArrayList;
import stereometry.Vector;

public class World {
    public int xsize, ysize, zsize;
    public Block[][][] blockArray;
    public Wall[][][][] wallArray;
    Biome biome;
    public Material[] material;
    public ArrayList<Creature> creature;
    public Pathfinder pf;
    public Renderer rend;
	public Vector gravity;

    public World(int x, int y, int z) {
        this.xsize = x;
        this.ysize = y;
        this.zsize = z;
		
		this.gravity = new Vector(0., 0., -4.9);

        this.blockArray = new Block[this.xsize][this.ysize][this.zsize];
        for (int i=0; i<this.xsize; i++)
            for (int j=0; j<this.ysize; j++)
                for (int k=0; k<this.zsize; k++)
                    this.blockArray[i][j][k] = new Block(i, j, k);

//FIXME how to make C-like enums, that are compatible with int?
        this.wallArray = new Wall[this.xsize][this.ysize][this.zsize][3];
        for (int i=0; i<this.xsize; i++)
            for (int j=0; j<this.ysize; j++)
                for (int k=0; k<this.zsize; k++) {
                    this.wallArray[i][j][k][0] = new Wall(i, j, k, WallOrient.TOP);
                    this.wallArray[i][j][k][1] = new Wall(i, j, k, WallOrient.LEFT);
                    this.wallArray[i][j][k][2] = new Wall(i, j, k, WallOrient.RIGHT);
                }

        this.material = new Material[4];
        this.material[0] = new Stone();
        this.material[1] = new Earth();
        this.material[2] = new Material();
		this.material[3] = new Bedrock();

        this.biome = new BiomeRough();
        this.biome.fillWorld(this);

        this.pf = new Pathfinder(this);

        this.creature = new ArrayList<Creature>();
//        System.out.printf("total %d creatures\n", creature.size());
//        this.creature.add(new SmartWalkingElem(this, this.blockArray[0][0][zsize-1]));
//        Creature cr = this.creature.get(0);
//        System.out.printf("0: %d %d %d\n", cr.b.x, cr.b.y, cr.b.z);
//        cr = this.creature.get(1);
//        System.out.printf("0: %d %d %d\n", cr.b.x, cr.b.y, cr.b.z);
//        this.creature.add(new SmartWalkingElem(this, this.blockArray[xsize-1][ysize-1][zsize-1]));
 //       System.out.printf("total %d creatures\n", creature.size());
//        this.creature.get(0).path = pf.getPath(this.creature.get(0),
//                                           this.creature.get(0).b,
//                                           this.creature.get(1).b);
    }

    public void iterate(long dT){
        for (int i=0; i<creature.size(); ++i){
//            System.out.printf("Iterate #%d\n", i);
            creature.get(i).iterate(dT);
        }
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
