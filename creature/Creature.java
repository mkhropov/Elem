package creature;

import world.*;

import java.util.Stack;
import stereometry.Point;

public class Creature {
    World w;
    public Block b;
    public Stack<Block> path;
    Point p, np;
    double speed;

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
    }

    public boolean canWalk(Block b){
        return true;
    }

    public boolean canMove(Block b1, Block b2){
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

