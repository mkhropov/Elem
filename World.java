public class World {
    int length, width, depth;
    Chunk[][][] chunkArray; //FIXME should be dynamic
    
    World() {
        this.length = 1;
        this.width = 1;
        this.depth = 1;

        chunkArray = new Chunk[this.length][this.width][this.depth];
		BiomePlains b = new BiomePlains();

        for (int i=0; i<this.length; i++)
            for (int j=0; j<this.width; j++)
                for (int k=0; k<this.depth; k++){
                    chunkArray[i][j][k] = new Chunk(i, j, k, 10, 10);
					b.fillChunk(chunkArray[i][j][k]);
//                    chunkArray[i][j][k].generate();
                }
    }
}
