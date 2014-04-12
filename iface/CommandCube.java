package iface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import graphics.*;
import stereometry.Point;

public class CommandCube implements GraphicalEntity {

	int channel_uniform;

	public int x, y, z;
	public Point p;

	Model model;
	GraphicalSurface gs;

	public CommandCube(int command, int x, int y, int z){
		this.x = x; this.y = y; this.z = z;
		this.p = new Point(x, y, z);
		Renderer r = Renderer.getInstance();
		channel_uniform = GL20.glGetUniformLocation(
		            r.shaders[Renderer.SHADER_GHOST], "channel");
		model = ModelList.getInstance().get("cube");
		int g;
/*		if (command == Interface.COMMAND_MODE_BUILD)
			g = graphics.GSList.getInstance().findId("IconBuild");
		else if (command == Interface.COMMAND_MODE_DIG)
			g = graphics.GSList.getInstance().findId("IconDig");
		else
			g = graphics.GSList.getInstance().findId("IconNotFound");*/
		gs = GSList.getInstance().get("selection");
	}

	@Override
	public Point getP(){
		return p;
	}

	@Override
	public void draw(){
		Renderer r = Renderer.getInstance();
		GL20.glUseProgram(r.shaders[Renderer.SHADER_GHOST]);
		GL20.glUniform1i(channel_uniform, 2);
		model.draw(x, y, z, 0.f, gs);
		GL20.glUseProgram(r.shaders[Renderer.SHADER_FADE]);
	}
}
