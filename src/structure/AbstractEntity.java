package structure;

import entity.Entity;

/**
 * An entity is a positionable object in the space
 * @author Kay
 *
 */
public abstract class AbstractEntity  implements Entity{
	
	/* Variables */
	protected int x;
	protected int y;

	/* Constructor */
	public AbstractEntity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/* Methods */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveX(int movement) {
		x += movement;
	}

	public void moveY(int movement) {
		y += movement;
	}
}
