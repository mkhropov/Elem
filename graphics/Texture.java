package graphics;

import java.io.IOException;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import utils.Initializable;
import utils.Named;

public class Texture implements Initializable, Named{
	org.newdawn.slick.opengl.Texture image;
	static String bound="";
	String name;
	String fname;
	public float tex_u;
	public float u_size;
	public float tex_v;
	public float v_size;
	public float rand;
	
	public void bind() {
		if (bound.equalsIgnoreCase(fname)) return;
		image.bind();
		bound = fname;
	}
	
	@Override
	public void initialize() {
		try {
			image = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/textures/png/"+fname));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	public Texture(){
		tex_u = 0.f;
		tex_v = 0.f;
		u_size = 1.f;
		v_size = 1.f;
		rand = 0.f;
	}
	
	public float resolveU(float u, int x, int y, int z) {
		return tex_u+(1-rand)*u*u_size/2+rand*((float)Math.abs(Math.sin(1.9*x+y+z)))*u_size;
	}
	
	public float resolveV(float v, int x, int y, int z) {
		return tex_v+(1-rand)*v*v_size/2+rand*((float)Math.abs(Math.sin(x-1.9*y+z)))*v_size;
	}
	
	public float resolveU(float u) {
		return tex_u+u*u_size;
	}
	
	public float resolveV(float v) {
		return tex_v+v*v_size;
	}
}
