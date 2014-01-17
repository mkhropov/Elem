package iface;

import world.World;
import player.Player;
import player.Order;

import graphics.*;

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

	private Interface() {
		world = World.getInstance();
		current_layer = world.zsize-1;
		camera = new Camera(world.xsize/2.0f, world.ysize/2.0f, (float) current_layer);
		input = new Input(this);
		cursor = new Cursor();
	}

	public void draw() {
		for (int i=0; i<player.order.size(); i++){
			drawOrder(player.order.get(i));
		}
		cursor.draw();
	}

	public void drawOrder(Order o) {
	/*	int gsid = 0;
		if (o.type == Order.ORDER_DIG) {
			gsid = GSList.getInstance().findId("selection");
		} else if (o.type == Order.ORDER_BUILD) {
			gsid = o.m.gsid;
		}
		if (o.b != null) {
			GraphicalSurface gs = GSList.getInstance().get(gsid);
			Model m = new graphics.models.Cube(gs);
			m.draw(o.b.x, o.b.y, o.b.z);
		}*/
	}
}
