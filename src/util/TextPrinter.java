package util;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import static org.lwjgl.opengl.GL11.glColor3f;

public class TextPrinter {

	private static TrueTypeFont t = new TrueTypeFont(Font.decode("Small Fonts Normal"), false);

	public static void drawString(int x, int y, String text, Color color) {
		t.drawString(x, y, text, color);
	}

	public static void drawString(int x, int y, String text) {
		drawString(x, y, text, Color.black);
		glColor3f(1f, 1f, 1f);
	}

	public static void drawStringToLeft(int x, int y, String text) {
		drawString(x - t.getWidth(text), y, text);
		glColor3f(1f, 1f, 1f);
	}
}
