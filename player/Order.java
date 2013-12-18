package player;

import world.Block;
import physics.material.*;
import creature.*;

public class Order {
    public boolean taken;
    public Block b;
    public Material m;
	public static final ORDER_MOVE;
	public static final ORDER_DIG;
	public static final ORDER_PLACE;
    public int type;
    static public int TYPE_MAX = 3;

    public Order(Block b, int type){
        this.b = b;
        this.taken = false;
        this.type = type;
    }

    public boolean capable(Creature c){
        if (c.capable.length < TYPE_MAX){
            System.out.printf("Creature capablities not updated\n");
            return false;
        }
        return c.capable[this.type];
    }
}
