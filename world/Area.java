package world;

import java.util.ArrayList;
import java.util.Iterator;
import stereometry.BoundBox;

public class Area {
	private ArrayList<Block> blocks;
	
	public Area() {
		blocks = new ArrayList<>();
	}
	
	public Area(Block b) {
		blocks.add(new Block(b));
	}
	
	public Area(Area a) {
		for (int i=0; i<a.blocks.size(); i++) {
			blocks.add(new Block(a.blocks.get(i)));
		}
	}
	
	public Area(BoundBox b) {
		for (int x = Math.round((float)b.p1.x); x<=b.p2.x; x++)
		for (int y = Math.round((float)b.p1.y); y<=b.p2.y; y++)
		for (int z = Math.round((float)b.p1.z); z<=b.p2.z; z++) {
			blocks.add(new Block(x,y,z));
		}
	}
	
	public Area(BlockBox b) {
		for (int x = b.c1.x; x<=b.c2.x; x++)
		for (int y = b.c1.y; y<=b.c2.y; y++)
		for (int z = b.c1.z; z<=b.c2.z; z++) {
			blocks.add(new Block(x,y,z));
		}
	}
	
	public boolean isIn(Block b){
		for (int i=0; i<blocks.size(); i++) {
			if (blocks.get(i).isSame(b)){
				return true;
			}
		}
		return false;
	}
	
	public void remove(Block b){
		for (int i=0; i<blocks.size(); i++) {
			if (blocks.get(i).isSame(b)){
				blocks.remove(i);
				return;
			}
		}
	}
	
	public void intersect(Area a) {
		for (int i=0; i<a.blocks.size(); i++) {
			if (!isIn(a.blocks.get(i))) {
				remove(a.blocks.get(i));
			}
		}		
	}
	
	public void include(Area a) {
		for (int i=0; i<a.blocks.size(); i++) {
			if (!isIn(a.blocks.get(i))) {
				blocks.add(a.blocks.get(i));
			}	
		}
	}
	
	public void exclude(Area a) {
		for (int i=0; i<a.blocks.size(); i++) {
			if (isIn(a.blocks.get(i))) {
				remove(a.blocks.get(i));
			}	
		}
	}
	
	public void add(Block b) {
		if (isIn(b)) return;
		blocks.add(b);
	}
	
	public Iterator<Block> iterator(){
		return blocks.iterator();
	}
	
}
