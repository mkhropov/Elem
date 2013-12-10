import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Game {
    World world;
    public static final int MAX_X=30;
    public static final int MAX_Y=30;
    public static final int MAX_Z=10;
    public static final double SCALE=0.7;


	public static void main(String[] args) {
		Game game = new Game();
		game.world = new World(MAX_X,MAX_Y,MAX_Z);
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
		GL11.glOrtho(20, -20, 15f, -15f, 20, -20);
		GL11.glRotated(135, 1.0, 0.0, 0.0);
		GL11.glRotated(45, 0.0, 0.0, 1.0);
                GL11.glScaled(SCALE, SCALE, SCALE);
                GL11.glTranslated(-MAX_X/2, -MAX_Y/2, -MAX_Z/2);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		while (!Display.isCloseRequested()) {
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
