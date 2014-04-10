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
	public char[][][] blockMeta; //For now FOW only, 7 bits free
	public final static char META_FOW = 1;
	public int mana;

    public Player(){
        this.order = new ArrayList<>();
        this.creature = new ArrayList<>();
		this.spellbook = new ArrayList<>();
		World w = World.getInstance();
		this.blockMeta = new char[w.xsize][w.ysize][w.zsize];
		for (int i=0; i<w.xsize; i++)
			for (int j=0; j<w.ysize; j++)
				for (int k=w.zsize-1; k>=0; k--) {
					blockMeta[i][j][k] |= META_FOW;
					if (!w.empty(i,j,k)) break;
				}
		spellbook.add(0, new SpellSummon(this));
		this.mana = 0;
    }

	public boolean blockKnown(int x, int y, int z) {
		if (World.getInstance().isIn(x,y,z)){
			return (blockMeta[x][y][z] & META_FOW) == META_FOW;
		} else {
			return false;
		}
	}

	public void addBlockKnownSingle(int x, int y, int z) {
		if (!World.getInstance().isIn(x,y,z)) return;
		blockMeta[x][y][z] |= META_FOW;
		Renderer.getInstance().updateBlock(x,y,z);
	}

	public void addBlockKnown(int x, int y, int z) {
		addBlockKnownSingle(x,y,z);
		addBlockKnownSingle(x+1,y,z);
		addBlockKnownSingle(x-1,y,z);
		addBlockKnownSingle(x,y-1,z);
		addBlockKnownSingle(x+1,y-1,z);
		addBlockKnownSingle(x-1,y-1,z);
		addBlockKnownSingle(x,y+1,z);
		addBlockKnownSingle(x+1,y+1,z);
		addBlockKnownSingle(x-1,y+1,z);
		addBlockKnownSingle(x,y,z-1);
		addBlockKnownSingle(x,y,z+1);
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
		if (c.b.m != Material.MATERIAL_NONE)
			return;
        World.getInstance().creature.add(c);
        creature.add(c);
        c.owner = this;
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
        order.add(o);
		Renderer.getInstance().addEntity(o.cube);
    }

	public boolean blockAlreadyRequested(Block b){
		for (int i=0; i<order.size(); ++i)
			if ((order.get(i).b != null) && (order.get(i).b.isSame(b)))
				return true;
		return false;
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
			order.get(i).declined = 0;
//        System.out.println(c+" succesfuly did order "+o);
    }

	public void setOrderDeclined(Order o, Creature c){
		c.declinedOrders.add(o);
		c.order = null;
		o.declined++;
	//	System.out.println(c+" declined  order "+o);
	}

    public void setOrderCancelled(Order o, Creature c){
        o.taken = false;
        c.order = null;
      //  System.out.println(c+" aborted order "+o);
    }
}
