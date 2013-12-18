package player;

import java.util.ArrayList;
import world.*;
import creature.*;
import physics.material.*;

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
        Order o = new Order(b, Order.ORDER_PLACE);
        o.m = m;
        order.add(o);
    }

    public void setOrderTaken(int i, Creature c){
        Order o = order.get(i);
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

	public void setOrderDeclined(int i, Creature c){
        Order o = order.get(i);
		c.declinedOrders.add(o);
		o.declined++;
	}
	
    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
        System.out.println(c+" aborted order "+o);
    }
}
