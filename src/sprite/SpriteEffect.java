package sprite;

import core.GameObject;
import level.Level;

/**
 * SpriteEffect renders a Sprite in a determinate location and after a
 * determinate time it sends a message to the level to end.
 * 
 * @author Kay
 * 
 */
public class SpriteEffect extends GameObject {

	// Needed to send die message
	private Level level;
	private int timeLeft = 64;
	
	public SpriteEffect(int x, int y, SpriteType spriteType, Level level) {
		super(x, y, spriteType);
		this.level = level;
	}

	public void update() {
		super.update();
		//TODO: Sprite animation update should be on top
		sprite.update();
		if (timeLeft > 0) {
			timeLeft--;
		} else if (timeLeft == 0) {
			level.removeSpriteEffect(this);
		}
	}

}
