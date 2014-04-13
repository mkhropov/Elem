package iface;

import java.awt.Font;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class FloatingText {
	static TrueTypeFont font;
	static Color textColor;
	
	String s;
	int xsize;
	int ysize;
	int width, height;
	
	static {
		Font awtFont = new Font("Helvetica", Font.BOLD, 10);
		font = new TrueTypeFont(awtFont, true);
		textColor = new Color(.1f, .1f, 1.f);
	}
	
	public FloatingText(String s){
		this.s = s;
		this.width = font.getWidth(s);
		this.height = font.getHeight(s);
		this.xsize = width/2+2;
		this.ysize = height/2+1;
	}
	
	public void draw(int x, int y){
		int X = x; int Y = 600-y;
		//draw background
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3d(.8, .8, 1.);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(X, Y-ysize-5);
		GL11.glVertex2d(X-xsize, Y-2*ysize-5);
		GL11.glVertex2d(X-xsize, Y-5);
		GL11.glVertex2d(X-3, Y-5);
		GL11.glVertex2d(X, Y);
		GL11.glVertex2d(X+3, Y-5);
		GL11.glVertex2d(X+xsize, Y-5);
		GL11.glVertex2d(X+xsize, Y-2*ysize-5);
		GL11.glVertex2d(X-xsize, Y-2*ysize-5);
		GL11.glEnd();
		//draw outline
		GL11.glColor3d(.5, .5, 1.);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(X-xsize, Y-2*ysize-5);
		GL11.glVertex2d(X-xsize, Y-5);
		GL11.glVertex2d(X-3, Y-5);
		GL11.glVertex2d(X, Y);
		GL11.glVertex2d(X+3, Y-5);
		GL11.glVertex2d(X+xsize, Y-5);
		GL11.glVertex2d(X+xsize, Y-2*ysize-5);
		GL11.glVertex2d(X-xsize, Y-2*ysize-5);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		//print the message
		font.drawString(X-xsize+2, Y-ysize-5-height/2, s, textColor);	
	}
}