package pathfind;

import creature.Action;
import creature.Creature;
import creature.Elem;
import item.Inventory;
import item.ItemReservation;
import java.util.ArrayList;
import java.util.Stack;
import player.Order;
import world.Block;
import world.World;

public class Path extends Stack<Action>{
	private Block lastBlock;
	private Condition lastCondition, newCondition;
	private final Order o;
	private final Creature c;
	
	public Path(Order o) {
		super();
		this.c = new Elem();
		this.o = o;
	}
	
	private void queueMove() throws PathFailure {
		Stack<Action> path = Pathfinder.getInstance().getPath(c, lastBlock, lastCondition, newCondition);
		if (path==null) {
			throw new PathFailure();
		}
		lastBlock = path.remove(0).b;
		this.addAll(0, path);
		lastCondition = new ConditionBeIn(lastBlock);
	}
	
	public void queueBuild() {
		this.push(new Action(Action.ACTION_BUILD, o.b.x, o.b.y, o.b.z, o.f, o.d, o.m));
		this.lastCondition = new ConditionReach(o.b, c);
		this.lastBlock = new Block(o.b);
	}
	
	private boolean queueItemStep(Integer n) throws PathFailure {
		newCondition = new ConditionItem(o.itemCondition);
		this.queueMove();
		Inventory inv = World.getInstance().items.getInventory(lastBlock);
		ItemReservation IR = inv.reserveItems(o.itemCondition, o.N-n);
		n += IR.amount();
		o.reserved.add(IR);
		o.path.add(0, new Action(Action.ACTION_TAKE, IR));
		return o.N!=n;
	}
	
	public void queueItems() throws PathFailure{
		int n = 0;
		while (queueItemStep(n)) {}
	}
	
	public void queueDig() {
		o.path.add(0, new Action(Action.ACTION_DIG, o.b.x, o.b.y, o.b.z, o.f, o.d));
		this.lastCondition = new ConditionReach(o.b, c);
		this.lastBlock = new Block(o.b);
	}
	
	public void queueAssign() throws PathFailure {
		newCondition = new ConditionWorker(o);
		this.queueMove();
		ArrayList<Creature> candidates = World.getInstance().getCreature(lastBlock);
		for (Creature creature: candidates){
			if (creature.capableOf(o)) {
				creature.owner.setOrderAssigned(o, creature);
				return;
			}
		}
		throw new PathFailure();
	}
	
	
}
