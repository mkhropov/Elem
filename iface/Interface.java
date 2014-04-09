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
	Player player;
	World world;
	Input input;
	public int current_layer;
	public Cursor cursor;
	public ArrayList<Button> buttons;
	public int viewMode;

	public char buildMaterial;
	public int commandMode;

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
		this.commandMode = commandMode;
	}

	public void setBuildMaterial(char material){
		this.buildMaterial = material;
	}

	private Interface() {
		world = World.getInstance();
		current_layer = world.zsize-1;
		camera = new Camera(world.xsize/2.0f, world.ysize/2.0f, (float) current_layer);
		input = new Input(this);
		cursor = new Cursor();
		buttons = new ArrayList<>();
		viewMode = Renderer.VIEW_MODE_FULL;
		Button t = new Button(300, 530, 60, 60, "IconSummon", "IconSummonD",
						new CommandSwitchMode(CommandSwitchMode.MODE_SPAWN));
		buttons.add(t);
		t = new Button(370, 530, 60, 60, "IconDig", "IconDigD",
						new CommandSwitchMode(CommandSwitchMode.MODE_DIG));
		buttons.add(t);
		t = new Button(440, 530, 60, 60, "IconBuild", "IconBuildD",
						new CommandSwitchMode(CommandSwitchMode.MODE_BUILD));
		buttons.add(t);
		this.setCommandMode(CommandSwitchMode.MODE_SPAWN);
		t = new Button(500, 530, 20, 20, "marble", "marble",
						new CommandSwitchMaterial(Material.MATERIAL_MARBLE));
		buttons.add(t);
		t = new Button(500, 550, 20, 20, "earth", "earth",
						new CommandSwitchMaterial(Material.MATERIAL_EARTH));
		buttons.add(t);
		t = new Button(500, 570, 20, 20, "stone", "stone",
						new CommandSwitchMaterial(Material.MATERIAL_STONE));
		buttons.add(t);
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

		for (int i=0; i<buttons.size(); ++i)
			buttons.get(i).draw();

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
