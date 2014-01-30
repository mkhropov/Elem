package physics.mana;

import world.Block;

public class ManaDrain extends Block {

	double flow;

	public ManaDrain(Block b, ManaField f){
		super(b);
		this.flow = f.field[b.x][b.y][b.z].len();
		f.addDrain(this);
	}
}
