package graphics;

import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class GraphicalSurface extends GS {
	private Texture texture;
	public double rand;
	public String name;

	public void bind() {
		texture.bind();
	}

	public GraphicalSurface(String Name, double Rand) {
		name = Name;
		rand = Rand;
	}
	
	@Override
	public void init() {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+name+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
