package world;

import graphics.Renderer;

public class EventHandler {
	Renderer r;
//	World w;
	static private EventHandler instance = null;

	public static EventHandler getInstance() {
		if (instance == null) {
			instance = new EventHandler();
		}
		return instance;
	}

	private EventHandler() {
		r = Renderer.getInstance();
//		w = World.getInstance();
	}

	public void updateBlock(int x, int y, int z) {
		r.updateBlock(x,y,z);
	}

	public void addEntity(Entity e) {
		r.addEntity(e);
	}

	public void removeEntity(Entity e) {
//		w.item.remove(e);
		r.removeEntity(e);
	}

}
