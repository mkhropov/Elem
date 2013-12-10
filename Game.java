import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game {
    World world;

	public static void main(String[] args) {
			Game game = new Game();
            game.world = new World(10,10,10);
		    game.start();
	}

	public void start() {
		try {
		Display.setDisplayMode(new DisplayMode(800,600));
		Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		while (!Display.isCloseRequested()) {
			Display.update();
		}

		Display.destroy();
	}
}
