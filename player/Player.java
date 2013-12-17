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
        order.add(new OrderMove(b));
    }

    public void setOrderTaken(int i, Creature c){
        Order o = order.get(i);
        c.order = o;
        o.taken = true;
    }

    public void setOrderDone(Order o, Creature c){
        order.remove(o);
        c.order = null;
    }

    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
    }
}
