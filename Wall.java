/**
 * Wall is a class describing an interblock volume of indistinguable width
*/

import physics.material.*;

enum WallOrient {
    TOP, LEFT, RIGHT //isometric view
}

public class Wall {
    // this properties are given for example
    int x, y, z; //in a chunk
    WallOrient orientation;
    Temperature t; //temperature
    Material m; //to be changed to a class
    int color; //to be changed to Texture

    Wall(int x, int y, int z, WallOrient o){
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = o;
    }
}
