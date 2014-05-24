package player;

import java.util.Iterator;
import world.Area;
import world.Block;
import world.BlockBox;

public class Zone {
	Area area;
	public ZoneTemplate type;
	
	public Zone(ZoneTemplate type) {
		this.type = type;
		this.area = new Area();
	}
	
	public Zone(ZoneTemplate type, Block b) {
		this.type = type;
		this.area = new Area(b);
	}
	
	public Zone(ZoneTemplate type, Area a){
		this.type = type;
		this.area = new Area(a);
	}
	
	public Zone(ZoneTemplate type, BlockBox b){
		this.type = type;
		this.area = new Area(b);
	}
	
	public void add(Block b) {
		area.add(b);
	}
	
	public Iterator<Block> iterator(){
		return area.iterator();
	}
}
