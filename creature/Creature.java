package creature;

import java.util.ArrayList;
import world.*;
import player.*;
import graphics.Renderer;
import physics.material.Material;
import item.Item;
import item.ItemTemplate;

import java.util.Stack;
import stereometry.Point;
import stereometry.Vector;

public class Creature extends Entity {
    public Block b;
    public Stack<Block> path;
    Point np;
    double speed;
	public double digStrength;
	public Item item; //hand-held
    public Player owner;
    public Order order;
    public boolean capable[];
	public ArrayList<Order> declinedOrders;
	int action;
	static final int ACTION_NONE = 0;
	static final int ACTION_MOVE = 1;
	static final int ACTION_FALL = 2;

    final public void setBlock(Block b, boolean adjustPoint){
        this.b = b;
        if (adjustPoint){
            this.p = np;
            this.np = new Point(b);
        }
    }

    public Creature(Block b){
        setBlock(b, true);
        this.p = this.np;
		this.digStrength = Material.HARD_STEEL;
        this.capable = new boolean[]{false, false, false, false};
		Renderer.getInstance().addEntity(this);
    }

	public boolean isIn(int x, int y, int z){
		return (x==b.x && y==b.y && z==b.z);
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
			if (it.suits(i) && i.isIn(b)){
				item = i;
				World.getInstance().item.remove(i);//EventHandler.getInstance().removeEntity(i);
				Renderer.getInstance().removeEntity(i);
				break;
			}
		}
		return (k != w.item.size());
	}

	public void drop(){
		if (item == null)
			return;
		World.getInstance().item.add(item);
		Renderer.getInstance().addEntity(item);
		item = null;
	}

    public boolean move(Block b) {
        if (!canMove(this.b, b))
            return false;
        else {
            setBlock(b, true);
            return true;
        }
    }

	public void turn(Point np){
		if (!(new Vector(p, np)).isZero())
			a = (float)(Math.signum(np.x-p.x)*Math.acos((np.y-p.y)/Math.sqrt((np.x-p.x)*(np.x-p.x)+(np.y-p.y)*(np.y-p.y))));
	}

    public void iterate(long dT){
    }

	public void update(){
	}
}
