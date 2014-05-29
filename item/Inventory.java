package item;

import java.util.ArrayList;
import world.Block;

public class Inventory {
	public int capacity;
	public int overflowCapacity;
	ArrayList<Item> items;

	public Inventory(int capacity) {
		this.capacity = capacity;
		this.overflowCapacity = capacity;
		this.items = new ArrayList<>();
	}

	public ItemReservation reserveItems(String itemCondition, int amount) {
		ItemReservation IR = new ItemReservation(this);
		IR.reserve(itemCondition, amount);
		return IR;
	}

	public Item get(int i){
		if (i<0 || i>=items.size())
			return null;
		return items.get(i);
	}

	public int amount(){
		int res = 0;
		for (Item i: items) {
			res += i.amount;
		}
		return res;
	}

	public ItemTemplate getSingleType(int num) {
		int res = 0;
		for (Item i: items) {
			res += i.amount;
			if (res>num)
				return i.type;
		}
		return items.get(0).type;
	}

	public boolean hasItems(){
		return items.size()>0;
	}

	public boolean hasItems(ItemTemplate it, int amount) {
		for (Item i: items) {
			if (i.sameType(it)&&(i.amount>=amount)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasItems(String itemCondition, int amount) {
		for (Item i: items) {
			if (i.suitsConditionFree(itemCondition)&&(i.amount>=amount)) {
				return true;
			}
		}
		return false;
	}

	public boolean suitsConditionFree(String itemCondition) {
		for (Item i: items) {
			if (i.suitsConditionFree(itemCondition)) {
				return true;
			}
		}
		return false;
	}

	public void removeItem(ItemTemplate type, int amount) {
		Item found;
		for (Item i: items) {
			if (i.sameType(type)){
				assert(i.reservedAmount > amount);
				i.amount -= amount;
				i.reservedAmount -= amount;
				found = i;
				if (found.amount == 0) {
					items.remove(found);
				}
				return;
			}
		}
	}

	public void addItem(ItemTemplate type, int amount) {
		for (Item i: items) {
			if (i.sameType(type)){
				i.amount += amount;
				return;
			}
		}
		Item i = new Item(type, amount);
		items.add(i);
	}

	public void transferTo(Inventory inv, ItemReservation IR) {
		for (int i = 0; i<IR.items.size(); i++) {
			removeItem(IR.items.get(i).type, IR.amounts.get(i));
			inv.addItem(IR.items.get(i).type, IR.amounts.get(i));
		}
	}

	public void transferFrom(Inventory inv, ItemReservation IR) {
		inv.transferTo(this, IR);
	}

	public void transferAllTo(Inventory inv) {
		for (Item i: items) {
			inv.addItem(i.type, i.amount);
			removeItem(i.type, i.amount);
		}
	}

	public void transferAllFrom(Inventory inv) {
		inv.transferAllTo(this);
	}
}
