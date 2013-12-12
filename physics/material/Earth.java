package physics.material;
import physics.*;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Earth extends Material {
    public Earth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);

		textureName = "earth";
        try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+textureName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
