package graphics;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class GraphicalSurface {
	private Texture texture;
	public double rand;
	public String name;

	public void bind() {
		texture.bind();
	}

	public GraphicalSurface(String Name, double Rand) {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+Name+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = Name;
		rand = Rand;
	}
}
