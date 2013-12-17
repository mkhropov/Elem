import world.*;

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
import graphics.GraphicalChunk;

public class Game {
	World world;
	public static final int MAX_X=25;
	public static final int MAX_Y=25;
	public static final int MAX_Z=20;
	public static final double SCALE=0.7;
	int current_layer=MAX_Z-1;
	Camera camera;
	Sun sun;
	int fps = 0;
	long lastFPS;
	long deltaT;
	long lastTime, newTime;
	GraphicalChunk[] gChunks;
	GraphicalChunk[] gChunksFull;

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	public void updateFPS(long dT) {
		if (lastFPS > 1000) {
			Display.setTitle("Elem (" + fps + "fps)");
			fps = 0; //reset the FPS counter
			lastFPS = 0; //add one second
		}
		lastFPS += dT;
		fps++;
	}

	public void pollInput() {
		while (Mouse.next()){
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					Block where = camera.resolveClick(Mouse.getEventX(), Mouse.getEventY(), current_layer, world);
					if (where != null) {
						System.out.println("Click at "+where.x+" "+where.y+" "+where.z);
					} else {
						System.out.println("Click at void");
					}
				}
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			System.out.println("SPACE KEY IS DOWN");
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				// Key pressed
				if (Keyboard.getEventCharacter() == 'z') {
					if (this.current_layer>0) {
						 this.current_layer--;
						 camera.repositionDelta(0.0f, 0.0f, -1.0f);
					}
				}
				if (Keyboard.getEventCharacter() == 'x') {
					if (this.current_layer<(MAX_Z-1)) {
						this.current_layer++;
						camera.repositionDelta(0.0f, 0.0f, 1.0f);
					}
				}
				if (Keyboard.getEventCharacter() == 'q') {
					camera.rotateLeft();
				}
				if (Keyboard.getEventCharacter() == 'e') {
					camera.rotateRight();
				}
                if (Keyboard.getEventCharacter() == 'i') {
                    world.iterate(deltaT);
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
		world = new World(MAX_X,MAX_Y,MAX_Z);
		lastFPS = getTime();
		lastTime = getTime();

		// init OpenGL
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//Set up camera
		camera = new Camera(MAX_X/2, MAX_Y/2, current_layer);

		//Set up lighting
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);

		sun = new Sun();
		sun.update();

		float light_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };

		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();
		buffer.put(light_ambient); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, buffer);

		float mat_ambient[] = { 0.5f, 0.5f, 0.5f, 0.0f };
		buffer.put(mat_ambient); buffer.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, buffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, buffer);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);

		gChunks = new GraphicalChunk[MAX_Z];
		for (int i=0; i<MAX_Z; i++) gChunks[i] = new GraphicalChunk(world, i, GraphicalChunk.MODE_TOP_VIEW);
		gChunksFull = new GraphicalChunk[MAX_Z];
		for (int i=0; i<MAX_Z; i++) gChunksFull[i] = new GraphicalChunk(world, i, GraphicalChunk.MODE_SHOW_ALL);

		while (!Display.isCloseRequested()) {
			newTime = getTime();
			deltaT = newTime - lastTime;
			lastTime = newTime;
			this.pollInput();
			camera.update(deltaT);
			sun.update();
			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			for (int k=this.current_layer; k>=0; k--) {
				if (k==this.current_layer) {
					GL11.glEnable(GL11.GL_LIGHT1);
					gChunksFull[k].draw();
					GL11.glDisable(GL11.GL_LIGHT1);
				} else {
					gChunks[k].draw();
				}
			}
                        world.iterate(deltaT);
			for (int i=0; i<world.creature.size(); ++i) world.creature.get(i).draw();
			Display.update();
			updateFPS(deltaT);
			Display.sync(60);
		}

		Display.destroy();
	}
}
