package player;

import world.Block;

public class Order {
    public boolean taken;
    public Block b;
    public Order(Block b){
        this.b = b;
        this.taken = false;
    }
}
