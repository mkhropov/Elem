package physics.material;
import physics.*;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Stone extends Material {
	public Stone() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);

        textureName = "stone";
		System.out.println("STOOONE");
        try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+textureName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
