package item;

import core.Data;
import java.util.ArrayList;
import world.Block;

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

	public void update() {
		for (int i = 0; i < blocks.size(); i++) {
			if (!invs.get(i).hasItems()) {
				invs.remove(i);
				blocks.remove(i);
				break;
			}
		}
	/*	for (int i = 0; i < blocks.size(); i++) {
			Block p = blocks.get(i);
			Data.Models.get(invs.get(i).get(0).type.model).draw(
			(float)(p.x), (float)(p.y), (float)(p.z), 0.f,
			Data.Textures.get(invs.get(i).get(0).type.texture));
		}*/
	}
}
