package player;

import world.Block;
import physics.material.*;
import creature.*;
import item.ItemTemplate;

public class Order {
    public boolean taken;
    public Block b;
    public ItemTemplate it;
	public Material m;
//	public CreatureTemplate ct;
	public static final int ORDER_MOVE  = 0;
	public static final int ORDER_DIG   = 1;
	public static final int ORDER_BUILD = 2;
	public static final int ORDER_TAKE  = 3;
    static public final int ORDER_MAX   = 4;
    public int type;
    public int declined;

    public Order(Block b, int type){
        this.b = b;
        this.taken = false;
        this.type = type;
        this.declined = 0;
    }

    public boolean capable(Creature c){
		System.out.println(it);
        if (c.capable.length < ORDER_MAX){
            System.out.printf("Creature capablities not updated\n");
            return false;
        }
		if (type == ORDER_BUILD)
			return it.suits(c.item) && c.capable[ORDER_BUILD];
        return c.capable[type];
    }
}
