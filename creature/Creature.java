package creature;

import static creature.Action.ACTION_BUILD;
import static creature.Action.ACTION_DIG;
import static creature.Action.ACTION_DROP;
import static creature.Action.ACTION_FALL;
import static creature.Action.ACTION_MOVE;
import static creature.Action.ACTION_NONE;
import static creature.Action.ACTION_TAKE;
import core.Data;
import graphics.Renderer;
import iface.FloatingText;
import item.Item;
import item.ItemBoulder;
import java.util.Stack;
import player.Order;
import player.Player;
import stereometry.Point;
import stereometry.Vector;
import world.Block;
import world.Entity;
import world.World;
import java.util.ArrayList;

public class Creature extends Entity {
    Point np;
    double speed;
	Vector mv;

	public double digStrength;

	public ArrayList<Item> item; //hand-held
	public int capacity = 20;
	private FloatingText bubble;

    public Player owner;
    public Order order;
    public boolean capable[];
	public Stack<Action> plans;
	Action act;
	long action_t;

	public boolean start_action(Action action, boolean forced){
		boolean res = true;
//		System.out.println("new action "+(int)action.type+" for "+this);
		World w = World.getInstance();
		if (forced && act!=null)
			plans.push(act);
		act = action;
		action_t = 0;
		switch(act.type){
		case ACTION_MOVE:
			np = new Point(act.xd, act.yd, act.zd);
			setDest(np);
			if (!this.canMove(w.getBlock(p), w.getBlock(act.x, act.y, act.z)))
					res = false;
			break;
		case ACTION_DIG:
		case ACTION_BUILD:
			np = new Point(act.xd, act.yd, act.zd);
			setDest(np);
			if (!this.canReach(w.getBlock(p), w.getBlock(act.x, act.y, act.z)))
					res = false;
			break;
		case ACTION_FALL:
			bubble = new FloatingText("", this);
			bubble.ttl = 10000;
			Renderer.getInstance().addFT(bubble);
			mv.toZero();
			break;
		case ACTION_TAKE:
			if (item!=null)
				start_action(new Action(ACTION_DROP), true);
			break;
		case ACTION_NONE:
		case ACTION_DROP:
		default:
			break;
		}
		return res;
	}

	public boolean exec_action(long dT){
		boolean res;
		action_t += dT;
		switch(act.type){
		case ACTION_NONE:
			res = action_idle(dT);
			break;
		case ACTION_MOVE:
			res = action_fly(dT);
			break;
		case ACTION_TAKE:
			res = action_take(dT);
			break;
		case ACTION_DROP:
			res = action_drop(dT);
			break;
		case ACTION_DIG:
			res = action_dig(dT);
			break;
		case ACTION_BUILD:
			res = action_build(dT);
			break;
		case ACTION_FALL:
			res = action_fall(dT);
			break;
		default:
			res = true;
			break;
		}
		return res;
	}

	public boolean end_action(){
		boolean res;
		Action action = act;
		act = null;
		switch(action.type){
		case ACTION_TAKE:
			res = take(action);
			break;
		case ACTION_DROP:
			res = drop();
			break;
		case ACTION_DIG:
			res = dig(action);
			break;
		case ACTION_BUILD:
			res = build(action);
			break;
		case ACTION_FALL:
			owner.updateOrders();
			res = true;
			break;
		case ACTION_MOVE:
		case ACTION_NONE:
		default:
			res = true;
			break;
		}
		return res;
	}

	boolean action_idle(long dT){
		return true;
	}

	boolean action_take(long dT){
		long T = 100;
		a += Math.PI*2*dT/T;
//		p.z -= .1f*((float)Math.sin(((double)action_t)*2*Math.PI/T));
		return (action_t >= T);
	}

	boolean action_drop(long dT){
		return true;
	}

	boolean action_build(long dT){
		long T = 400;
//		p.add(mv, dT*.1*(Math.sin(((double)action_t)*8*Math.PI/T)));
		return(action_t >= T);
	}

	boolean action_dig(long dT){
		long T = 400;
//		p.add(mv, dT*.1*(Math.sin(((double)action_t)*8*Math.PI/T)));
		return(action_t >= T);
	}

	boolean action_fall(long dT){
		if (bubble != null){
			if ((action_t-dT % 100) != (action_t % 100))
				bubble.s = bubble.s+"a";
			bubble.ttl += dT;
		}
		World w = World.getInstance();
		int i = (int)p.z;
		while (!w.hasSolidFloor((int)p.x, (int)p.y, i))
			i--;
		p.add(mv, dT/1000.);
		mv.add(World.getInstance().gravity, dT);
		if (p.z<0. || p.z<i){
			if (bubble != null){
				if (mv.len()>300.)
					bubble.s = bubble.s+"ARGH!";
				else
					bubble.s = bubble.s+"A!";
				bubble.ttl = 1000;
			}
			mv.toZero();
			p.z = Math.max(i, 1);
			return true;
		} else
			return false;
	}

	boolean action_fly(long dT){
		p.add(mv, dT);
		if (p.dist(np) < speed*dT) {
			p = np;
			return true;
		} else
			return false;
	}

    public Creature(Block b){
        this.p = new Point(b);
		this.np = p;
		this.mv = new Vector(p, np);
		this.plans = new Stack<>();
		this.digStrength = 400.;
        this.capable = new boolean[]{false, false, false, false};
		this.item = new ArrayList<>();
		Renderer.getInstance().addEntity(this);
		if (!World.getInstance().hasSolidFloor((int)p.x, (int)p.y, (int)p.z))
			start_action(new Action(ACTION_FALL), true);
    }

	public Creature(){
		this.capable = new boolean[]{false, false, false, false};
		this.bubble = null;
	}

