package iface;

import world.World;
import player.Player;

public class Interface {
	public Camera camera;
	Player player;
	World world;
	Input input;
	public int current_layer;
	public Cursor cursor;

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
	}
}
