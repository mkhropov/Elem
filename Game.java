import generation.Generator;
import graphics.Renderer;
import iface.Interface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import player.Player;
import world.World;

public class Game {
	World world;
    Player p1;
	Interface iface;
	Renderer renderer;
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
		world = World.getInstance();
		world.init();
		newTime = getTime();
		for (int k=0; k<0; ++k){
			System.out.print("Iteration "+k+":\n");
			lastTime = newTime;
			Generator.getInstance().apply();
			newTime = getTime();
			deltaT = newTime-lastTime;
			System.out.println("Full time      "+(double)deltaT);
			System.out.println("Time per cube  "+((double)deltaT)/(Generator.getInstance().morphs.size()*world.xsize*world.ysize*world.zsize));
			System.out.println();
		}

		iface = Interface.getInstance();
		renderer = Renderer.getInstance();

		p1 = new Player();
		iface.setCurrentPlayer(p1);

		lastFPS = getTime();
		lastTime = getTime();

		// init OpenGL
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
			renderer.draw();
            world.iterate(deltaT);
			p1.iterate();
			Display.update();
			updateFPS(deltaT);
			Display.sync(60);
		}

		Display.destroy();
	}
}
