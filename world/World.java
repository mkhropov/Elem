package world;

import core.Data;
import creature.Creature;
import generation.Generator;
import graphics.Renderer;
import iface.Interface;
import item.Inventory;
import item.FallingBlock;
import item.ItemManager;
import java.util.ArrayList;
import physics.Material;
import physics.mana.ManaField;
import player.Order;
import player.Player;
import stereometry.BoundBox;
import stereometry.Point;
import stereometry.Vector;

public class World {
    public static int DEFAULT_XSIZE = 64;
    public static int DEFAULT_YSIZE = 64;
    public static int DEFAULT_ZSIZE = 32;
    public int xsize, ysize, zsize;
    private final int[][][] block;

	public static final int MATERIAL_MASK = 0x03ff;
	public static final int FORM_MASK = 0x1c00;
	public static final int DIRECTION_MASK = 0xe000;

	public static final int FORM_BLOCK = 0x0000;
	public static final int FORM_FLOOR = 0x0400;

	public static final int DIRECTION_DOWN = 0x2000;
	public static final int DIRECTION_UP = 0x4000;
	public static final int DIRECTION_X_PLUS = 0x6000;
	public static final int DIRECTION_EAST = 0x6000;
	public static final int DIRECTION_X_MINUS = 0x8000;
	public static final int DIRECTION_WEST = 0x8000;
	public static final int DIRECTION_Y_PLUS = 0xa000;
	public static final int DIRECTION_NORTH = 0xa000;
	public static final int DIRECTION_Y_MINUS = 0xc000;
	public static final int DIRECTION_SOUTH = 0xc000;

    public ArrayList<Creature> creature;
	public ItemManager items;
	public ArrayList<int[]> updateBlocks;
	public ArrayList<int[]> updateEntities;
	public Vector gravity;
	public BoundBox bb;

	private static World instance = null;

	public static World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

    private World() {
		this.xsize = World.DEFAULT_XSIZE;
		this.ysize = World.DEFAULT_YSIZE;
		this.zsize = World.DEFAULT_ZSIZE;

		this.bb = new BoundBox(0, 0, 0, xsize, ysize, zsize);

		this.gravity = new Vector(0., 0., -4.9);

		System.out.print("Reserving world space ...");
        this.block = new int[xsize][ysize][zsize];
        for (int i=0; i<xsize; i++)
            for (int j=0; j<ysize; j++)
                for (int k=0; k<zsize; k++)
                    block[i][j][k] = Data.Materials.getId("air");
		System.out.print(" done\n");

		this.creature = new ArrayList<>();
		this.items = new ItemManager();

		this.updateBlocks = new ArrayList<>();
		this.updateEntities = new ArrayList<>();
    }

	public int getMaterialID (int x, int y, int z) {
		if (!isIn(x, y, z))
			return Data.Materials.getId("bedrock");
		return block[x][y][z]&MATERIAL_MASK;
	}

	public int getMaterialID (Point p) {
		if (!isIn(p.x, p.y, p.z))
			return Data.Materials.getId("bedrock");
		return block[(int)p.x][(int)p.y][(int)p.z]&MATERIAL_MASK;
	}

	public void setMaterialID (int x, int y, int z, int m) {
		block[x][y][z] = (block[x][y][z]&~MATERIAL_MASK)|m;
	}

	public int getForm (int x, int y, int z) {
		if (!isIn(x, y, z))
			return FORM_BLOCK;
		return block[x][y][z]&FORM_MASK;
	}

	public void setForm (int x, int y, int z, int f) {
		block[x][y][z] = (block[x][y][z]&~FORM_MASK)|f;
	}

	public int getDirection( int x, int y, int z) {
		if (!isIn(x, y, z))
			return 0;
		return block[x][y][z]&DIRECTION_MASK;
	}

	public void setDirection (int x, int y, int z, int d) {
		block[x][y][z] = (block[x][y][z]&~DIRECTION_MASK)|d;
	}

