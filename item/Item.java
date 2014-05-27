package item;

import core.Data;
import creature.Creature;
import graphics.Renderer;
import world.Block;
import world.Entity;
import world.World;
import stereometry.Point;

public class Item extends Entity{
	Block b;
	Creature c;
	public boolean marked;
	public ItemTemplate type;

	public Item(Block b, ItemTemplate type){
		super(b);
		this.b = b;
		this.c = null;
		this.type = type;
		this.marked = false;
		this.mid = Data.Models.getId(type.model);
		this.gsid = Data.Textures.getId(type.texture);
		Renderer.getInstance().addEntity(this);
	}

	public Item(int x, int y, int z, ItemTemplate type){
		this(World.getInstance().getBlock(x, y, z), type);
	}

	public Item(Creature c, ItemTemplate type){
		super();
		this.b = null;
		this.c = c;
		this.type = type;
		this.marked = false;
		this.mid = Data.Models.getId(type.model);
		this.gsid = Data.Textures.getId(type.texture);
	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	public void setP(Point np){
		this.b = World.getInstance().getBlock(np);
		this.p = new Point(np);
	}

	public boolean isIn(int x, int y, int z){
		return (b.x==x && b.y==y && b.z==z);
	}

	public boolean isIn(Block t){
		return (b.x==t.x && b.y==t.y && b.z==t.z);
	}

	public void update(){
		World w = World.getInstance();
		if (b!=null){
			int i = b.z;
			while (i > 0 && !w.hasSolidFloor(b.x, b.y, i)){
				--i;
			}
			if (i!=b.z){
				b = w.getBlock(b.x, b.y, i);
				setP(b);
			}
		}
	}
}
