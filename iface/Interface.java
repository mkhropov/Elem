package iface;

import world.World;
import player.Player;
import player.Order;
import physics.material.Material;

import graphics.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.util.ArrayList;
import java.util.concurrent.Callable;


public class Interface {
	public Camera camera;
	public Player player;
	World world;
	Input input;
	public int current_layer;
	public Cursor cursor;

	public static final int COMMAND_MODE_SPAWN = 0;
	public static final int COMMAND_MODE_DIG = 1;
	public static final int COMMAND_MODE_BUILD = 2;


	public static final int MENU_TOOLBAR = 0;
	public static final int MENU_DIG_FORM = 1;
	public static final int MENU_BUILD_FORM = 2;
	public static final int MENU_BUILD_MATERIAL = 3;
	public static final int MENU_COUNT = 4;


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

	public void setBuildMaterial(char material){
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
		SelectorMenu t = new SelectorMenu(new Element());
		t.addButton(new Button(300, 530, 60, 60, "IconSummon"), COMMAND_MODE_SPAWN);
		Button bDig = new Button(370, 530, 60, 60, "IconDig");
		bDig.bindAction(new Runnable() { @Override public void run() {
				Interface.getInstance().menus[Interface.MENU_DIG_FORM].toggle();}}, 1);
		t.addButton(bDig, COMMAND_MODE_DIG);
		Button bBuild = new Button(440, 530, 60, 60, "IconBuild");
		bBuild.bindAction(new Runnable() { @Override public void run() {
				Interface.getInstance().menus[Interface.MENU_BUILD_FORM].toggle();
				Interface.getInstance().menus[Interface.MENU_BUILD_MATERIAL].toggle();}}, 1);
		t.addButton(bBuild, COMMAND_MODE_BUILD);
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
		t.addButton(new Button(290, 480, 40, 40, "IconFloor"), World.FORM_FLOOR);
		t.addButton(new Button(340, 480, 40, 40, "IconBlock"), World.FORM_BLOCK);
		t.setState(World.FORM_BLOCK);
		menus[MENU_BUILD_FORM] = t;
		bBuild.setFace(t);
		t = new SelectorMenu(bBuild);
		t.show();
		t.addButton(new Button(410, 480, 40, 40, "marble"), Material.MATERIAL_MARBLE);
		t.addButton(new Button(460, 480, 40, 40, "earth"), Material.MATERIAL_EARTH);
		t.addButton(new Button(510, 480, 40, 40, "stone"), Material.MATERIAL_STONE);
		t.addButton(new Button(560, 480, 40, 40, "granite"), Material.MATERIAL_GRANITE);
		menus[MENU_BUILD_MATERIAL] = t;

		viewMode = Renderer.VIEW_MODE_FOW;
		this.setCommandMode(COMMAND_MODE_SPAWN);
		this.setBuildMaterial(Material.MATERIAL_MARBLE);
}

	public void draw(){
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
}
