package item;

import java.util.ArrayList;

public class ItemReservation {
	Inventory inv;
	ArrayList<Items> items;
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
	}

	public int amount(){
		int res = 0;
		for (int a: amounts){
			res += a;
		}
		return res;
	}
}
