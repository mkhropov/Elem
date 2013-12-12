public class World {
    int xsize, ysize, zsize; //position in world
    Block[][][] blockArray;
    Wall[][][][] wallArray;
    Biome biome;

    World(int x, int y, int z) {
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

        this.biome = new BiomeRough();
        this.biome.fillWorld(this);
    }
}
