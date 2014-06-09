package iface.widget;

import graphics.Texture;
import core.Data;
import org.lwjgl.opengl.GL11;

public class Image extends Widget {

	public Texture image;

	private int w, h;
	private int sX, sY;

	public Image(String texture, int width, int height) {
		super();

		w = width;
		h = height;
		image = Data.Textures.get(texture);
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
//		System.out.printf("Image %d %d %d %d %d %d\n", X, Y, dX, dY, sX, sY);
	}

	public void draw(){
        if (!visible)
			return;

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
