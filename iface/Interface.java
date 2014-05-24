package iface;

import core.Data;
import graphics.Renderer;
import java.awt.Font;
import java.util.Iterator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.TrueTypeFont;
import physics.Material;
import player.Player;
import player.Zone;
import world.Block;
import world.World;


public class Interface {
	public Camera camera;
	public Player player;
	World world;
	Input input;
	public int current_layer;
	public Cursor cursor;
	TrueTypeFont sansSerif;
	TrueTypeFont serif;

	public boolean debug = false;

	public static final int COMMAND_MODE_SPAWN = 0;
	public static final int COMMAND_MODE_DIG = 1;
	public static final int COMMAND_MODE_BUILD = 2;
	public static final int COMMAND_MODE_ZONE = 3;
	public static final int COMMAND_MODE_CANCEL = 4;


	public static final int MENU_TOOLBAR = 0;
	public static final int MENU_DIG_FORM = 1;
	public static final int MENU_BUILD_FORM = 2;
	public static final int MENU_BUILD_MATERIAL = 3;
	public static final int MENU_ZONE_TYPE = 4;
	public static final int MENU_COUNT = 5;


	public Menu[] menus;

	public int viewMode;

	public void update(long deltaT){
		input.poll(deltaT);
		camera.update(deltaT);
	}

	private static Interface instance = null;
	public static Interface getInstance() {
		if (instance == null) {
			instance = new Interface();
		}
		return instance;
	}

	public void setCurrentPlayer(Player p) {
		player = p;
	}

	public void setCommandMode(int commandMode){
		SelectorMenu t = (SelectorMenu)menus[MENU_TOOLBAR];
		t.setState(commandMode);
	}

	public int getCommandMode(){
		SelectorMenu t = (SelectorMenu)menus[MENU_TOOLBAR];
		return t.getState();
	}

	public void setBuildMaterial(int material){
		SelectorMenu t = (SelectorMenu)menus[MENU_BUILD_MATERIAL];
		t.setState(material);
	}

	public int getBuildMaterial(){
		SelectorMenu t = (SelectorMenu)menus[MENU_BUILD_MATERIAL];
		return t.getState();
	}

	public void setBuildForm(int form){
		SelectorMenu t = (SelectorMenu)menus[MENU_BUILD_FORM];
		t.setState(form);
	}

	public int getBuildForm(){
		SelectorMenu t = (SelectorMenu)menus[MENU_BUILD_FORM];
		return t.getState();
	}
	
	public void setZoneType(int type){
		SelectorMenu t = (SelectorMenu)menus[MENU_ZONE_TYPE];
		t.setState(type);
	}

	public int getZoneType(){
		SelectorMenu t = (SelectorMenu)menus[MENU_ZONE_TYPE];
		return t.getState();
	}

	public void setDigForm(int form){
		SelectorMenu t = (SelectorMenu)menus[MENU_DIG_FORM];
		t.setState(form);
	}

	public int getDigForm(){
		SelectorMenu t = (SelectorMenu)menus[MENU_DIG_FORM];
		return t.getState();
	}

	public int getDirection(){
		return World.DIRECTION_UP;
	}

	private Interface() {
		world = World.getInstance();
		current_layer = world.zsize-1;
		camera = new Camera(world.xsize/2.0f, world.ysize/2.0f, (float) current_layer);
		input = new Input(this);
		cursor = new Cursor();
		menus = new Menu[MENU_COUNT];
		Font awtFont = new Font("Times New Roman", Font.PLAIN, 12);
		serif = new TrueTypeFont(awtFont, true);
		awtFont = new Font("Helvetica", Font.BOLD, 12);
		sansSerif = new TrueTypeFont(awtFont, true);
		SelectorMenu t = new SelectorMenu(new Element());
		t.addButton(new Button(235, 530, 60, 60, "IconSummon"), COMMAND_MODE_SPAWN);
		Button bDig = new Button(305, 530, 60, 60, "IconDig");
		bDig.bindAction(new Runnable() { @Override public void run() {
				Interface.getInstance().menus[Interface.MENU_DIG_FORM].toggle();}}, 1);
		t.addButton(bDig, COMMAND_MODE_DIG);
		Button bBuild = new Button(375, 530, 60, 60, "IconBuild");
		bBuild.bindAction(new Runnable() { @Override public void run() {
				Interface.getInstance().menus[Interface.MENU_BUILD_FORM].toggle();
				Interface.getInstance().menus[Interface.MENU_BUILD_MATERIAL].toggle();}}, 1);
		t.addButton(bBuild, COMMAND_MODE_BUILD);
		t.addButton(new Button(515, 530, 60, 60, "IconCancel"), COMMAND_MODE_CANCEL);
		Button bZone = new Button(445, 530, 60, 60, "IconZone");
		bZone.bindAction(new Runnable() { @Override public void run() {
				Interface.getInstance().menus[Interface.MENU_ZONE_TYPE].toggle();}}, 1);
		t.addButton(bZone, COMMAND_MODE_ZONE);
		menus[MENU_TOOLBAR] = t;
		menus[MENU_TOOLBAR].show();
		t = new SelectorMenu(bDig);
		t.show();
		t.addButton(new Button(355, 480, 40, 40, "IconDig"), World.FORM_FLOOR);
		t.addButton(new Button(405, 480, 40, 40, "IconChannel"), World.FORM_BLOCK);
		menus[MENU_DIG_FORM] = t;
		bDig.setFace(t);
		t = new SelectorMenu(bBuild);
		t.show();
		t.addButton(new Button(300, 480, 40, 40, "IconFloor"), World.FORM_FLOOR);
		t.addButton(new Button(350, 480, 40, 40, "IconBlock"), World.FORM_BLOCK);
		t.setState(World.FORM_BLOCK);
		menus[MENU_BUILD_FORM] = t;
		bBuild.setFace(t);
		t = new SelectorMenu(bBuild);
		t.show();
		t.addButton(new Button(410, 480, 40, 40, "marble"), Data.Materials.getId("marble"));
		t.addButton(new Button(460, 480, 40, 40, "earth"), Data.Materials.getId("earth"));
		t.addButton(new Button(510, 480, 40, 40, "gabbro"), Data.Materials.getId("gabbro"));
		t.addButton(new Button(560, 480, 40, 40, "granite"), Data.Materials.getId("granite"));
		menus[MENU_BUILD_MATERIAL] = t;
		t = new SelectorMenu(bZone);
		t.show();
		t.addButton(new Button(410, 480, 40, 40, ""), Data.Zones.getId("stockpile"));
		t.addButton(new Button(460, 480, 40, 40, ""), Data.Zones.getId("lumber"));
		t.addButton(new Button(510, 480, 40, 40, ""), Data.Zones.getId("mason"));
		t.addButton(new Button(560, 480, 40, 40, ""), Data.Zones.getId("jewel"));
		menus[MENU_ZONE_TYPE] = t;

		viewMode = Renderer.VIEW_MODE_FOW;
		this.setCommandMode(COMMAND_MODE_SPAWN);
		this.setBuildMaterial(Data.Materials.getId("marble"));
}

