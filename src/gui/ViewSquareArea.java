package gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;

import structure.AbstractView;
import structure.SquareView;

public class ViewSquareArea extends AbstractView {

	/* Variables */
	private List<SquareView> views;
	private int xDistance = 36, yDistance = 36;
	private int xOffset = 0, yOffset = 0;
	private boolean visibleArea = false;

	/* Constructor */
	public ViewSquareArea(int x, int y, int width, int height) {
		super(x, y, width, height);
		xOffset = x + 4;
		yOffset = y + 4;
		views = new ArrayList<SquareView>();
	}

	public SquareView get(int i) {
		return views.get(i);
	}

	public void setVisibleArea(boolean b) {
		this.visibleArea = b;
	}

	public void setYDistance(int d) {
		yDistance = d;

		reAllocate();
	}

	public SquareView getView(int index) {
		return views.get(index);
	}

	public void addView(SquareView v) {

		// Wrap views
		if (xOffset + v.getWidth() > getX() + getWidth()) {
			xOffset = x + 4;
			yOffset += yDistance;
		}

		v.setX(xOffset);
		v.setY(yOffset);
		views.add(v);

		xOffset += xDistance;
	}

	public void setXDistance(int d) {
		xDistance = d;

		reAllocate();
	}

	private void reAllocate() {
		xOffset = x + 4;
		yOffset = y + 4;

		SquareView v;
		for (int i = 0; i < views.size(); i++) {

			v = views.get(i);

			if (xOffset + v.getWidth() > getX() + getWidth()) {
				xOffset = x + 4;
				yOffset += yDistance;
			}

			v.setX(xOffset);
			v.setY(yOffset);

			xOffset += xDistance;
		}
	}

	public void update() {
		for (SquareView v : views) {
			v.update();
		}
	}

	public void render() {

		if (visibleArea) {
			glDisable(GL_TEXTURE_2D);
			glColor3f(.6f, .6f, .6f);
			glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x + width, y);
			glVertex2f(x + width, y + height);
			glVertex2f(x, y + height);
			glEnd();
			glColor3f(1f, 1f, 1f);
			glEnable(GL_TEXTURE_2D);
		}
		for (SquareView v : views) {
			v.render();
		}
	}

	public void input() {

	}

	public void onFocus(int x, int y) {
		for (SquareView v : views) {
			v.onFocus(x, y);
		}
	}
}
