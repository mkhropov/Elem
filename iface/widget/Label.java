package iface.widget;

import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;
import org.lwjgl.opengl.GL11;

public class Label extends Widget {
	String text;
	TrueTypeFont font;
	Color color;

	int textX, textY;

	static TrueTypeFont defaultFont;

	static {
		Font awtFont = new Font("Helvetica", Font.BOLD, 10);
		defaultFont = new TrueTypeFont(awtFont, true);
	}

	public Label(String text, TrueTypeFont font, Color color) {
		super();
		this.text = text;
		this.font = font;
		this.color = color;
	}

	public Label(String text, TrueTypeFont font) {
		this(text, font, new Color(0.f, 0.f, 0.f));
	}

	public Label(String text) {
		this(text, defaultFont);
	}

	@Override
	public void crop() {
		minX = font.getWidth(text) + 10;
		minY = font.getHeight(text);
	}

	@Override
	public void compile(int X, int Y, int dX, int dY) {
		this.X = X;
		this.Y = Y;
		this.dX = dX;
		this.dY = dY;
		/* assert (dX >= minX && dY >= minY); */
		textX = X + dX/2 - minX/2 + 5;
		textY = Y + dY/2 - minY/2;
//		System.out.printf("Label %d %d %d %d %d %d\n", X, Y, dX, dY, textX, textY);
	}

	@Override
	public void draw() {
        if (!visible)
            return;
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		TextureImpl.bindNone();
		font.drawString(textX, textY, text, color);
		GL11.glPopAttrib();
	}
}
