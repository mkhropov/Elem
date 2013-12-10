/**
 * Block is a class describing a minimal single 3D voxel
*/

public class Block {
    // this properties are given for example
    int x, y, z; //in a chunk
    int T; //temperature
    Material m; //to be changed to a class
    int liquidLevel;
    int liquidId;
    Gase gase;
//    void[] items;
//    void[] creatures;

    public Block(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = null;
    }

    void setMaterial(Material m) {
        this.m = m;
    }
}
