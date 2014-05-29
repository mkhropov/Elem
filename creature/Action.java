package creature;

import item.ItemReservation;
import item.ItemTemplate;
import world.Block;
import world.World;

public class Action {
	public static final char ACTION_NONE = 0;
	public static final char ACTION_MOVE = 1;
	public static final char ACTION_TAKE = 2;
	public static final char ACTION_DROP = 3;
	public static final char ACTION_DIG = 4;
	public static final char ACTION_BUILD = 5;
	public static final char ACTION_FALL = 6;
	public static final char ACTION_MAX = 7;

	public char type;

	//public String itemCondition;
	public ItemReservation IR;
	public int f, d, m;
//	public CreatureTemplate ct;
	public double xd, yd, zd;
	public int x, y, z;
	public Block b;

	public Action(char type){
		assert((type==ACTION_FALL)||(type==ACTION_NONE)||(type==ACTION_DROP));
		this.type = type;
	}

	public Action(char type,  ItemReservation IR){
		assert(type == ACTION_TAKE);
		this.type = type;
		//this.itemCondition = itemCondition;
		this.IR = IR;
	}

	public Action(char type, double x, double y, double z){
		assert(type==ACTION_MOVE);
		this.type = type;
		this.xd = x; this.yd = y; this.zd = z;
		this.x = (int)xd; this.y = (int)yd; this.z = (int)zd;
		this.b = World.getInstance().getBlock(this.x, this.y, this.z);
	}

	public Action(char type, int x, int y, int z, int form, int direction){
		assert(type==ACTION_DIG);
		this.type = type;
		this.f = form;
		this.d = direction;
		this.x = x; this.y = y; this.z = z;
		this.xd = x; this.yd = y; this.zd = z;
		this.b = World.getInstance().getBlock(this.x, this.y, this.z);
	}

	public Action(char type, int x, int y, int z, int form, int direction, int matID){
		assert(type==ACTION_BUILD);
		this.type = type;
		this.f = form;
		this.d = direction;
		this.m = matID;
		this.x = x; this.y = y; this.z = z;
		this.xd = x; this.yd = y; this.zd = z;
		this.b = World.getInstance().getBlock(this.x, this.y, this.z);
	}
}
