package world;

import geomorph.*;
import physics.material.*;
import creature.*;

import java.util.ArrayList;

public class World {
    public int xsize, ysize, zsize;
    public Block[][][] blockArray;
    public Wall[][][][] wallArray;
    Biome biome;
    public Material[] material;
    public ArrayList<Creature> creature;

    public World(int x, int y, int z) {
        this.xsize = x;
        this.ysize = y;
        this.zsize = z;

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

        this.material = new Material[3];
        this.material[0] = new Stone();
        this.material[1] = new Earth();
        this.material[2] = new Material();

        this.biome = new BiomeRough();
        this.biome.fillWorld(this);

        this.creature = new ArrayList<Creature>(2);
        this.creature.add(new Elem(this, this.blockArray[0][0][zsize-1]));
        this.creature.add(new Elem(this, this.blockArray[xsize-1][0][zsize-1]));
    }

    public void iterate(){
        for (int i=0; i<creature.size(); ++i)
            creature.get(i).iterate();
    }
}
