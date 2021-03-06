package iface;

import graphics.GraphicalEntity;
import graphics.Model;
import graphics.Texture;
import graphics.Renderer;
import core.Data;
import org.lwjgl.opengl.GL20;
import stereometry.Point;

public class CommandCube implements GraphicalEntity {

	int hue_uniform;

	public int x, y, z;
	public Point p;

	Model model;
	Texture gs;

	public CommandCube(int command, int x, int y, int z){
		this.x = x; this.y = y; this.z = z;
		this.p = new Point(x, y, z);
		Renderer r = Renderer.getInstance();
		hue_uniform = GL20.glGetUniformLocation(
		            r.shaders[Renderer.SHADER_GHOST], "hue");
		model = Data.Models.get("cube");
		int g;
/*		if (command == Interface.COMMAND_MODE_BUILD)
			g = graphics.GSList.getInstance().findId("IconBuild");
		else if (command == Interface.COMMAND_MODE_DIG)
			g = graphics.GSList.getInstance().findId("IconDig");
		else
			g = graphics.GSList.getInstance().findId("IconNotFound");*/
		gs = Data.Textures.get("selection");
	}

	@Override
	public Point getP(){
		return p;
	}

	@Override
	public void draw(){
		Renderer r = Renderer.getInstance();
		GL20.glUseProgram(r.shaders[Renderer.SHADER_GHOST]);
		GL20.glUniform4f(hue_uniform, 0.f, 0.f, 1.f, .6f);
		model.draw(x, y, z, 0.f, gs);
		GL20.glUseProgram(r.shaders[Renderer.SHADER_FADE]);
	}
}
