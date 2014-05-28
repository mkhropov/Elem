package item;

public class ItemManager {
	private ArrayList<Inventory> invs;
	private ArrayList<Block> blocks;

	public boolean hasItems(Block b) {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).isSame(b)) {
				return invs.get(i).hasItems();
			}
		}
		return false;
	}

	public getInventory (Block b) {
		int found = -1;
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).isSame(b)) {
				found = i;
			}
		}
		if (found<0) {
			invs.add(new Inventory);
			blocks.add(b);
			return invs.get(invs.size());
		} else {
			return invs.get(found);
		}
	}

	public void update() {
		for (int i = 0; i < blocks.size(); i++) {
			if (!invs.get(i).hasItems()
	}
}
