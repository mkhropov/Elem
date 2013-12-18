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

import iface.Interface;
import graphics.Renderer;

import player.*;
import creature.*;

public class Game {
	World world;
    Player p1;
	Interface iface;
	Renderer renderer;
	public static final int MAX_X=25;
	public static final int MAX_Y=25;
	public static final int MAX_Z=20;
	public static final double SCALE=0.7;
	int fps = 0;
	long lastFPS;
	long deltaT;
	long lastTime, newTime;

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
		p1 = new Player(world);
		iface = new Interface(world, p1);
		renderer = new Renderer(world, iface);
        world.rend = renderer;

        p1.spawnCreature(new SmartWalkingElem(world,
                world.blockArray[world.xsize/2][world.ysize/2][world.zsize-1]));
		lastFPS = getTime();
		lastTime = getTime();

		// init OpenGL
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		//Set up lighting
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);

		float light_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };

		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();
		buffer.put(light_ambient); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, buffer);

		while (!Display.isCloseRequested()) {
			newTime = getTime();
			deltaT = newTime - lastTime;
			lastTime = newTime;
			iface.update(deltaT);
			renderer.draw(iface.current_layer);
            world.iterate(deltaT);
			Display.update();
			updateFPS(deltaT);
			Display.sync(60);
		}

		Display.destroy();
	}
}
