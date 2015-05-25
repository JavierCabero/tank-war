package sprite;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.opengl.Texture;

import ai.Direction;
import entity.Animation;

public class Sprite implements Renderable {

	/* THE texture */
	private Texture texture;
	private Animation animation;
	private int blinkTime = 0;

	/* Cache variables */
	private Direction dir = Direction.UP;
	private int maxDirs = 1;
	private int maxState = 1;
	private int state = 0;

	/* Performance improvement */
	private int width = 32;
	private int halfWidth = 16;
	private int height = 32;
	private int halfHeight = 16;
	private SpriteType spriteType;

	/* Constructor */
	public Sprite(SpriteType st) {
		this.spriteType = st;
		this.texture = SpriteFactory.getTexture(st.getName());

		maxState = st.getMaxState();
		// If needed we create an animation component
		if (st.getMaxState() != 1) {
			animation = new Animation(maxState);
		}

		// Sprite acts as hitbox so we need this to improve collisions
		this.width = (texture.getImageWidth() / maxState);
		// If the sprite has directions then the sprite width is 4 times smaller
		if (st.isDirectional()) {
			maxDirs = 4;
			this.width /= 4;
		}
		this.halfWidth = this.width / 2;
		this.height = texture.getImageHeight();
		this.halfHeight = this.height / 2;
	}

	/* Methods */
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	public int getHalfWidth() {
		return halfWidth;
	}

	public void update() {
		if (maxState != 1) {
			animation.update();
		}
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Direction getDirection() {
		return dir;
	}

	/**
	 * Sets the sprite to always animate. It "delegates" to the component
	 * animation.<br>
	 * <br>
	 * <b>Warnings:</b><br>
	 * Maybe the animation component does not exists.<br>
	 * 
	 * 
	 * @param animate
	 *            : boolean value to set.
	 */
	public void setAlwaysAnimate(boolean animate) {
		if (maxState != 1)
			animation.setAlwaysAnimate(animate);
	}

	/**
	 * It renders the current texture from (0,0). Rendering in other another
	 * positions will need a call glTranslate3f(x,y,0), x and y standing for the
	 * point you want to render (assuming you are rendering 2D, of course).
	 */
	public void render() {

		if ((blinkTime /8) % 2 == 0) {
			glEnable(GL_TEXTURE_2D);

			texture.bind();

			// If it is animated -> animate!
			if (maxState != 1)
				state = animation.getState();

			glBegin(GL_QUADS);
			glTexCoord2f((float) texture.getImageWidth() * (dir.getValue() * maxState + state) / (float) (texture.getImageWidth() * maxDirs * maxState)
					* texture.getWidth(), 0);
			glVertex2f(0, 0);
			glTexCoord2f((float) texture.getImageWidth() * (dir.getValue() * maxState + state + 1) / (float) (texture.getImageWidth() * maxDirs * maxState)
					* texture.getWidth(), 0);
			glVertex2f(width, 0);
			glTexCoord2f((float) texture.getImageWidth() * (dir.getValue() * maxState + state + 1) / (float) (texture.getImageWidth() * maxDirs * maxState)
					* texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);
			glTexCoord2f((float) texture.getImageWidth() * (dir.getValue() * maxState + state) / (float) (texture.getImageWidth() * maxDirs * maxState)
					* texture.getWidth(), texture.getHeight());
			glVertex2f(0, height);
			glEnd();

			glDisable(GL_TEXTURE_2D);
		}
		if (blinkTime > 0) {
			blinkTime--;
		}
	}

	public void setBlinkTime(int t) {
			blinkTime = t;
	}


	public void updateAnimation() {
		if (maxState != 1) {
			animation.update();
		}
	}

	public SpriteType getType() {
		return spriteType;
	}
}

/*
 * - Notes -
 * 
 * The animation component is null if not needed so you have to take care when
 * accessing to it because sometimes it will be null.
 */
