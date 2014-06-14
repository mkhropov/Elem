package iface.widget;

import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;
import org.lwjgl.opengl.GL11;
import java.lang.reflect.Field;

public class TrackingLabel extends Widget {
	String text;
	TrueTypeFont font;
	Color color;

	int textX, textY;
	
	int w;
	Field f;
	Object o;

	static TrueTypeFont defaultFont;

	static {
		Font awtFont = new Font("Helvetica", Font.BOLD, 10);
		defaultFont = new TrueTypeFont(awtFont, true);
	}

	public TrackingLabel(Object o, String field, int width, TrueTypeFont font, Color color) {
		super();
		this.text = "";
		this.o = o;
		this.w = width;
		this.font = font;
		this.color = color;
		try {
			this.f = o.getClass().getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			System.out.println("No field "+field+" in object "+o);
			this.f = null;
		}
	}

	public TrackingLabel(Object o, String field, int width, TrueTypeFont font) {
		this(o, field, width, font, new Color(0.f, 0.f, 0.f));
	}

	public TrackingLabel(Object o, String field, int width) {
		this(o, field, width, defaultFont);
	}

	@Override
	public void crop() {
		minX = w + 10;
		minY = font.getHeight("A");
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

		try {
			text = f.get(o).toString();
			text = text.substring(0, Math.min(w, text.length()));
		} catch (IllegalAccessException e) {
			System.out.println("Can't access field "+f.getName()+" from "+o);
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		font.drawString(textX, textY, text, color);
		GL11.glColor3f(1.f, 1.f, 1.f);
	}
}
