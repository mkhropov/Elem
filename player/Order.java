package player;

import java.util.Stack;

import world.World;
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
    public boolean declined;

	public Stack<Action> path;

    public Order(Block b, int type){
        this.b = b;
        this.taken = false;
        this.type = type;
		this.m = Interface.getInstance().getBuildMaterial();
		this.f = (type == ORDER_DIG)?(Interface.getInstance().getDigForm()):
				(Interface.getInstance().getBuildForm());
		this.d = Interface.getInstance().getDirection();
        this.declined = !is_accesible();
		this.path = new Stack<>();
		System.out.println("New order "+this+", "+this.declined+" @("+b.x+","+b.y+","+b.z+")");
		if ((type == ORDER_DIG) || (type == ORDER_BUILD))
			this.cube = new CommandCube(type, b.x, b.y, b.z);
    }

	public boolean is_accesible(){
		Block t;
		for (int i=0; i<18; ++i){
			t = World.getInstance().getBlock(
						b.x+Block.nearInd[i][0],
						b.y+Block.nearInd[i][1],
						b.z+Block.nearInd[i][2]);
			if (t!=null && t.m == Material.MATERIAL_NONE)
				return true;
		}
		return false;
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

	public void dumpPath(){
		Action a;
		String[] acodes = new String[]{"ACTION_NONE",
									   "ACTION_MOVE",
									   "ACTION_TAKE",
									   "ACTION_DROP",
									   "ACTION_DIG",
									   "ACTION_BUILD"};
		for (int i=0; i<path.size(); ++i){
			a = path.get(i);
			if (a.b != null)
				System.out.println(acodes[a.type]+" to ("+a.b.x+", "+a.b.y+", "+a.b.z+")");
			else
				System.out.println(acodes[a.type]);
		}
	}
}
