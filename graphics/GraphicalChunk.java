package graphics;

import world.*;

import java.util.ArrayList;

public class GraphicalChunk {
	ArrayList<GraphicalCube> cubes;

//				if (!w.empty(i,j,z)) //continue;
//				if (w.empty(i-1,j,z) || w.empty(i+1,j,z) || w.empty(i,j-1,z) ||
//					w.empty(i,j+1,z) || w.empty(i,j,z-1) || w.empty(i,j,z+1)) {
	public GraphicalChunk(World w, int z) {
		boolean v;
		cubes = new ArrayList<GraphicalCube>(50000);
		for (int i=0; i<w.xsize; i++)
			for (int j=0; j<w.ysize; j++){
				if (w.empty(i,j,z)) continue;
				if (w.empty(i-1,j,z) || w.empty(i+1,j,z) || w.empty(i,j-1,z) ||
					w.empty(i,j+1,z) || w.empty(i,j,z-1) || w.empty(i,j,z+1)) {
						GraphicalCube c = new GraphicalCube(w.blockArray[i][j][z]);
						c.visible[GraphicalCube.TOP] = w.empty(i,j,z+1);
						c.visible[GraphicalCube.NORTH] = w.empty(i,j+1,z);
						c.visible[GraphicalCube.SOUTH] = w.empty(i,j-1,z);
						c.visible[GraphicalCube.EAST] = w.empty(i+1,j,z);
						c.visible[GraphicalCube.WEST] = w.empty(i-1,j,z);
						cubes.add(c);
				}
		}
	}

	public void draw() {
		for (int i=0; i<cubes.size(); i++) {
			cubes.get(i).draw();
		}
	}
}
