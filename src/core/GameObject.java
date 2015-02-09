package core;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.Renderable;

import entity.Health;
import entity.HealthType;

import sprite.Sprite;
import sprite.SpriteType;
import structure.AbstractSquareArea;
import util.Updatable;

/**
 * Makes the object to be able to be rendered, as well as many methods for
 * dimension sizes.
 */
public class GameObject extends AbstractSquareArea implements Renderable, Updatable {

	/* Variables */
	protected Sprite sprite;
	protected Health health = new Health(HealthType.LOW, -1);

	protected String logMsg = null;
	protected boolean debug = true;

	/* Constructor */
	public GameObject(int x, int y, SpriteType spriteType) {
		super(x, y, 0, 0);
		this.sprite = new Sprite(spriteType);
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
	}

	/* Methods */
	public void moveX(int movement) {
		super.moveX(movement);
		sprite.updateAnimation();
	}

	public void moveY(int movement) {
		super.moveY(movement);
		sprite.updateAnimation();
	}

	/* Methods delegated to sprite */
	public int getHalfHeight() {
		return sprite.getHalfHeight();
	}

	public int getHalfWidth() {
		return sprite.getHalfWidth();
	}

	public int getCenterX() {
		return x + sprite.getHalfWidth();
	}

	public int getCenterY() {
		return y + sprite.getHalfHeight();
	}

	public boolean containsPoint(int x, int y) {
		return this.x <= x && x < this.x + this.getWidth() && this.y <= y && y < this.y + this.getHeight();
	}

	public void updateAnimation() {
		sprite.updateAnimation();
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, getWidth(), getHeight());
	}

	public void render() {
		glPushMatrix();
		glTranslatef(x, y, 0);
		sprite.render();
		glPopMatrix();
	}

	public int damage(int damage) {
		return health.damage(damage);
	}

	public boolean isDead() {
		return health.isDead();
	}

	public void setDebug(boolean value) {
		debug = value;
	}

	public boolean debugEnabled() {
		return debug;
	}

	public void log(String msg) {
		if (debug) {
			System.out.println("[GameObject] " + msg);
		}
	}
	

	public void update() {
		
	}
}
