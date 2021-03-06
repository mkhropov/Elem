package item;

public class Item {
	public int amount;
	public int reservedAmount;
	public ItemTemplate type;

	public Item(ItemTemplate type, int amount){
		this.type = type;
		this.reservedAmount = 0;
		this.amount = amount;
	}

	public boolean sameType(ItemTemplate type){
		return this.type.getName().equalsIgnoreCase(type.name);
	}

	public boolean suitsConditionFree(String itemCondition){
		return type.suitsCondition(itemCondition)&&(reservedAmount < amount);
	}

	public boolean suitsConditionReserved(String itemCondition){
		return type.suitsCondition(itemCondition)&&(reservedAmount > 0);
	}

	public int reserve(int n) {
		if (reservedAmount < amount) {
			int res = Math.min(n, amount - reservedAmount);
			reservedAmount += res;
			return res;
		} else {
			return 0;
		}
	}

	public void release(int n) {
		reservedAmount -= n;
		if (reservedAmount < 0) reservedAmount = 0;
	}
}
