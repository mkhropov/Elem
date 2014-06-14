package iface.widget;

import graphics.Texture;
import core.Data;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;

public class GroupReflection extends Widget {

	private Texture image;
	private GroupButton activeButton;
	private ArrayList<GroupButton> group;

	private int w, h;
	private int sX, sY;

	public GroupReflection(GroupButton gb, int width, int height) {
		super();

		group = gb.group;
//		findActive();
		w = width;
		h = height;
	}

	private Texture getTexture(GroupButton b) {
		Widget W = b;
		while (!(W instanceof Image) && W.child != null && W.child.size()>0) {
			W = W.child.get(0);
		}
		if (W instanceof Image) {
			return ((Image) W).image;
		} else {
			return Data.Textures.get("");
		}
	}

	private void findActive() {
		assert(group != null);
		activeButton = null;
		for (GroupButton b: group) {
			if (b.active) {
				activeButton = b;
				break;
			}
		}
		assert(activeButton != null);
		image = getTexture(activeButton);
	}

	@Override
	public void crop() {
		minX = w;
		minY = h;
	}

	@Override
	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;

		sX = X + dX/2 - w/2;
		sY = Y + dY/2 - h/2;
//		System.out.printf("GReflect %d %d %d %d %d %d\n", X, Y, dX, dY, sX, sY);
	}

	@Override
	public void draw(){
        if (!visible)
			return;
		if (activeButton==null || !activeButton.active)
			findActive();
		image.bind();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(image.tex_u,
							image.tex_v);
		GL11.glVertex2d(sX, sY);
		GL11.glTexCoord2d(image.tex_u + image.u_size,
							image.tex_v);
		GL11.glVertex2d(sX+w, sY);
		GL11.glTexCoord2d(image.tex_u + image.u_size,
							image.tex_v + image.v_size);
		GL11.glVertex2d(sX+w, sY+h);
		GL11.glTexCoord2d(image.tex_u,
							image.tex_v + image.u_size);
		GL11.glVertex2d(sX, sY+h);
		GL11.glEnd();
	}
}
