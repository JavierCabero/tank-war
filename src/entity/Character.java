package entity;

import java.util.List;

import core.GameObject;

import sprite.SpriteType;
import util.Math;

import level.Level;
import level.Tile;

import ai.Direction;

/**
 * Generic class with directional logic and mobility.
 * 
 * @author Kay
 * 
 */
public abstract class Character extends GameObject {

	/* Debug */
	private static boolean collisionDebug = false;

	/* Logic control variables */
	protected Level level;
	protected int speed = 2;
	protected Direction dir = Direction.UP;

	/* Constructor */
	public Character(int x, int y, SpriteType spriteType, Level level) {
		super(x, y, spriteType);
		this.level = level;
	}

	/* Methods */
	public void setDirection(Direction dir) {
		this.dir = dir;
		sprite.setDirection(dir);
	}

	public Direction getDirection() {
		return this.dir;
	}

	public int getSpeed() {
		return speed;
	}

	/* Methods delegated to health */
	public int damage(int ammount) {
		return health.damage(ammount);
	}

	public boolean move() {

		/* Data */
		int movement = 0;
		int width = level.getWidth();
		int height = level.getHeight();
		Tile[][] tileMatrix = level.getTileMatrix();
		List<GameObject> characters = level.getCollisionList(this);
		
		// Top left point
		int i = getX() / 32;
		int j = getY() / 32;
		if (!(0 <= i && i < width && 0 <= j && j < height)) {
			collisionLog("Something wrong going on (" + i + "," + j + ")");
			return false;
		}

		boolean animationChanged = false;

		// Scan tiles in the movement direction
		if (dir.equals(Direction.UP)) {
			/* Collision with tiles */
			// If character is not on first row
			if (0 < j) {
				// If not in the first column
				if (0 < i && tileMatrix[i][j - 1].getType().isSolid()) {
					movement = Math.min(getSpeed(), getY() - j * 32);
					// It is solid and the left edge of the character is at the
					// left of right edge of tile
					collisionLog("Colliding with some top tile");
				} else if ((i + 1) * 32 < getX() + getWidth() && i < width - 1 && tileMatrix[i + 1][j - 1].getType().isSolid()) {
					movement = Math.min(getSpeed(), getY() - j * 32);
					collisionLog("Colliding with some top tile");
					// If not in the last column (-1 as +1 would be the last)
					// [weird comment]
				} else {
					movement = getSpeed();
					collisionLog("Moving up");
				}
			}
			/* Collision with entities */
			for (GameObject that : characters) {
				if (!equals(that) && Math.abs(getX() - that.getX()) < getHalfWidth() + that.getHalfWidth() && that.getY() < getY()) {
					movement = Math.min(movement, getY() - that.getY() - (getHalfWidth() + that.getHalfWidth()));
				}
			}

			// -movement as we move upwards
			moveY(-movement);
			setDirection(Direction.UP);
			if (movement != 0) {
				animationChanged = true;
			}
		}
		i = getX() / 32;
		j = getY() / 32;
		if (dir.equals(Direction.LEFT)) {
			// If character is not on first column
			if (0 < i) {
				// If not in the first row
				if (tileMatrix[i - 1][j].getType().isSolid()) {
					// It is solid and the top edge of the character is upwards
					// the bottom edge of the tile
					movement = Math.min(getSpeed(), (getX()) - i * 32);
					collisionLog("Colliding with some left tile");
				} else if ((j + 1) * 32 < getY() + getHeight() && j < height - 1 && tileMatrix[i - 1][j + 1].getType().isSolid()
						&& (j + 1) * 32 < getY() + getHeight()) {
					movement = Math.min(getSpeed(), (getX()) - i * 32);
					collisionLog("Colliding with some left tile");
				} else {
					collisionLog("Moving left");
					movement = getSpeed();
				}
			}
			/* Collision with entities */
			for (GameObject that : characters) {
				if (!equals(that) && Math.abs(getY() - that.getY()) < getHalfWidth() + that.getHalfWidth() && that.getX() < getX()) {
					movement = Math.min(movement, getX() - that.getX() - (getHalfWidth() + that.getHalfWidth()));
				}
			}
			moveX(-movement);
			if (!animationChanged) {
				setDirection(Direction.LEFT);
			}
			if (movement != 0) {
				animationChanged = true;
			}
		}

		i = getX() / 32;
		j = (getY() + getHeight() - 1) / 32;
		if (dir.equals(Direction.DOWN)) {
			// If character is not on last row
			if (j < height - 1) {
				if (tileMatrix[i][j + 1].getType().isSolid()) {
					movement = Math.min(getSpeed(), (j + 1) * 32 - (getY() + getHeight()));
					collisionLog("Colliding with some bottom tile");
					// If not in the last column (-1 as +1 would be the last)
					// [weird comment]
				} else if (i < width - 1 && (i + 1) * 32 < getX() + getWidth() && tileMatrix[i + 1][j + 1].getType().isSolid() && i < width - 1) {
					// I think this is correct :)
					movement = Math.min(getSpeed(), (j + 1) * 32 - (getY() + getHeight()));
					collisionLog("Colliding with  some bottom tile");
				} else {
					collisionLog("Moving down");
					movement = getSpeed();
				}
			}
			/* Collision with entities */
			for (GameObject that : characters) {
				if (!equals(that) && Math.abs(getX() - that.getX()) < getHalfWidth() + that.getHalfWidth() && getY() < that.getY()) {
					movement = Math.min(movement, that.getY() - getY() - (getHalfWidth() + that.getHalfWidth()));
				}
			}
			// -movement as we move upwards
			moveY(movement);
			if (!animationChanged) {
				setDirection(Direction.DOWN);
			}
			if (movement != 0) {
				animationChanged = true;
			}
		}

		i = (getX() + getWidth() - 1) / 32;
		j = getY() / 32;
		if (dir.equals(Direction.RIGHT)) {
			// If character is not on last column
			if (i < width - 1) {
				// If not in the first row
				if (tileMatrix[i + 1][j].getType().isSolid()) {
					movement = Math.min(getSpeed(), (i + 1) * 32 - (getX() + getWidth()));
					collisionLog("Colliding with some right tile");
					// If not in the last column (-1 as +1 would be the last)
					// [weird comment]
				} else if (j < height - 1 && tileMatrix[i + 1][j + 1].getType().isSolid() && (j + 1) * 32 < getY() + getHeight()) {
					movement = Math.min(getSpeed(), (i + 1) * 32 - (getX() + getWidth()));
					collisionLog("Colliding with some right tile");
				} else {
					collisionLog("Moving right");
					movement = getSpeed();
				}
			}

			/* Collision with entities */
			for (GameObject that : characters) {
				if (!equals(that) && Math.abs(getY() - that.getY()) < getHalfWidth() + that.getHalfWidth() && getX() < that.getX()) {
					movement = Math.min(movement, that.getX() - getX() - (getHalfWidth() + that.getHalfWidth()));
				}
			}

			moveX(movement);
			if (!animationChanged) {
				setDirection(Direction.RIGHT);
			}
			if (movement != 0) {
				animationChanged = true;
			}
		}
		// LOL CODE SO OLD
		return animationChanged;
		// Formula: Math.min(speed, distanceToNextSolidObject/Tile)

	}

	private void collisionLog(String msg) {

		if (collisionDebug) {
			System.out.println("[Level collision] " + msg);
		}
	}

	public void update() {
		health.update();
	}
}
