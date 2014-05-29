package item;

import core.Data;
import java.util.ArrayList;
import world.Block;
import utils.PseudoRandom;
import stereometry.Point;

public class ItemManager {
	private ArrayList<Inventory> invs;
	private ArrayList<Block> blocks;

	public ItemManager() {
		invs = new ArrayList<>();
		blocks = new ArrayList<>();
	}

	public boolean hasItems(Block b) {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).isSame(b)) {
				return invs.get(i).hasItems();
			}
		}
		return false;
	}

	public Inventory getInventory (Block b) {
		int found = -1;
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).isSame(b)) {
				found = i;
			}
		}
		if (found<0) {
			invs.add(new Inventory(50));
			blocks.add(b);
			return invs.get(invs.size()-1);
		} else {
			return invs.get(found);
		}
	}

	boolean removeEmpty(){
		for (int i = 0; i < blocks.size(); i++) {
			if (!invs.get(i).hasItems()) {
				invs.remove(i);
				blocks.remove(i);
				return true;
			}
		}
		return false;
	}

	public void draw() {
		for (int i = 0; i < blocks.size(); i++) {
			if (invs.get(i).hasItems()){
				Inventory inv = invs.get(i);
				int maxItems = inv.amount();
//				System.out.print(maxItems+"  ->  ");
				int drawItems = Math.min(maxItems, 5);
				Block b = blocks.get(i);
				for (int j = 0; j < drawItems; j++) {
					Point p = PseudoRandom.position(b.x, b.y, b.z, j);
				//	p.x+=Math.sin(j)/2;
				//	p.y+=Math.cos(j)/2;
				//	p.z+=(j==4)?(0.3):(0.0);
					int pick = PseudoRandom.pick(maxItems, j);
//					System.out.print(pick+" ");
					Data.Models.get(inv.getSingleType(pick).model).draw(
						(float)(p.x), (float)(p.y), (float)(p.z), (float)(1.9*p.x-0.5*p.y+j),
						Data.Textures.get(inv.getSingleType(pick).texture));
				}
//				System.out.println();
			}
		}
	}

	public void update() {
		while (removeEmpty()) {}
	}
}
