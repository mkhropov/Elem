package physics.material;
import physics.*;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Material {
    public Temperature tFreeze;
    public Temperature tBoil;

	public Texture texture;
	public double textureRandomization;
	public String textureName;

	public Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);

		textureName = "void";
		textureRandomization = 0.5;
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+textureName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
