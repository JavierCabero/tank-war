package entity;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import gui.Panel;
import level.Level;
import sprite.Sprite;
import sprite.SpriteType;
import util.TextPrinter;
import core.GameObject;

public class PlayerRespawn extends GameObject {

	/* Information panel */
	private static Panel panel = new Panel(640, 0, SpriteType.INFO_PANEL);
	private static int score1 = 0;
	private static int score2 = 0;
	private static Sprite player1Sprite = new Sprite(SpriteType.PLAYER_BLUE_SMALL);
	private static Sprite player2Sprite = new Sprite(SpriteType.PLAYER_ORANGE_SMALL);

	/* Spawn logic */
	private int spawn_rate = 128;
	private int next_spawn = 0;

	/* Player life */
	private int lifes = 4;
	private boolean respawning = false;

	/* Auxiliary */
	private Level level;
	private int playerNo = 0;

	/* Constructor */
	public PlayerRespawn(int x, int y, SpriteType spriteType, Level level, int playerNo) {
		super(x, y, spriteType);
		this.level = level;
		this.playerNo = playerNo;
	}

	/* Methods */
	/**
	 * Used to check for game over.
	 * 
	 * @return true if it was the last life.
	 */
	public boolean consumeLife() {
		if (lifes > 0) {
			lifes--;
			return true;
		} else {
			return false;
		}
	}

	public boolean tryRespawn() {
		if (!respawning && consumeLife()) { //TODO: delete !respawning because its busy waiting
			respawning = true;
		}
		return respawning;
	}

	public void update() {
		if (respawning) {
			if (next_spawn <= 0) {
				level.setPlayer(new Player(x, y, ArmedCharacterType.PLAYER_BASIC, level, playerNo), playerNo);
				next_spawn = spawn_rate;
				respawning = false;
			} else {
				if (next_spawn == 64) {
					level.addSpriteEffect(x, y, sprite.getType());
				}
				next_spawn--;
			}
		}
	}

	public boolean stillHasLifes() {
		return lifes > 0;
	}

	public void renderStats() {
		if (playerNo == 0) {
			panel.render();
		}
		/* Render lifes info */
		glPushMatrix();
		glTranslatef(670, 396 + 20 * playerNo, 0);
		for (int i = 0; i < lifes; i++) {
			glTranslatef(20, 0, 0);
			if (playerNo == 0) {
				player1Sprite.render();
			} else {
				player2Sprite.render();
			}
			if ((i + 1) % 3 == 0) {
				glTranslatef(-60, 20, 0);
			}
		}
		glPopMatrix();
		/* Render score */
		String scoreStr;
		if (playerNo == 0) {
			scoreStr = String.valueOf(score1);
		} else {
			scoreStr = String.valueOf(score2);
		}
		TextPrinter.drawStringToLeft(737, 160 + playerNo * 16, scoreStr);
		glColor3f(1f, 1f, 1f); // TrueTypeFonts are shiat
	}

	public void increaseScore1(int ammount) {
		score1 += ammount;
	}

	public void increaseScore2(int ammount) {
		score2 += ammount;
	}

	public static void resetScores() {
		score1 = 0;
		score2 = 0;
	}

	public void setLifes(int i) {
		lifes = i;
	}
}
