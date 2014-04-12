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

	int hue_uniform; //shader uniform location
	float[] hue;
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
		hue_uniform = GL20.glGetUniformLocation(
			r.shaders[Renderer.SHADER_GHOST], "hue");
		hue = new float[]{0.f, 0.f, 0.f, .6f};
		int m = ModelList.getInstance().findId("cursor");
		model = ModelList.getInstance().get(m);
		int g = graphics.GSList.getInstance().findId("marble");
		gs = GSList.getInstance().get(g);

		state = STATE_ENABLED;
	}

	public void reposition (int x, int y, int z, int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.x = x;
		this.y = y;
		this.z = z;
		boolean isAir;
		if (World.getInstance().isIn(x, y, z))
			isAir = World.getInstance().isAir(x, y, z);
		else {
			state = STATE_DISABLED;
			return;
		}
		switch(Interface.getInstance().getCommandMode()){
			case Interface.COMMAND_MODE_BUILD:
			case Interface.COMMAND_MODE_SPAWN:
				state = (isAir)?(STATE_ENABLED):(STATE_DISABLED);
				break;
			case Interface.COMMAND_MODE_DIG:
				state = (!isAir)?(STATE_ENABLED):(STATE_DISABLED);
				break;
			default: break;
		}
		switch(state){
		case STATE_ENABLED:
			hue[0]=0.f; hue[1]=1.f;
			break;
		case STATE_DISABLED:
			hue[0]=1.f; hue[1]=0.f;
			break;
		default:
			break;
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
		
		GL20.glUniform4f(hue_uniform, hue[0], hue[1], hue[2], hue[3]);
		model.draw(x, y, z, 0.f, gs);
	}
}
