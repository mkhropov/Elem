package iface;

import core.Data;
import graphics.Texture;
import org.lwjgl.opengl.GL11;

public class ButtonFaceIcon extends ButtonFace {
	private Texture icon;
	
	public ButtonFaceIcon(String iconName) {
		icon = Data.Textures.get(iconName);
	}
	
	@Override
	public void draw(int x, int y, int dx, int dy){
		icon.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(icon.resolveU(0.f), icon.resolveV(0.f));
		GL11.glVertex2d(x+0.1*dx, y+0.1*dy);
		GL11.glTexCoord2d(icon.resolveU(1.f), icon.resolveV(0.f));
		GL11.glVertex2d(x+0.9*dx, y+0.1*dy);
		GL11.glTexCoord2d(icon.resolveU(1.f), icon.resolveV(1.f));
		GL11.glVertex2d(x+0.9*dx, y+0.9*dy);
		GL11.glTexCoord2d(icon.resolveU(0.f), icon.resolveV(1.f));
		GL11.glVertex2d(x+0.1*dx, y+0.9*dy);
		GL11.glEnd();
	}
}