	public boolean hasSolidBorder(int x, int y, int z, int d) {
		if (getMaterialID(x,y,z) == Data.Materials.getId("air")) {
			return false;
		} else {
			switch (getForm(x,y,z)) {
				case FORM_BLOCK: return true;
				case FORM_FLOOR: return (d==DIRECTION_DOWN);
				default: return true;
			}
		}
	}

	public boolean hasSolidFloor(int x, int y, int z){
		return hasSolidBorder(x, y, z, DIRECTION_DOWN)
				|| hasSolidBorder(x, y, z-1, DIRECTION_UP);
	}

	public void init(){
//		ManaField.getInstance().addSource(
//			new ManaSource(blockArray[20][10][10],
//				new Vector(1., 1., 1.),
//				2.));
/*		ManaField.getInstance().addSource(
            new ManaSource(getBlock(10, 20, 15),
				new Vector(1., 1., 1.),
				5.));*/
		Generator.getInstance().generate();
		Generator.getInstance().apply();
	}

    public void iterate(long dT){
		if (Renderer.getInstance().draw_mana)
			ManaField.getInstance().iterate(dT);

		updateAll();

        for (int i=0; i<creature.size(); ++i){
            creature.get(i).iterate(dT);
        }
    }

	public Block getBlock(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return null;
		if ((y<0) || (y>=ysize)) return null;
		if ((z<0) || (z>=zsize)) return null;
		return (new Block(x, y, z));
	}

	public Block getBlock(Point p){
		if ((p.x<0) || (p.x > xsize-1)) return null;
		if ((p.y<0) || (p.y > ysize-1)) return null;
		if ((p.z<0) || (p.z > zsize-1)) return null;
		return (new Block((int)p.x, (int)p.y, (int)p.z));
	}

	public void disassembleBlock(int x, int y, int z) {
		Point p = new Point(x, y, z);
		Material m = Data.Materials.get(getMaterialID(x,y,z));
		Inventory inv = items.getInventory(new Block(x,y,z));
		if (m.dropAmount > 0) {
			inv.addItem(Data.Items.get(m.drop), m.dropAmount);
		}
		for (VeinPatch vp: Data.Veins.asList()) {
			if (vp.removePatch(p)) {
				inv.addItem(Data.Items.get(vp.drop),1);
			}
		}
	}

	public ArrayList<Creature> getCreature(int x, int y, int z){
		ArrayList<Creature> res = new ArrayList<>(0);
		for (int i=0; i<creature.size(); ++i)
			if (creature.get(i).isIn(x, y, z))
				res.add(creature.get(i));
		return res;
	}

	public ArrayList<Creature> getCreature(Block b){
		return getCreature(b.x, b.y, b.z);
	}
	
	public void updateBlock(int x, int y, int z){
		if(!isIn(x, y, z)) return;
		Renderer.getInstance().updateBlock(x, y, z);
		updateBlocks.add(new int[]{x, y, z});
		for (int i=0; i<6; ++i)
			updateBlocks.add(new int[]{
						x+Block.nearInd[i][0],
			            y+Block.nearInd[i][1],
						z+Block.nearInd[i][2]});
		}


	public void updateEntities(int x, int y, int z){
		if(!isIn(x, y, z)) return;
		updateEntities.add(new int[]{x, y, z});
	}

