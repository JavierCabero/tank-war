package gui;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import sprite.SpriteType;
import structure.SquareView;
import util.Action;

import core.GameObject;

public class Button extends GameObject implements SquareView {

	/* Button logic */
	private boolean pressed = false;
	protected Action clickAction = null;

	/* Constructor */
	public Button(int x, int y, SpriteType spriteType) {
		super(x, y, spriteType);
	}

	public Button(int x, int y, SpriteType sprite, Action clickAction) {
		this(x, y, sprite);
		this.clickAction = clickAction;
	}

	/* Methods */
	public void input() {
		// Nuthing
	}

	private boolean inBounds(int x, int y) {
		return getX() < x && x < getX() + getWidth() && Display.getHeight() - getY() - getHeight() < y && y < Display.getHeight() - getY();
	}

	public boolean isPressed() {
		return pressed;
	}

	public void render() {

		if (pressed) {
			glColor3f(.8f, .8f, .8f);
		}
		super.render();

		glColor3f(1f, 1f, 1f);
	}

	public void log(String msg) {
		if (debug) {
			System.out.println("[Button] " + msg);
		}
	}

	public void setLogMessage(String msg) {
		logMsg = msg;
	}

	public void onFocus(int x, int y) {
		boolean inbounds = inBounds(x, y);
		if (inbounds && !pressed) {
			if (Mouse.isButtonDown(0)) {
				pressed = true;
			}
		} else if (!Mouse.isButtonDown(0)) {
			pressed = false;
			if (inbounds) {
				clickAction.execute();
				if (logMsg != null) {
					log(logMsg);
				}
			}
		}
	}

}
