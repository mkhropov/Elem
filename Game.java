import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Game {
    World world;

	public static void main(String[] args) {
			Game game = new Game();
//            game.world = new World(10,10,10);
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
		System.out.println("Display created. OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		while (!Display.isCloseRequested()) {
			Display.update();
		}

		Display.destroy();
	}
}
