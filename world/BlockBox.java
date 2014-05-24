package world;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class BlockBox {
	public Block c1, c2;
	
	public BlockBox(Block b1, Block b2) {
		c1 = new Block(min(b1.x, b2.x), min(b1.y, b2.y), min(b1.z, b2.z));
		c2 = new Block(max(b1.x, b2.x), max(b1.y, b2.y), max(b1.z, b2.z));
	}
	
	public boolean isIn(Block b) {
		return (c1.x<=b.x) && (b.x<=c2.x) &&
		       (c1.y<=b.y) && (b.y<=c2.y) &&
		       (c1.z<=b.z) && (b.z<=c2.z);
	}
}
