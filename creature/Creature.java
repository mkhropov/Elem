package creature;

import java.util.ArrayList;
import world.*;
import player.*;
import physics.material.Material;
import item.Item;
import item.ItemTemplate;

import java.util.Stack;
import stereometry.Point;

public class Creature extends Entity {
    public World w;
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
        if (this.b != null)
            this.b.creature.remove(this);
        this.b = b;
        this.b.creature.add(this);
        if (adjustPoint){
            this.p = np;
            this.np = new Point(b);
        }
    }

    public Creature(World w, Block b){
//        w.creature.add(this);
        setBlock(b, true);
        this.p = this.np;
        this.w = w;
		this.digStrength = Material.HARD_STEEL;
        this.capable = new boolean[]{false, false, false, false};
		w.rend.addEntity(this);
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
		return (b.m.digTime(digStrength) > 0.);
	}

	public boolean take(ItemTemplate it){
		Item i;
		int k;
		for (k=0; k<b.item.size(); ++k){
			i = b.item.get(k);
			if (it.suits(i)){
				item = i;
				b.item.remove(i);
				break;
			}
		}
		return (k != b.item.size());
	}

    public boolean move(Block b) {
        if (!canMove(this.b, b))
            return false;
        else {
            setBlock(b, true);
            return true;
        }
    }

    public void iterate(long dT){
    }

	public void update(){
	}

	public void draw(){
	}
}