	public void draw(){
		if (this.getCommandMode() == Interface.COMMAND_MODE_ZONE) {
			int hue_uniform =GL20.glGetUniformLocation(Renderer.getInstance().shaders[Renderer.SHADER_GHOST], "hue");;
			for (int i = 0; i < player.zones.size(); ++i){
				Zone z = player.zones.get(i);
				GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_GHOST]);
				GL20.glUniform4f(hue_uniform, z.type.color[0], z.type.color[1], z.type.color[2], 0.6f);
				Iterator<Block> it = z.iterator();
				Block b;
				while (it.hasNext()) {
					b = it.next();
					Data.Models.get("floor").draw(b.x, b.y, b.z, 0.f, Data.Textures.get("selection"));
				}
				GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_NONE]);
			}
		}
		input.draw();
// entering 2d drawing mode
		GL11.glViewport(0,0,800,600);
//		glMatrixMode(GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLoadIdentity();
		Renderer.getInstance().resetMaterial(1.f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3d(1., 1., 1.);

		for (int i=0; i<MENU_COUNT; ++i)
			menus[i].draw();

		for (FloatingText ft: Renderer.getInstance().ftArray)
			ft.draw();

		if (debug){
			int x = Mouse.getEventX();
			int y = Mouse.getEventY();
			Block where;
			int[] pos = camera.resolvePixel(x, y, current_layer);
			where = world.getBlock(pos[0], pos[1], current_layer);
//			int[] pos2 = Renderer.getInstance().get2DCoord(new Point(where));
			sansSerif.drawString(8, 2,
					"Mouse coords:   x "+where.x+"   y "+where.y+"   z "+where.z);
//			sansSerif.drawString(8, 14, pos[0]+"x   "+pos[1]+"y   "+current_layer+"z");
//			sansSerif.drawString(8, 14, x+"="+pos2[0]+", "+(600-y)+"="+pos2[1]);
		}

		cursor.draw2d();

// and leaving it. I hope.
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(10.0f, 4.0f/3.0f, 1.0f, 1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		camera.forceUpdate(0);
	}

	public boolean canPlaceCommand(int x, int y, int z) {
		World w = World.getInstance();
		if (!w.isIn(x,y,z))
			return false;
		switch (getCommandMode()) {
			case Interface.COMMAND_MODE_SPAWN:
				return (player. blockKnown(x, y, z)
						&& w.isEmpty(x, y, z));
			case Interface.COMMAND_MODE_DIG:
				return (!player. blockKnown(x, y, z) ||
						w.isFull(x, y, z) ||
						(getDigForm() == World.FORM_BLOCK))
						&& !player.blockAlreadyRequested(w.getBlock(x, y, z));
			case Interface.COMMAND_MODE_BUILD:
				return (player. blockKnown(x, y, z) &&
						(w.isAir(x, y, z) ||
							((w.getForm(x, y, z) == World.FORM_FLOOR)
							&& (getBuildMaterial() == w.getMaterialID(x, y, z))))
						&& !player.blockAlreadyRequested(w.getBlock(x, y, z)));
			case Interface.COMMAND_MODE_ZONE: 
				return (player. blockKnown(x, y, z)
						&& w.isEmpty(x, y, z) && w.hasSolidFloor(x, y, z));				
			case Interface.COMMAND_MODE_CANCEL: return player.blockAlreadyRequested(w.getBlock(x, y, z));
			default:
					System.out.println("Interface.canPlaceCommand: weird request");
			return false;
		}
	}

}
