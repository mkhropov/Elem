package player;

import creature.Action;
import creature.Creature;
import iface.CommandCube;
import iface.Interface;
import item.Item;
import item.ItemTemplate;
import java.util.Stack;
import java.util.ArrayList;
import world.Block;
import world.World;

public class Order {
    public boolean taken;
	public Creature worker;
    public Block b;
	public CommandCube cube;
    public ItemTemplate it;
	public int m; //material code
	public int f; //form code
	public int d; //direction code
	public int N; //amount of needed items
	public ArrayList<Item> marked; //for this order
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
        this.declined = !isAccesible();
		this.path = new Stack<>();
		this.marked = new ArrayList<>();
//		System.out.println("New order "+this+", "+this.declined+" @("+b.x+","+b.y+","+b.z+")");
		if ((type == ORDER_DIG) || (type == ORDER_BUILD))
			this.cube = new CommandCube(type, b.x, b.y, b.z);
		this.N = 8;
    }

	public boolean isAccesible(){
		for (int i=0; i<18; ++i){
			if (World.getInstance().isEmpty(
						b.x+Block.nearInd[i][0],
						b.y+Block.nearInd[i][1],
						b.z+Block.nearInd[i][2]))
				return true;
		}
		return false;
	}

// XXX UNUSED XXX
    public boolean capable(Creature c){
//		System.out.println(it);
        if (c.capable.length < ORDER_MAX){
            System.out.printf("Creature capablities not updated\n");
            return false;
        }
		if (type == ORDER_BUILD)
			return it.suits(c.item.get(0)) && c.capable[ORDER_BUILD];
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
