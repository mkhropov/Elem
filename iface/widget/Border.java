package iface.widget;

import core.Data;
import org.lwjgl.opengl.GL11;
import graphics.Texture;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class Border {

	public static final w = 5;
	public static final h = 5;

	public static final size = 512;

	static int framebufferID;

	static Texture corner;
	static Texture side;

	static
	{
		framebufferID = glGenFramebuffersEXT();
		corner = Data.Textures.getID("frame_corner");
		side = Data.Textures.getID("frame_side");
	}

	static void drawQuad(int x1, int y1, int x2, int y2)
	{
		GL11.glTexture2d(0., 0.);
		GL11.glVertex2d(x1, y1);
		GL11.glTexture2d(1., 0.);
		GL11.glVertex2d(x2, y1);
		GL11.glTexture2d(1., 1.);
		GL11.glVertex2d(x2, y2);
		GL11.glTexture2d(0., 1.);
		GL11.glVertex2d(x1, y2);
	}

	public static int generate(int x, int y)
	{
		if ((x<2*w) || (y<2*h)) {
			return -1;
		}

		int colorTextureID = glGenTextures();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, size, size, 0, GL_RGBA, GL_INT,
			(java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
			GL_TEXTURE_2D, colorTextureID, 0);

		glClear(GL_COLOR_BUFFER_BIT);
		glDisable(GL_DEPTH_TEST);
		glLoadIdentity ();

		GL11.glBegin(GL11.GL_QUADS);

		bg.bind();
		drawQuad(w, h, x-w, y-h);

		corner.bind();
		drawQuad(0, 0, w, h);
		drawQuad(x, 0, x-w, h);
		drawQuad(x, y, x-w, y-h);
		drawQuad(0, y, w, y-h);

		side.bind();
		int N = (x-2*w)/side.xsize()+1;
		double sX = (x-2.*w)/N;
		for (int i=0; i<N; ++i) {
			drawQuad(w+i*sX, 0, w+(i+1)*sX, h);
			drawQuad(x-w-i*sX, y, x-w-(i+1)*sX, y-h);
		}
		int M = (y-2*h)/side.ysize()+1;
		double sY = (y-2.*h)/M;
		for (int j=0; i<M; ++i) {
			drawQuad(0, h+j*sY, w, h+(j+1)*sY);
			drawQuad(x, y-h-j*sY, x-w, y-h-(j+1)*sY);
		}

		GL11.glEnd();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		return colorTextureID;
	}

	public static void destroy(int textureID) {
		glDeleteTextures(textureID);
	}
}
