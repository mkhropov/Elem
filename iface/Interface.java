package iface;

import world.World;
import player.Player;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import graphics.Renderer;

public class Interface {
	public Camera camera;
	Player player;
	World world;
	Input input;
	public int current_layer;
	public Cursor cursor;
	public ArrayList<Button> buttons;

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

	private Interface(){
		world = World.getInstance();
		current_layer = world.zsize-1;
		camera = new Camera(world.xsize/2.0f, world.ysize/2.0f, (float) current_layer);
		input = new Input(this);
		cursor = new Cursor();
		commandMode = Command.COMMAND_SPAWN;
		buttons = new ArrayList<>();
		Button t = new Button(0, 0, 80, 80, "IconSummon",
						new Command(Command.COMMAND_SPAWN));
		buttons.add(t);
		t = new Button(80, 0, 80, 80, "IconNotFound",
						new Command(Command.COMMAND_DIG));
		buttons.add(t);
	}

	public void draw(){
// entering 2d drawing mode
        GL11.glViewport(0,0,800,600);
//        glMatrixMode(GL_MODELVIEW);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLoadIdentity();
		Renderer.getInstance().resetMaterial(1.f);

		for (int i=0; i<buttons.size(); ++i)
			buttons.get(i).draw();

		cursor.draw2d();

// and leaving it. I hope.
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(10.0f, 4.0f/3.0f, 1.0f, 1000.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
		camera.forceUpdate(0);
	}
}
