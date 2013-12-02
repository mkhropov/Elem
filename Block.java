/**
 * Block is a class describing a minimal single 3D voxel
*/

public class Block {
    // this properties are given for example
    int x, y, z; //in a chunk
    int T; //temperature
    int material; //to be changed to a class
    int color; //to be changed to Texture
    int liquidLevel;
    int liquidId;
    int gaseId;
//    void[] items;
//    void[] creatures;

    Block(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    void setMaterial(int m) {
        this.material = m;
    }
}
