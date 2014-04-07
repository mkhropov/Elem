package iface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import graphics.*;
import physics.material.Material;
import world.World;

public class Cursor {

	public static final int STATE_ENABLED  = 0;
	public static final int STATE_DISABLED = 1;
	public static final int STATE_IFACE    = 2;
	public static final int STATE_MAX      = 3;
	public int state;

	public int x, y, z;  //3D
	public int X, Y;     //2D

	int channel_uniform; //shader uniform location
	Model model;
	GraphicalSurface gs;

	public Cursor(){
	//	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		try {
			org.lwjgl.input.Cursor emptyCursor = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			 e.printStackTrace();
			 System.exit(0);
		}
		Renderer r = Renderer.getInstance();
		channel_uniform = GL20.glGetUniformLocation(
			r.shaders[Renderer.SHADER_GHOST], "channel");
		int m = ModelList.getInstance().findId("cursor");
		model = ModelList.getInstance().get(m);
		int g = graphics.GSList.getInstance().findId("marble");
		gs = GSList.getInstance().get(g);

		state = STATE_IFACE;
	}

	public void reposition (int x, int y, int z, int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.x = x;
		this.y = y;
		this.z = z;
		char m;
		if (World.getInstance().isIn(x, y, z))
			m = World.getInstance().m[x][y][z];
		else {
			state = STATE_DISABLED;
			return;
		}
		switch(Interface.getInstance().commandMode){
			case Command.COMMAND_BUILD:
			case Command.COMMAND_SPAWN:
				state = (m == Material.MATERIAL_NONE)?(STATE_ENABLED):(STATE_DISABLED);
				break;
			case Command.COMMAND_DIG:
				state = (m != Material.MATERIAL_NONE)?(STATE_ENABLED):(STATE_DISABLED);
				break;
			default: break;
		}
	}

	public void draw2d(){
//		if (state != STATE_IFACE)
//			return; //or exception
//		GL11.glDisable(GL11.GL_LIGHT0);
//		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//		GL11.Color.white.bind();
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3d(1., 1., 1.);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(X, 600-Y);
		GL11.glVertex2d(X+20, 600-Y+14);
		GL11.glVertex2d(X+7, 600-Y+12);
		GL11.glVertex2d(X, 600-Y+23);
		GL11.glEnd();
		GL11.glColor3d(0., 0., 0.);
		GL11.glLineWidth(2);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(X, 600-Y);
		GL11.glVertex2d(X+20, 600-Y+14);
		GL11.glVertex2d(X+7, 600-Y+12);
		GL11.glVertex2d(X, 600-Y+23);
		GL11.glVertex2d(X, 600-Y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
//		GL11.glEnable(GL11.GL_LIGHT0);
	}

	public void draw3d(){
		if (state == STATE_IFACE)
			return; //or exception

		GL20.glUniform1i(channel_uniform, ((state == STATE_ENABLED)?(1):(0)));
		model.draw(x, y, z, 0.f, gs);
	}
}
