package iface.widget;

import core.Data;
import org.lwjgl.opengl.GL11;
import graphics.Texture;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class Border {

	public static final int w = 5;
	public static final int h = 5;

	public static final int size = 512;

	static int framebufferID;

	static Texture bg;
	static Texture corner;
	static Texture side;

	static
	{
		framebufferID = glGenFramebuffersEXT();
		bg = Data.Textures.get("iconActive");//"frame_background");
		corner = Data.Textures.get("iconActive");//("frame_corner");
		side = Data.Textures.get("iconActive");//("frame_side");
	}

	static void drawQuad(int x1, int y1, int x2, int y2)
	{
		GL11.glTexCoord2d(0., 0.);
		GL11.glVertex2d(x1, y1);
		GL11.glTexCoord2d(1., 0.);
		GL11.glVertex2d(x2, y1);
		GL11.glTexCoord2d(1., 1.);
		GL11.glVertex2d(x2, y2);
		GL11.glTexCoord2d(0., 1.);
		GL11.glVertex2d(x1, y2);
	}

	public static int generate(int x, int y)
	{
		if ((x<2*w) || (y<2*h)) {
			return -1;
		}

		int colorTextureID = GL11.glGenTextures();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, size, size,
				0, GL11.GL_RGBA, GL11.GL_INT,
			(java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
			GL11.GL_TEXTURE_2D, colorTextureID, 0);

		GL11.glClearColor(0.f, 0.f, 0.f, 0.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLoadIdentity ();
		GL11.glColor3f(1.f, 1.f, 1.f);

		GL11.glBegin(GL11.GL_QUADS);

		bg.bind();
		drawQuad(w, h, x-w, y-h);

		corner.bind();
		drawQuad(0, 0, w, h);
		drawQuad(x, 0, x-w, h);
		drawQuad(x, y, x-w, y-h);
		drawQuad(0, y, w, y-h);

		side.bind();
		int N = (int)((x-2*w)/side.u_size+1);
		double sX = (x-2.*w)/N;
		for (int i=0; i<N; ++i) {
			drawQuad((int)(w+i*sX), 0, (int)(w+(i+1)*sX), h);
			drawQuad((int)(x-w-i*sX), y, (int)(x-w-(i+1)*sX), y-h);
		}
		int M = (int)((y-2*h)/side.v_size+1);
		double sY = (y-2.*h)/M;
		for (int j=0; j<M; ++j) {
			drawQuad(0, (int)(h+j*sY), w, (int)(h+(j+1)*sY));
			drawQuad(x, (int)(y-h-j*sY), x-w, (int)(y-h-(j+1)*sY));
		}

		GL11.glEnd();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		return colorTextureID;
	}

	public static void destroy(int textureID) {
		GL11.glDeleteTextures(textureID);
	}
}
