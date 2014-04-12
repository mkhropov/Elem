package creature;

import world.*;
import player.*;
import graphics.Renderer;
import physics.material.Material;
import item.Item;
import item.ItemTemplate;

import java.util.Stack;
import stereometry.Point;
import stereometry.Vector;

import static creature.Action.*;

public class Creature extends Entity {
    Point np;
    double speed;
	Vector mv;

	public double digStrength;

	public Item item; //hand-held

    public Player owner;
    public Order order;
    public boolean capable[];
	public Stack<Action> plans;
	Action act;
	long action_t;

	public void start_action(Action action, boolean forced){
		System.out.println("new action "+(int)action.type+" for "+this);
		if (forced && act!=null)
			plans.push(act);
		act = action;
		action_t = 0;
		switch(act.type){
		case ACTION_MOVE:
		case ACTION_DIG:
		case ACTION_BUILD:
			np = new Point(act.xd, act.yd, act.zd);
			setDest(np);
			break;
		case ACTION_FALL:
			mv.toZero();
			break;
		case ACTION_NONE:
		case ACTION_TAKE:
		case ACTION_DROP:
		default:
			break;
		}
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
		switch(act.type){
		case ACTION_TAKE:
			res = take(act.it);
			break;
		case ACTION_DROP:
			res = drop();
			break;
		case ACTION_DIG:
			res = dig(order);
			break;
		case ACTION_BUILD:
			res = build(order);
			break;
		case ACTION_FALL:
		case ACTION_MOVE:
		case ACTION_NONE:
		default:
			res = true;
			break;
		}
		act = null;
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
		World w = World.getInstance();
		int i = (int)p.z;
		while (w.isEmpty((int)p.x, (int)p.y, i))
			i--;
		i++;
		p.add(mv, dT/1000.);
//		System.out.println(p.z);
		mv.add(World.getInstance().gravity, dT);
		if (p.z<0. || p.z<i){
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
		this.digStrength = Material.HARD_STEEL;
        this.capable = new boolean[]{false, false, false, false};
		Renderer.getInstance().addEntity(this);
		start_action(new Action(ACTION_FALL), true);
    }

	public Creature(){
		this.capable = new boolean[]{false, false, false, false};
	}

	public boolean capableOf(Order o){
		return ((order == null) && (capable[o.type]));
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
		return (b.m != Material.MATERIAL_NONE) &&
			(World.getInstance().material[b.m].digTime(digStrength) > 0.);
	}

	public boolean take(ItemTemplate it){
		Item i;
		World w = World.getInstance();
		int k;
		for (k=0; k < w.item.size(); ++k){
			i = w.item.get(k);
			if (it.suits(i) && i.isIn(World.getInstance().getBlock(p))){
				System.out.println("Item "+i+" found!");
				item = i;
				World.getInstance().item.remove(i);//EventHandler.getInstance().removeEntity(i);
				Renderer.getInstance().removeEntity(i);
				return true;
			}
		}
		return false;
	}

	public boolean drop(){
		if (item == null)
			return false;
		World.getInstance().item.add(item);
		Renderer.getInstance().addEntity(item);
		item = null;
		return true;
	}

    public boolean dig(Order o){
		Block b = o.b;
        if (!canReach(b))
            return false;
        else {
			if (canDig(b)){
				int m = World.getInstance().getMaterialID(b.x, b.y, b.z);
				World.getInstance().destroyBlock(b);
				if (o.f!=World.FORM_BLOCK){
					World.getInstance().setMaterial(b.x, b.y, b.z, m);
					World.getInstance().setForm(b.x, b.y, b.z, o.f);
					World.getInstance().setDirection(b.x, b.y, b.z, o.d);
				}
				return true;
			}
			return false;
        }
    }

    public final boolean build(Order o){
		Block b = o.b;
		int m = o.m;
        if ((!canReach(b)) || (b.m != Material.MATERIAL_NONE) ||
			!((item.type == Item.TYPE_BUILDABLE) && (item.m == o.m)))
            return false;
        else {
            World.getInstance().setMaterial(b.x, b.y, b.z, o.m);
            World.getInstance().setForm(b.x, b.y, b.z, o.f);
            World.getInstance().setDirection(b.x, b.y, b.z, o.d);
			item = null;
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
		if (!(new Vector(p, np)).isZero())
			a = (float)(Math.signum(np.x-p.x)*Math.acos((np.y-p.y)/Math.sqrt((np.x-p.x)*(np.x-p.x)+(np.y-p.y)*(np.y-p.y))));
	}

	public void turn(double x, double y, double z){
		Point np = new Point(x, y, z);
		if (!(new Vector(p, np)).isZero())
			a = (float)(Math.signum(np.x-p.x)*Math.acos((np.y-p.y)/Math.sqrt((np.x-p.x)*(np.x-p.x)+(np.y-p.y)*(np.y-p.y))));
	}

    public void iterate(long dT){
		if (act != null){
			if (!exec_action(dT))
				return;
			if (!end_action() && order!=null){
				plans.clear();
				if (order.type == Order.ORDER_BUILD)
					start_action(new Action(ACTION_DROP), false);
				owner.setOrderCancelled(order, this);
				return;
			}
		}

		if (plans.size()!=0){
			start_action(plans.remove(0), false);
			return;
		} else 
			plans.clear();

		if (order != null)
			owner.setOrderDone(order, this);
    }

	public void update(){
		start_action(new Action(ACTION_FALL), true);
	}
}
