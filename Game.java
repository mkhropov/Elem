import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import graphics.Camera;
import graphics.Sun;

public class Game {
	World world;
	public static final int MAX_X=30;
	public static final int MAX_Y=30;
	public static final int MAX_Z=10;
	public static final double SCALE=0.7;
	int current_layer=MAX_Z-1;
	Camera camera;
	Sun sun;

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
				if (Keyboard.getEventCharacter() == 'z') {
					if (this.current_layer>0) this.current_layer--;
				}
				if (Keyboard.getEventCharacter() == 'x') {
					if (this.current_layer<(MAX_Z-1)) this.current_layer++;
				}
				if (Keyboard.getEventCharacter() == 'q') {
					camera.rotateLeft();
				}
				if (Keyboard.getEventCharacter() == 'e') {
					camera.rotateRight();
				}
			} else {
				// Key released
				if (Keyboard.getEventCharacter() == Keyboard.KEY_Z) {
					//Whatever
				}
				if (Keyboard.getEventCharacter() == Keyboard.KEY_X) {
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
		//Set up camera
		camera = new Camera(MAX_X/2, MAX_Y/2, MAX_Z/2);

		//Set up lighting
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		sun = new Sun();
		sun.update();

		float light_ambient[] = { 0.3f, 0.3f, 0.3f, 1.0f };

        ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
        temp.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = temp.asFloatBuffer();
		buffer.put(light_ambient); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, buffer);

		while (!Display.isCloseRequested()) {
			this.pollInput();
			camera.update();
			sun.update();
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
