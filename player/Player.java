package player;

import java.util.ArrayList;
import java.util.Stack;
import graphics.Renderer;
import world.*;
import creature.*;
import physics.Material;
import item.Item;
import item.ItemTemplate;
import physics.magics.*;
import pathfind.*;

public class Player {
    public ArrayList<Order> order;
    public ArrayList<Creature> creature;
	public ArrayList<Spell> spellbook;
	public char[][][] blockMeta; //For now FOW only, 7 bits free
	public final static char META_FOW = 1;
	public int mana;
	int free_workers;

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
					if (!w.isEmpty(i,j,k))
						break;
				}
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i=0; i<w.xsize; i++)
			for (int j=0; j<w.ysize; j++)
			for (int k=w.zsize-1; k>=0; k--) {
				if(!blockKnown(i,j,k)){
					for (int dx=-1; dx<=1; dx++)
					for (int dy=-1; dy<=1; dy++)
					if (blockKnown(i+dx, j+dy, k)&&w.isAir(i+dx, j+dy, k)) {
						blockMeta[i][j][k] |= META_FOW;
						changed = true;
					}
					if ((blockKnown(i, j, k-1) && w.isEmpty(i,j,k-1))
							||(blockKnown(i, j, k+1) && w.isEmpty(i,j,k+1))){
						blockMeta[i][j][k] |= META_FOW;
						changed = true;
					}
				}
			}
		}
		spellbook.add(0, new SpellSummon(this));
		this.mana = 0;
		this.free_workers = 0;
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
		ArrayList<Order> toDestroy = new ArrayList<>();
		blockMeta[x][y][z] |= META_FOW;
		if (!World.getInstance().isFull(x, y, z))
			for (Order o: order)
				if (o.b.x==x &&
					o.b.y==y &&
					o.b.z==z &&
					o.type==Order.ORDER_DIG &&
					(World.getInstance().isAir(x, y, z) ||
						o.f==World.FORM_FLOOR))
					toDestroy.add(o);
		for (Order o: toDestroy)
			destroyOrder(o);
		if (!toDestroy.isEmpty())
			updateOrders();
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
		World w = World.getInstance();
		if (!w.isEmpty((int)c.p.x, (int)c.p.y, (int)c.p.z))
			return;
        w.creature.add(c);
        creature.add(c);
        c.owner = this;
		free_workers++;

		updateOrders();
	}

	public void updateOrders(){
		for (int i=0; i<order.size(); ++i)
			order.get(i).declined = !order.get(i).isAccesible();
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

    public void placeBuildOrder(Block b, int m){
		if ((b.m != Material.MATERIAL_NONE)&&
				!((m==b.m) && (World.getInstance().getForm(b.x, b.y, b.z)==World.FORM_FLOOR)))
			return;
		if (blockAlreadyRequested(b))
			return;
		ItemTemplate it = new ItemTemplate(Item.TYPE_BUILDABLE, m);
	    Order o = new Order(b, Order.ORDER_BUILD); o.it = it; o.m = m;
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
		int i = -1;
		Order o;
		Condition c1, c2;
		Block b;
		Stack<Action> path;
		ArrayList<Creature> candidates;
		Pathfinder p = Pathfinder.getInstance();
		p.resetDepth();
		Elem e = new Elem();
//		System.out.println("Iterating player...");
		while((free_workers>0) && (p.getDepth()<500) && (++i<order.size())){
//			System.out.println("Order #"+i);
			o = order.get(i);
			if (o.taken || o.declined)
				continue;
			o.path.clear();
			switch (o.type) {
			case (Order.ORDER_BUILD):
				b = o.b;
				c1 = new ConditionReach(b, e);
				c2 = new ConditionItem(o.it);
				path = p.getPath(e, b, c1, c2);
				if (path==null){
					o.declined = true;
//					System.out.println("Build order "+o+" declined at buildable search");
					continue;
				}

				o.path.add(0, new Action(Action.ACTION_BUILD, b.x, b.y, b.z, o.f, o.d, o.m));
				b = path.remove(0).b;
				o.path.addAll(0, path);

				c1 = new ConditionBeIn(b);
				c2 = new ConditionWorker(o);
				path = p.getPath(e, b, c1, c2);
				if (path==null){
					o.declined = true;
//					System.out.println("Build order "+o+" declined at worker search");
					continue;
				}
				candidates = World.getInstance().getCreature(path.remove(0).b);
				o.path.add(0, new Action(Action.ACTION_TAKE, o.it));
				o.path.addAll(0, path);
				for (Creature c: candidates){
					if (c.capableOf(o)) {
						setOrderAssigned(o, c);
						break;
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
//					System.out.println("Dig order "+o+" declined at worker search");
					continue;
				}
				candidates = World.getInstance().getCreature(path.remove(0).b);
				o.path.add(0, new Action(Action.ACTION_DIG, b.x, b.y, b.z, o.f, o.d));
				o.path.addAll(0, path);
				for (Creature c: candidates){
					if (c.capableOf(o)) {
						setOrderAssigned(o, c);
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	}


/* order-creature interaction */

	public void setOrderAssigned(Order o, Creature c){
		c.order = o;
		c.plans = o.path;

		o.taken = true;
		o.worker = c;

		free_workers--;
	}

    public void setOrderDone(Order o, Creature c){
		destroyOrder(o);

        c.order = null;

		updateOrders();
		free_workers++;
//        System.out.println(c+" succesfuly did order "+o);
    }

    public void setOrderCancelled(Order o, Creature c){
        c.order = null;

        o.taken = false;
		o.worker = null;

		free_workers++;
  //      System.out.println(c+" aborted order "+o);
    }


	public void destroyOrder(Order o) {
		if ((o.type == Order.ORDER_BUILD) ||
			(o.type == Order.ORDER_DIG))
			Renderer.getInstance().removeEntity(o.cube);
		order.remove(o);
	}

	public void cancelOrders(Block b){
		Order o;
		for (int i=0; i<order.size(); ++i) {
			o = order.get(i);
			if ((o.b != null) && (o.b.isSame(b))){
				if (o.taken)
					o.worker.cancelOrder();
				destroyOrder(o);
			}
		}
		updateOrders();
	}
}