	public void updateAll(){
		ArrayList<int[]> toRemove = new ArrayList<>();
		ArrayList<int[]> toAdd = new ArrayList<>();
		items.update();
		for (int[] p: updateBlocks){
			toRemove.add(p);
//			System.out.println("Checking updated block "+p[0]+" "+p[1]+" "+p[2]);
/*			if (!hasSupport(p[0], p[1], p[2])){
				new FallingBlock(p);
//				System.out.println("A falling block was added");
				setMaterialID(p[0], p[1], p[2], Data.Materials.getId("air"));
				setForm(p[0], p[1], p[2], FORM_BLOCK);
				Renderer.getInstance().updateBlock(p[0], p[1], p[2]);
				for (int i=0; i<6; ++i)
					toAdd.add(new int[]{
								p[0]+Block.nearInd[i][0],
								p[1]+Block.nearInd[i][1],
								p[2]+Block.nearInd[i][2]});
				updateEntities(p[0], p[1], p[2]);
				updateEntities(p[0], p[1], p[2]+1);
			}*/
		}
		updateBlocks.removeAll(toRemove);
		updateBlocks.addAll(toAdd);
		toRemove.clear();
		for (int[] p: updateEntities){
			toRemove.add(p);
			for (Creature c: getCreature(p[0], p[1], p[2]))
				c.update();
		}
		updateEntities.removeAll(toRemove);
	}
/*
	public void updateBlock(Block b){
		updateBlock(b.x, b.y, b.z);
		for (int i=0; i<6; ++i)
			updateBlock(p[0]+Block.nearInd[i][0],
			            p[1]+Block.nearInd[i][1],
						p[2]+Block.nearInd[i][2]);
	}
*/
	public boolean hasSupport(int x, int y, int z){
		int[] s = new int[]{1, 1, 2, 0, 1, 1};
		int tx, ty, tz;
		int m1, f1, s1, m2, f2, s2, bform, bmat;
		int support = 0;
		bform = getForm(x, y, z);
		bmat = getMaterialID(x, y, z);
		for (int i=0; i<6; ++i){
			tx = x+Block.nearInd[i][0];
			ty = y+Block.nearInd[i][1];
			tz = z+Block.nearInd[i][2];
			m1 = getMaterialID(tx, ty, tz);
			f1 = getForm(tx, ty, tz);
			s1 = s[i];
			if (bform==FORM_FLOOR && tz==z){
				m2 = getMaterialID(tx, ty, tz-1);
				f2 = getForm(tx, ty, tz-1);
				s2 = Material.UP;
			} else {
				m2 = m1; f2 = f1; s2 = s1;
			}
			support += Math.max(
				Data.Materials.get(m1).support[f1/0x400][s1],
				Data.Materials.get(m2).support[f2/0x400][s2]);
		}
//		System.out.println("Support is "+support+", weight "+(Material.weight[bform/0x400][bmat]));
		return (Data.Materials.get(bmat).weight[bform/0x400] <= support);
	}

	public boolean isIn(int x, int y, int z){
	    if ((x < 0) || (x >= xsize)) return false;
		if ((y < 0) || (y >= ysize)) return false;
		if ((z < 0) || (z >= zsize)) return false;
		return true;
	}

	public boolean isIn(int x, int y, int z, int[] offset){
		return isIn(x+offset[0], y+offset[1], z+offset[2]);
	}

	public boolean isIn(double x, double y, double z){
	    if ((x < 0) || (x >= xsize)) return false;
		if ((y < 0) || (y >= ysize)) return false;
		if ((z < 0) || (z >= zsize)) return false;
		return true;
	}

	public boolean isEmpty(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return true;
		if ((y<0) || (y>=ysize)) return true;
		if ((z<0) || (z>=zsize)) return true;
		return (getMaterialID(x, y, z) == Data.Materials.getId("air"))
				|| (getForm(x, y, z) == FORM_FLOOR);
	}

	public boolean isAir(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return true;
		if ((y<0) || (y>=ysize)) return true;
		if ((z<0) || (z>=zsize)) return true;
		return (getMaterialID(x, y, z) == Data.Materials.getId("air"));
	}

	public boolean isFull(int x, int y, int z){
		if ((x<0) || (x>=xsize)) return false;
		if ((y<0) || (y>=ysize)) return false;
		if ((z<0) || (z>=zsize)) return false;
		return (getMaterialID(x, y, z) != Data.Materials.getId("air"))
				&& (getForm(x, y, z) == FORM_BLOCK);
	}

	public Order getOrder(int x, int y, int z){
		Order o;
		Player p = Interface.getInstance().player; //FIXME
		//in future we will need to access all players here
		//or move this method somewhere else
		for (int i=0; i<p.order.size(); ++i){
			o = p.order.get(i);
			if (o.b.x==x && o.b.y==y && o.b.z==z)
				return o;
		}
		return null;
	}
}
