package graphics;

import world.*;

import java.util.ArrayList;

public class GraphicalChunk {
	private ArrayList<GraphicalCube> cubes;
	private World world;
	private int z;
	private int mode;
	public static final int MODE_TOP_VIEW = 0;
	public static final int MODE_SHOW_ALL = 1;
	public static final int MODE_FOG_OF_WAR = 2;

	public GraphicalChunk(World w, int z, int mode) {
		this.world = w;
		this.z = z;
		this.mode = mode;
		cubes = new ArrayList<GraphicalCube>(50000);
		rebuild();
	}

	public void rebuild() {
		cubes.clear();
		for (int i=0; i<world.xsize; i++)
			for (int j=0; j<world.ysize; j++){
				if (world.empty(i,j,z)) continue;
				if (mode == MODE_SHOW_ALL ||
						world.empty(i-1,j,z) || world.empty(i+1,j,z) || world.empty(i,j-1,z) ||
						world.empty(i,j+1,z) || world.empty(i,j,z-1) || world.empty(i,j,z+1)) {
					GraphicalCube c = new GraphicalCube(world.blockArray[i][j][z]);
					c.visible[GraphicalCube.TOP] = world.empty(i,j,z+1);
					c.visible[GraphicalCube.NORTH] = world.empty(i,j+1,z);
					c.visible[GraphicalCube.SOUTH] = world.empty(i,j-1,z);
					c.visible[GraphicalCube.EAST] = world.empty(i+1,j,z);
					c.visible[GraphicalCube.WEST] = world.empty(i-1,j,z);
					cubes.add(c);
					if (mode == MODE_SHOW_ALL){
						c.visible[GraphicalCube.TOP] = true;
					}
				}
		}
	}

	public void draw() {
		for (int i=0; i<cubes.size(); i++) {
			cubes.get(i).draw();
		}
	}
}
