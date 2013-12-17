package iface;

import world.World;
import player.Player;

public class Interface {
	Camera camera;
	Player player;
	World world;
	Input input;
	public int current_layer;

	public void update(long deltaT){
		input.poll(deltaT);
		camera.update(deltaT);
	}

	public Interface(World w, Player p){
		current_layer = w.zsize-1;
		camera = new Camera(w.xsize/2.0f, w.ysize/2.0f, (float) current_layer);
		input = new Input(this);
		player = p;
		world = w;
	}
}