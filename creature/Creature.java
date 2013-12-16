package creature;

import world.*;

import java.util.Stack;

public class Creature {
    World w;
    public Block b;
    public Stack<Block> path;

    public Creature(World w, Block b){
//        w.creature.add(this);
        b.creature.add(this);
        this.b = b;
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
            this.b.creature.remove(this);
            b.creature.add(this);
            this.b = b;
            return true;
        }
    }

    public void iterate(){
    }

	public void draw(){
	}
}

