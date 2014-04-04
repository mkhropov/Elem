package player;

import java.util.ArrayList;
import graphics.Renderer;
import world.*;
import creature.*;
import physics.material.*;
import item.Item;
import item.ItemTemplate;
import physics.magics.*;

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
        World.getInstance().creature.add(c);
        creature.add(c);
        c.owner = this;
    }

    public void placeMoveOrder(Block b){
        order.add(new Order(b, Order.ORDER_MOVE));
    }

    public void placeDigOrder(Block b){
		Order o = new Order(b, Order.ORDER_DIG);
        order.add(o);
		Renderer.getInstance().addEntity(o.cube);
    }

    public void placeBuildOrder(Block b, char m){
		ItemTemplate it = new ItemTemplate(Item.TYPE_BUILDABLE, m);
		Order o = new Order(null, Order.ORDER_TAKE); o.it = it;
		order.add(o);
        o = new Order(b, Order.ORDER_BUILD); o.it = it; o.m = m;
        order.add(o);
		Renderer.getInstance().addEntity(o.cube);
    }

    public void setOrderTaken(Order o, Creature c){
        c.order = o;
        o.taken = true;
        System.out.println(c+" took order "+o);
    }

    public void setOrderDone(Order o, Creature c){
		if ((o.type == Order.ORDER_BUILD) ||
			(o.type == Order.ORDER_DIG))
			Renderer.getInstance().removeEntity(o.cube);
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
