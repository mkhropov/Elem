import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

		//Set up lighting
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		float light_ambient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float light_diffuse[] = { 0.7f, 0.7f, 0.7f, 1.0f };
		float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float light_position[] = { -15.0f, 5.0f, 10.0f, 0.0f };

		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();
		buffer.put(light_ambient); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer);
		buffer.clear();
		buffer.put(light_diffuse); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer);
		buffer.clear();
		buffer.put(light_specular); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, buffer);
		buffer.clear();
		buffer.put(light_position); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, buffer);

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
