package entity;

import level.Level;
import ai.AI;

public class Enemy extends ArmedCharacter {

	/* Variables */
	private AI ai;

	/* Constructor */
	public Enemy(int x, int y, ArmedCharacterType armedCharacterType, Level level) {
		super(x, y, armedCharacterType, level);
		ai = new AI(armedCharacterType.getAIType(), this, level);
	}

	/* Methods */
	public void update() {
		super.update();
		ai.update();
	}

}
