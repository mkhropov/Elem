package iface;
import graphics.GSList;

import org.lwjgl.opengl.GL11;
import graphics.GraphicalSurface;

public class ButtonFaceIcon extends ButtonFace {
	private GraphicalSurface icon;
	
	public ButtonFaceIcon(String iconName) {
		icon = GSList.getInstance().get(iconName);
	}
	
	@Override
	public void draw(int x, int y, int dx, int dy){
		icon.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0., 0.);
		GL11.glVertex2d(x+0.1*dx, y+0.1*dy);
		GL11.glTexCoord2d(1., 0.);
		GL11.glVertex2d(x+0.9*dx, y+0.1*dy);
		GL11.glTexCoord2d(1., 1.);
		GL11.glVertex2d(x+0.9*dx, y+0.9*dy);
		GL11.glTexCoord2d(0., 1.);
		GL11.glVertex2d(x+0.1*dx, y+0.9*dy);
		GL11.glEnd();
	}
}
