package item;

import java.util.ArrayList;

public class ItemReservation {
	Inventory inv;
	ArrayList<Item> items;
	ArrayList<Integer> amounts;

	ItemReservation(Inventory inv){
		this.inv = inv;
		items = new ArrayList<>();
		amounts = new ArrayList<>();
	}

	public int reserve(String itemCondition, int amount) {
		int a;
		int res = 0;
		for (Item i: inv.items) {
			if (i.suitsConditionFree(itemCondition)) {
				a = i.reserve(amount-res);
				items.add(i);
				amounts.add(a);
				res += a;
			}
		}
		return res;
	}

	public int amount(){
		int res = 0;
		for (int a: amounts){
			res += a;
		}
		return res;
	}
	
	public void release() {
		for (int i=0; i<items.size(); i++){
			items.get(i).release(amounts.get(i));
		}
	}
}
