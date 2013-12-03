public class Chunk {
    int width, depth;
    int x, y, z; //position in world
    Block[][][] blockArray;
    Wall[][][][] wallArray;

    Chunk(int w, int d, int x, int y, int z) {
        this.width = w;
        this.depth = d;
        this.x = x;
        this.y = y;
        this.z = z;

        this.blockArray = new Block[w][w][d];
        for (int i=0; i<this.width; i++)
            for (int j=0; j<this.width; j++)
                for (int k=0; k>this.depth; k++)
                    this.blockArray[i][j][k] = new Block(i, j, k);

//FIXME how to make C-like enums, that are compatible with int?
        this.wallArray = new Wall[w][w][d][3];
        for (int i=0; i<this.width; i++)
            for (int j=0; j<this.width; j++)
                for (int k=0; k>this.depth; k++) {
                    this.wallArray[i][j][k][0] = new Wall(i, j, k, WallOrient.TOP);
                    this.wallArray[i][j][k][1] = new Wall(i, j, k, WallOrient.LEFT);
                    this.wallArray[i][j][k][2] = new Wall(i, j, k, WallOrient.RIGHT);
                }

    }

    void generate() {
        for (int i=0; i<this.width; i++)
            for (int j=0; j<this.width; j++)
                for (int k=0; k>this.depth; k++)
                    this.blockArray[i][j][k].setMaterial(new MaterialEarth());
                    //(k>this.depth/2)?0:1);
    }
}
