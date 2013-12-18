package player;

import world.Block;
import physics.material.*;
import creature.*;

public class Order {
    public boolean taken;
    public Block b;
    public Material m;
	public static final int ORDER_MOVE=0;
	public static final int ORDER_DIG=1;
	public static final int ORDER_PLACE=2;
    public int type;
    public int declined;
    static public int TYPE_MAX = 3;

    public Order(Block b, int type){
        this.b = b;
        this.taken = false;
        this.type = type;
        this.declined = 0;
    }

    public boolean capable(Creature c){
        if (c.capable.length < TYPE_MAX){
            System.out.printf("Creature capablities not updated\n");
            return false;
        }
        return c.capable[this.type];
    }
}
