package player;

import java.util.ArrayList;
import world.*;
import creature.*;
import physics.material.*;
import item.Item;
import item.ItemTemplate;

public class Player {
    World w;
    public ArrayList<Order> order;
    public ArrayList<Creature> creature;

    public Player(World w){
        this.w = w;
        this.order = new ArrayList<>();
        this.creature = new ArrayList<>();
    }

    public void spawnCreature(Creature c){
        w.creature.add(c);
        creature.add(c);
        c.owner = this;
    }

    public void placeMoveOrder(Block b){
        order.add(new Order(b, Order.ORDER_MOVE));
    }

    public void placeDigOrder(Block b){
        order.add(new Order(b, Order.ORDER_DIG));
    }

    public void placeBuildOrder(Block b, Material m){
		ItemTemplate it = new ItemTemplate(Item.TYPE_BUILDABLE, m);
		Order o = new Order(null, Order.ORDER_TAKE); o.it = it;
		order.add(o);
        o = new Order(b, Order.ORDER_BUILD); o.it = it; o.m = m;
        order.add(o);
    }

    public void setOrderTaken(Order o, Creature c){
        c.order = o;
        o.taken = true;
        System.out.println(c+" took order "+o);
    }

    public void setOrderDone(Order o, Creature c){
        order.remove(o);
        c.order = null;
		for (int i=0; i<order.size(); ++i)
			order.get(i).declined = 0;
        System.out.println(c+" succesfuly did order "+o);
    }

	public void setOrderDeclined(Order o, Creature c){
		c.declinedOrders.add(o);
		c.order = null;
		o.declined++;
		System.out.println(c+" declined  order "+o);
	}

    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
        System.out.println(c+" aborted order "+o);
    }
}
