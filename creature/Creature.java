package creature;

import world.*;
import player.*;

import java.util.Stack;
import stereometry.Point;

public class Creature extends Entity {
    World w;
    public Block b;
    public Stack<Block> path;
    Point np;
    double speed;
    public Player owner;
    public Order order;
    public boolean capable[];

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
        this.capable = new boolean[]{false, false, false};
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

	public void draw(){
	}
}

