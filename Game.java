import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Game {
	World world;
	public static final int MAX_X=30;
	public static final int MAX_Y=30;
	public static final int MAX_Z=10;
	public static final double SCALE=0.7;
	int current_layer=MAX_Z-1;


	public static void main(String[] args) {
		Game game = new Game();
		game.world = new World(MAX_X,MAX_Y,MAX_Z);
		game.start();
	}

	public void pollInput() {
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			System.out.println("SPACE KEY IS DOWN");
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { 
				// Key pressed
				if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
					if (this.current_layer>0) this.current_layer--;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_X) {
					if (this.current_layer<(MAX_Z-1)) this.current_layer++;
				}
			} else {
				// Key released
				if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
					//Whatever
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_X) {
				}
			}
		}
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
		
		float light_ambient1[] = { 0.3f, 0.3f, 0.3f, 1.0f };

		buffer.put(light_ambient1); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, buffer);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		while (!Display.isCloseRequested()) {
			this.pollInput();
			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			for (int k=0; k<=this.current_layer; k++) {
				if (k==this.current_layer) {
					GL11.glEnable(GL11.GL_LIGHT1);
				}
				for (int i=0; i<world.xsize; i++)
					for (int j=0; j<world.ysize; j++) {
						world.blockArray[i][j][k].Draw();
				}
				GL11.glDisable(GL11.GL_LIGHT1);
			}

			Display.update();
		}

		Display.destroy();
	}
}
