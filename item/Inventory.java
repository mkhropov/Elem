package item;

public class Inventory {
	public capacity;
	public overflowCapacity;
	private ArrayList<Item> items;

	public Inventory(int capacity) {
		this.capacity = capacity;
		this.overflowCapacity = capacity;
		this.items = new ArrayList<>();
	}

	public ItemReservation reserveItems(String itemCondition, int amount) {
		ItemResrvation IR = new ItemReservation(this);
		IR.reserve(itemCondition, amount);
	}

	void removeItem(ItemTemplate type, int amount) {
		Item found;
		for (Item i: items) {
			if (i.sameType(type)){
				assert(i.reservedAmount > amount);
				i.amount -= amount;
				i.reservedAmount -= amount;
				found = i;
			}
		}
		if (found.amount == 0) {
			items.remove(found);
		}
	}

	void addItem(ItemTemplate type, int amount) {
		for (Item i: items) {
			if (i.sameType(type)){
				i.amount += amount;
				return;
			}
		}
		Item i = new Item(type, amount);
		items.add(i);
	}

	public transferTo(Inventory inv, ItemReservation IR) {
		for (int i = 0; i<IR.items.size(); i++) {
			removeItem(IR.items.get(i).type, IR.amounts.get(i));
			inv.addItem(IR.items.get(i).type, IR.amounts.get(i));
		}
	}

	public transferFrom(Inventory inv, ItemReservation IR) {
		inv.transferTo(this, IR);
	}

	public transferAllTo(Inventory inv) {
		for (Item i: items) {
			inv.addItem(i, i.amount);
			removeItem(i, i.amount);
		}
	}

	public transferAllFrom(Inventory inv) {
		inv.transferAllTo(this);
	}
}
