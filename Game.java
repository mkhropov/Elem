import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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
		System.out.println("Display created. OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		// init OpenGL
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(10, -10, 7.5f, -7.5f, 10, -10);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glRotatef(50.0f, 1.0f, 1.0f, 1.0f);

		while (!Display.isCloseRequested()) {
			GL11.glRotatef(0.5f, 1.0f, 0.1f, 0.0f);
			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			for (int i=0; i<world.xsize; i++)
				for (int j=0; j<world.ysize; j++)
					for (int k=0; k<world.zsize; k++) {
						world.blockArray[i][j][k].Draw();
			}

			Display.update();
		}

		Display.destroy();
	}
}
