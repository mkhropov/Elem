package player;

import java.util.ArrayList;
import java.util.Stack;
import graphics.Renderer;
import world.*;
import creature.*;
import physics.material.*;
import item.Item;
import item.ItemTemplate;
import physics.magics.*;

import pathfind.*;


public class Player {
    public ArrayList<Order> order;
    public ArrayList<Creature> creature;
	public ArrayList<Spell> spellbook;
	public int mana;

    public Player(){
        this.order = new ArrayList<>();
        this.creature = new ArrayList<>();
		this.spellbook = new ArrayList<>();
		spellbook.add(0, new SpellSummon(this));
		spellbook.add(1, new SpellSunstrike(this));
		this.mana = 0;
    }

	public boolean cast(int i, Block b){
		if (i>spellbook.size())
			return false;
		Spell s = spellbook.get(i);
		if (mana>=s.cost()){
			mana -= s.cost();
			s.cast(b);
			return true;
		} else
			return false;
	}

    public void spawnCreature(Creature c){
		World w = World.getInstance();
		if (w.getBlock(c.p).m != Material.MATERIAL_NONE)
			return;
        w.creature.add(c);
        creature.add(c);
        c.owner = this;

		for (int i=0; i<order.size(); ++i)
			order.get(i).declined = order.get(i).is_accesible();
   }

    public void placeMoveOrder(Block b){
        order.add(new Order(b, Order.ORDER_MOVE));
    }

    public void placeDigOrder(Block b){
		if (b.m == Material.MATERIAL_NONE)
			return;
		if (blockAlreadyRequested(b))
			return;
		Order o = new Order(b, Order.ORDER_DIG);
        order.add(o);
		Renderer.getInstance().addEntity(o.cube);
    }

    public void placeBuildOrder(Block b, char m){
		if (b.m != Material.MATERIAL_NONE)
			return;
		if (blockAlreadyRequested(b))
			return;
		ItemTemplate it = new ItemTemplate(Item.TYPE_BUILDABLE, m);
		Order o = new Order(null, Order.ORDER_TAKE); o.it = it;
	    order.add(o);
	    o = new Order(b, Order.ORDER_BUILD); o.it = it; o.m = m;
//        Order o = new Order(b, Order.ORDER_BUILD); o.it = it; o.m = m;
        order.add(o);
		Renderer.getInstance().addEntity(o.cube);
    }

	public boolean blockAlreadyRequested(Block b){
		for (int i=0; i<order.size(); ++i)
			if ((order.get(i).b != null) && (order.get(i).b.isSame(b)))
				return true;
		return false;
	}

	public void iterate(){ //give orders to elems
		int t = 0;
		int i = -1;
		Order o;
		Condition c1, c2;
		Block b;
		Stack<Action> path;
		ArrayList<Creature> candidates;
		Pathfinder p = Pathfinder.getInstance();
		Elem e = new Elem();
//		System.out.println("Iterating player...");
		while((t<100) && (++i<order.size())){
//			System.out.println("Order #"+i);
			o = order.get(i);
			if (o.taken || o.declined)
				continue;
			switch (o.type) {
			case (Order.ORDER_BUILD):
				b = o.b;
				c1 = new ConditionReach(b, e);
				c2 = new ConditionItem(o.it);
				path = p.getPath(e, b, c1, c2);
				if (path==null){
					o.declined = true;
					System.out.println("Build order "+o+" declined at buildable search");
					continue;
				}
				t += path.size();
				b = path.remove(0).b;
				o.path.add(new Action(Action.ACTION_BUILD, b.x, b.y, b.z));
				o.path.addAll(0, path);

				c1 = new ConditionBeIn(b);
				c2 = new ConditionWorker(o);
				path = p.getPath(e, b, c1, c2);
				if (path==null){
					o.declined = true;
					System.out.println("Build order "+o+" declined at worker search");
					continue;
				}
				t += path.size();
				candidates = World.getInstance().getCreature(path.remove(0).b);
				o.path.add(new Action(Action.ACTION_TAKE, o.it));
				o.path.addAll(0, path);
				for (Creature c: candidates){
					if (c.capableOf(o)) {
						c.order = o;
						c.plans = o.path;
						o.dumpPath();
						o.taken = true;
						continue;
					}
				}
				break;
			case (Order.ORDER_DIG):
				b = o.b;
				c1 = new ConditionReach(b, e);
				c2 = new ConditionWorker(o);
				path = p.getPath(e, b, c1, c2);
				if (path==null){
					o.declined = true;
					System.out.println("Dig order "+o+" declined at worker search");
					continue;
				}
				t += path.size();
				candidates = World.getInstance().getCreature(path.remove(0).b);
				o.path.add(new Action(Action.ACTION_DIG, b.x, b.y, b.z));
				o.path.addAll(0, path);
				for (Creature c: candidates){
					if (c.capableOf(o)) {
						System.out.println("Assigned to elem @("+c.p.x+","+c.p.y+","+c.p.z+")");
						c.order = o;
						c.plans = o.path;
						o.dumpPath();
						o.taken = true;
						continue;
					}
				}
				break;
			default:
				break;
			}
		}
	}


    public void setOrderTaken(Order o, Creature c){
        c.order = o;
        o.taken = true;
  //      System.out.println(c+" took order "+o);
    }

    public void setOrderDone(Order o, Creature c){
		if ((o.type == Order.ORDER_BUILD) ||
			(o.type == Order.ORDER_DIG))
			Renderer.getInstance().removeEntity(o.cube);
        order.remove(o);
        c.order = null;
		for (int i=0; i<order.size(); ++i)
			order.get(i).declined = order.get(i).is_accesible();
        System.out.println(c+" succesfuly did order "+o);
    }

	public void setOrderDeclined(Order o, Creature c){
		c.order = null;
		o.taken = false;
	//	System.out.println(c+" declined  order "+o);
	}

    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
		c.plans = null;
        System.out.println(c+" aborted order "+o);
    }
}
