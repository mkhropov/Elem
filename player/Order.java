package player;

import world.Block;
import world.World;
import physics.material.*;
import creature.*;
import item.ItemTemplate;
import iface.CommandCube;
import iface.Interface;

public class Order {
    public boolean taken;
    public Block b;
	public CommandCube cube;
    public ItemTemplate it;
	public int m; //material code
	public int f; //form code
	public int d; //direction code
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
		this.m = Interface.getInstance().getBuildMaterial();
		this.f = (type == ORDER_DIG)?(Interface.getInstance().getDigForm()):
				(Interface.getInstance().getBuildForm());
		this.d = Interface.getInstance().getDirection();
		if ((type == ORDER_DIG) || (type == ORDER_BUILD))
			this.cube = new CommandCube(type, b.x, b.y, b.z);
    }

    public boolean capable(Creature c){
//		System.out.println(it);
        if (c.capable.length < ORDER_MAX){
            System.out.printf("Creature capablities not updated\n");
            return false;
        }
		if (type == ORDER_BUILD)
			return it.suits(c.item) && c.capable[ORDER_BUILD];
        return c.capable[type];
    }
}