	public boolean capableOf(Order o){
		if ((order != null) || (!capable[o.type]))
			return false;
		return (o.type != Order.ORDER_DIG) ||
				canDig(o.b);
	}

	public boolean isIn(int x, int y, int z){
//		System.out.println("Is ("+p.x+","+p.y+","+p.z+" in ("+(int)p.x+","+(int)p.y+","+(int)p.z+")?");
		return (x==(int)p.x && y==(int)p.y && z==(int)p.z);
	}

    public boolean canWalk(Block b){
        return true;
    }

    public boolean canMove(Block b1, Block b2){
        return true;
    }

    public boolean canReach(Block v1, Block b2){
        return true;
    }

	public boolean canReach(Block b){
		return canReach(World.getInstance().getBlock(p), b);
	}

	public boolean canDig(Block b){
		return (b.m != Data.Materials.getId("air")) &&
			(Data.Materials.get(b.m).digTime(digStrength) > 0.);
	}

	public boolean take(Action action){
		System.out.println("Trying to take something...");
		Item i;
		World w = World.getInstance();
		int k;
		for (k=0; k < w.item.size(); ++k){
			i = w.item.get(k);
			if (action.it.suitsMarked(i) && i.isIn(World.getInstance().getBlock(p))){
				System.out.println("Item "+i+" found!");
				i.unmark();
				item.add(i);
				World.getInstance().item.remove(i);//EventHandler.getInstance().removeEntity(i);
				Renderer.getInstance().removeEntity(i);
				return true;
			}
		}
		return false;
	}

	public boolean drop(){
		if (item.size() == 0)
			return false;
		for (Item i: item) {
			i.setP(this.p);
			World.getInstance().item.add(i);
			Renderer.getInstance().addEntity(i);
		}
		item.clear();
		return true;
	}

    public boolean dig(Action action){
		Block b = action.b;
        if (canReach(b) && canDig(b)){
			World w = World.getInstance();
			owner.addBlockKnown(b.x, b.y, b.z);
			int m = w.getMaterialID(b.x, b.y, b.z);
			switch(w.getForm(b.x, b.y, b.z)){
			case World.FORM_FLOOR:
				if (action.f == World.FORM_BLOCK){
					w.setMaterialID(b.x, b.y, b.z, Data.Materials.getId("air"));
					w.setForm(b.x, b.y, b.z, World.FORM_BLOCK);
					w.setDirection(b.x, b.y, b.z, 0);
					w.updateEntities(b.x, b.y, b.z);
				}
				break;
			case World.FORM_BLOCK:
				w.disassembleBlock(b.x, b.y, b.z);
				if (action.f == World.FORM_FLOOR)
					w.setMaterialID(b.x, b.y, b.z, m);
				else
					w.setMaterialID(b.x, b.y, b.z, Data.Materials.getId("air"));
				w.setForm(b.x, b.y, b.z, action.f);
				w.setDirection(b.x, b.y, b.z, action.d);
				w.updateEntities(b.x, b.y, b.z+1);
				break;
			default:
				break;
			}
			w.updateBlock(b.x, b.y, b.z);
			return true;
        }
		return false;
    }

    public final boolean build(Action action){
		Block b = action.b;
		int m = action.m;
        if ((!canReach(b)) || ((b.m != Data.Materials.getId("air")) &&
			!((m==b.m) && (World.getInstance().getForm(b.x, b.y, b.z)==World.FORM_FLOOR))) ||
			!((item.get(0).type == Item.TYPE_BUILDABLE) && (item.get(0).m == m) && item.size()>=8))
            return false;
        else {
            World.getInstance().setMaterialID(b.x, b.y, b.z, m);
            World.getInstance().setForm(b.x, b.y, b.z, action.f);
            World.getInstance().setDirection(b.x, b.y, b.z, action.d);
			for (int i=0; i<8; ++i)
				item.remove(0);
            Renderer.getInstance().updateBlock(b.x, b.y, b.z);
            return true;
        }
    }

	public void setDest(Point np){
		this.np = np;
		turn(np);
		mv = new Vector(p, np);
		mv.normalize(); mv.scale(speed);
	}

	public void turn(Point np){
		if (p.distProj(np)>.1)
			a = (float)Math.atan2(np.x-p.x, np.y-p.y);
	}

	public void turn(double x, double y, double z){
		Point np = new Point(x, y, z);
		if (p.distProj(np)>.1)
			a = (float)Math.atan2(np.x-p.x, np.y-p.y);
	}

	public void cancelOrder(){
		plans.clear();
		if (order != null) {
			if (order.type == Order.ORDER_BUILD)
				plans.add(new Action(ACTION_DROP));
			owner.setOrderCancelled(order, this);
		}
	}

	public void iterate(long dT){
		if (bubble != null){
			bubble.ttl -= dT;
			if (bubble.ttl < 0){
				Renderer.getInstance().removeFT(bubble);
				bubble = null;
			}
		}

		if (act != null){
			if (!exec_action(dT))
				return;
			if (!end_action() && order!=null){
				cancelOrder();
				return;
			}
		}

		if (!plans.empty()){
			if (!start_action(plans.remove(0), false)
					&& order!=null)
				cancelOrder();
			return;
		}

		if (order != null)
			owner.setOrderDone(order, this);
    }

	public void update(){
		if (!World.getInstance().hasSolidFloor((int)p.x, (int)p.y, (int)p.z))
			start_action(new Action(ACTION_FALL), true);
	}

	@Override
	public void draw(){
		super.draw();
		if (item.size() > 0){
			Data.Models.get(item.get(0).mid).draw(
			(float)(p.x+.5*Math.sin(a)), (float)(p.y+.5*Math.cos(a)), (float)(p.z+.3), a,
			Data.Textures.get(item.get(0).gsid));
		}
	}
}
