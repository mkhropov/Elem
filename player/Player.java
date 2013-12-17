package player;

import java.util.ArrayList;
import world.*;
import creature.*;

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
        order.add(new Order(b, 0));
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
        System.out.println(c+" succesfuly did order "+o);
    }

    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
        System.out.println(c+" aborted order "+o);
    }
}
