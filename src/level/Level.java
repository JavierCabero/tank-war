package level;

import static org.lwjgl.opengl.GL11.*;

import entity.ArmedCharacter;
import entity.ArmedCharacterType;
import entity.Bonus;
import entity.BonusType;
import entity.Character;
import entity.Enemy;
import entity.EnemyRespawn;
import entity.Player;
import entity.PlayerRespawn;
import entity.Projectile;
import gui.View;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;

import audio.AudioBuffer;
import audio.AudioType;

import sprite.Sprite;
import sprite.SpriteEffect;
import sprite.SpriteType;
import structure.AbstractSquareArea;
import util.Action;

import core.GameObject;
import core.GameState;
import core.GameStateFactory;
import core.TankWar;

public class Level extends AbstractSquareArea implements GameState, View {

	private static int levelNo = 0;
	private static TrueTypeFont t;

	/* Data */
	private String path = null;
	private int width = 20;
	private int height = 20;

	private Tile[][] tileMatrix = new Tile[width][height];
	private List<Projectile> projectiles;
	private List<Projectile> deadProjectiles;
	private List<SpriteEffect> spriteEffects;
	private List<SpriteEffect> deadSpriteEffects;
	private List<Bonus> deadBonuses;

	/* Respawns */
	private EnemyRespawn enemyRespawn = new EnemyRespawn(this);
	private PlayerRespawn player1Respawn;
	private PlayerRespawn player2Respawn;

	/* Level npcs */
	private Player player1;
	private Player player2;
	private List<Character> characters;
	private List<Bonus> bonuses;

	/* Debug and modes */
	private boolean actionsDebug = true;
	private static boolean debug = true;

	/* Win state */
	private int timeToNextLevel = 256;
	private GameObject victorySprite;
	private String nextLevel;
	private String winAction = "credits";
	private boolean victory = false;

	/* Sound */
	private String musicName;
	private Audio music;
	private Audio hitSound = AudioBuffer.getAudio(AudioType.WOOD_HIT_01);

	/* Auxiliary */
	private LevelEngine levelEngine;
	private Random random = new Random();

	/* Collision */
	private Quadtree quad = new Quadtree(0, new Rectangle(0, 0, 640, 640));

	/* onFocus */
	private Action action = null;

	/* Constructor */
	public Level() {

		// All maps have the same dimensions
		super(0, 0, 640, 640);

		/* Initialize null tiles */
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tileMatrix[i][j] == null) {
					tileMatrix[i][j] = new Tile(i * Tile.WIDTH, j * Tile.HEIGHT, TileType.NULL);
				}
			}
		}

		/* Load victory stuff */
		Sprite vSprite = new Sprite(SpriteType.VICTORY_SPRITE);
		victorySprite = new GameObject(width * 16 - vSprite.getHalfWidth(), height * 12 - vSprite.getHalfHeight(), SpriteType.VICTORY_SPRITE);

		/* Create font printer */
		t = new TrueTypeFont(Font.decode("Small Fonts Normal"), false);

		/* Initialize structures */
		characters = new ArrayList<Character>();
		projectiles = new ArrayList<Projectile>();
		deadProjectiles = new LinkedList<Projectile>();
		spriteEffects = new ArrayList<SpriteEffect>();
		deadSpriteEffects = new LinkedList<SpriteEffect>();
		bonuses = new ArrayList<Bonus>();
		deadBonuses = new LinkedList<Bonus>();
	}

	private static void log(String msg) {
		if (debug) {
			System.out.println("[Level] " + msg);
		}
	}

	public void clear() {

		/* Clear tiles */
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tileMatrix[i][j] = new Tile(i * Tile.WIDTH, j * Tile.HEIGHT, TileType.NULL);
			}
		}

		/* Clear enemies */
		characters.clear();

		/* Clear entities */
		player1 = null;
		player2 = null;
		player1Respawn = null;
		player2Respawn = null;
		enemyRespawn.clear();

		// TODO: Clear level data
	}

	public void setTileAt(int x, int y, TileType tileType) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			tileMatrix[x][y] = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT, tileType);
			log(tileType.getName() + " placed on (" + x + "," + y + ")");
		} else {
			log("Invalid index tile.");
		}
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer(Player player, int playerNo) {
		if (playerNo == 0) {
			this.player1 = player;
		} else {
			this.player2 = player;
		}
		characters.add(player);
	}

	public void addEnemyAt(int x, int y, ArmedCharacterType enemyType) {
		characters.add(new Enemy(x, y, enemyType, this));
	}

	public Tile getTileAt(int x, int y) {
		return tileMatrix[x][y];
	}

	public List<Character> getCharacters() {
		return characters;
	}

	/* ------------- GAME STATE PRINCIPAL METHODS ------------- */
	public void input() {
		// Nuthing, coz input makes nu zenze
	}

	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}

	public void update() {

		/* Quad-tree collision */
		quad.clear();

		// First, we insert every character
		for (int i = 0; i < characters.size(); i++) {
			quad.insert(characters.get(i));
		}

		/* Update every character */
		for (Character c : characters) {
			c.update();
		}

		/* Update players respawn */
		player1Respawn.update();
		player2Respawn.update();

		/* Update enemy respawns */
		enemyRespawn.update();

		/* Check for dead guys */
		if (player1 == null) {
			if (!player1Respawn.tryRespawn()) {
				if (player2Respawn == null || !player2Respawn.stillHasLifes()) {
					setGameState(GameStateFactory.GAME_OVER);
				}
			}
		}
		// TODO: This is busy waiting when player 2 is dead and player1
		// remainsAlive
		// TODO: This should be on playerRespawn
		if (player2 == null) {
			if (!player2Respawn.tryRespawn()) {
				if (player1Respawn == null || !player1Respawn.stillHasLifes()) {
					setGameState(GameStateFactory.GAME_OVER);
				}
			}
		}

		// Insert projectiles for collisions
		for (Projectile p : projectiles) {
			quad.insert(p);
		}

		/* Update projectiles */
		for (Projectile p : projectiles) {
			p.update();
		}

		// Remove dead characters
		{
			int i = 0;
			Character c;
			while (i < characters.size()) {
				c = characters.get(i);
				if (c.isDead()) {
					removeCharacter(c);
				} else {
					i++;
				}
			}
		}

		// Update spriteEffects
		for (SpriteEffect se : spriteEffects) {
			se.update();
		}

		// Remove dead projectiles
		{
			int i = 0;
			while (i < projectiles.size()) {
				if (projectiles.get(i).isDead()) {
					Projectile p = projectiles.remove(i);
					p.getArmedCharacter().addAmmo(1);
					spriteEffects.add(new SpriteEffect(p.getX() + p.getHalfWidth() - new Sprite((p.getEffectOnDead())).getHalfWidth(), p.getY()
							+ p.getHalfHeight() - new Sprite(p.getEffectOnDead()).getHalfHeight(), p.getEffectOnDead(), this));
					hitSound.playAsSoundEffect(1.0f, 1.0f, false);
				} else {
					i++;
				}
			}
		}
		// Remove sprite effects
		for (SpriteEffect se : deadSpriteEffects) {
			spriteEffects.remove(se);
		}
		deadSpriteEffects.clear();

		/* Update victory state */
		if (victory) {
			if (timeToNextLevel > 0) {
				timeToNextLevel--;
			} else {
				doWinAction();
			}
		} else if (characters.size() <= 2
				&& ((player1Respawn != null && player1Respawn.stillHasLifes()) || (player2Respawn != null && player2Respawn.stillHasLifes()))
				&& enemyRespawn.noMoreEnemies()) {
			victory = true;
			for (Character c : characters) {
				if (c.getClass().equals(Enemy.class)) {
					victory = false;
				}
			}
			if (victory) {
				player1Respawn.increaseScore1(1000);
				if (player2Respawn != null) {
					player2Respawn.increaseScore2(1000);
				}
			}
		}

		for (Bonus b : bonuses) {
			b.update();
		}

		for (Bonus b : deadBonuses) {
			bonuses.remove(b);
		}
		deadBonuses.clear();

		if (random.nextInt(1000) <= 5 && bonuses.size() < 3) {
			addRandomBonus();
		}

	}

	private void addRandomBonus() {
		List<Tile> auxiliaryList = new ArrayList<Tile>();
		for (Tile[] tArr : tileMatrix) {
			for (Tile t : tArr) {
				if (!t.getType().isSolid())
					auxiliaryList.add(t);
			}
		}

		Tile chosenTile = auxiliaryList.get(random.nextInt(auxiliaryList.size()));
		BonusType[] bonusTypes = BonusType.values();
		bonuses.add(new Bonus(chosenTile.getX(), chosenTile.getY(), bonusTypes[random.nextInt(bonusTypes.length)], this));
	}

	private void doWinAction() {
		if (winAction.equals("changelevel")) {
			stopEverything();
			levelEngine.setFileToLoad(nextLevel);
		} else if (winAction.equals("credits")) {
			setGameState(GameStateFactory.CREDITS);
		} else {
			actionsLog("Unkown action: " + winAction);
		}

	}

	public void stopEverything() {
		if (music != null) {
			music.stop();
		}
	}

	public void resetScores() {
		PlayerRespawn.resetScores();
	}

	private void actionsLog(String msg) {
		if (actionsDebug) {
			System.out.println("[Level] " + msg);
		}
	}

	public void renderStats() {

		// (Interface info)
		player1Respawn.renderStats();
		player2Respawn.renderStats();

		enemyRespawn.renderEnemyList();

		String levelNoStr = String.valueOf(levelNo);
		t.drawString(736 - t.getWidth(levelNoStr), 278, levelNoStr, Color.black, 64, 64);

		glColor3f(1f, 1f, 1f);

	}

	public void renderRespawns() {

		glPushMatrix();
		glTranslatef(x, y, 0);

		/* View respawns */
		if (player1Respawn != null) {
			player1Respawn.render();
		}
		if (player2Respawn != null) {
			player2Respawn.render();
		}

		enemyRespawn.render();

		glPopMatrix();
	}

	public void render() {

		glPushMatrix();
		glTranslatef(x, y, 0);

		/* Render tiles */
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tileMatrix[i][j].render();
			}
		}

		/* Render characters */
		for (Character c : characters) {
			c.render();
		}

		/* Render sprite effects */
		for (SpriteEffect se : spriteEffects) {
			se.render();
		}

		/* Render projectiles */
		for (Projectile p : projectiles) {
			p.render();
		}

		/* Render bonuses */
		for (Bonus b : bonuses) {
			b.render();
		}

		/* Render victory state */
		if (victory) {
			victorySprite.render();
		}

		glPopMatrix();

	}

	public Tile[][] getTileMatrix() {
		return tileMatrix;
	}

	public void removeProjectile(Projectile projectile) {
		ArmedCharacter origin = projectile.getArmedCharacter();
		origin.addAmmo(1);
		deadProjectiles.add(projectile);
	}

	public void removeTile(int x, int y) {
		// TODO: Collision sounds must be on colliding object
		hitSound.playAsSoundEffect(1.0f, 1.0f, false);
		tileMatrix[x][y] = new Tile(x * 32, y * 32, TileType.DESTROYED_BRICK_WALL);
	}

	public void removeSpriteEffect(SpriteEffect spriteEffect) {
		deadSpriteEffects.add(spriteEffect);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void addSpriteEffect(int x, int y, SpriteType spriteType) {
		spriteEffects.add(new SpriteEffect(x, y, spriteType, this));
	}

	public void addEnemyRespawnAt(int normalizedX, int normalizedY) {
		for (GameObject go : enemyRespawn.getRespawnsList()) {
			if (go.getX() == normalizedX && go.getY() == normalizedY) {
				return;
			}
		}

		enemyRespawn.addRespawn(normalizedX, normalizedY);
		log("Enemy respawn placed on (" + normalizedX + "," + normalizedY + ")");
	}

	public boolean removeEnemyRespawnAt(int x, int y) {
		return enemyRespawn.removeEnemyRespawnAt(x, y);
	}

	public void addEnemyToList(ArmedCharacterType enemyType) {
		enemyRespawn.addEnemy(enemyType);
	}

	public void removeLastEnemyOfList() {
		enemyRespawn.removeLastEnemyOfList();
	}

	public void renderEnemyListAt(int x, int y) {
		glPushMatrix();
		glTranslatef(this.x, this.y, 0);

		enemyRespawn.renderEnemyListAt(x, y);

		glPopMatrix();
	}

	public boolean damageCharacter(Character c, int ammount) {
		c.damage(ammount);
		log(c + " recieves " + ammount + " points of damage");
		hitSound.playAsSoundEffect(1.0f, 1.0f, false);
		if (c.isDead()) {
			removeCharacter(c);
			return true;
		}

		return false;
	}

	private void removeCharacter(Character c) {
		characters.remove(c);
		if (c.equals(player1)) {
			player1 = null;
		} else if (c.equals(player2)) {
			player2 = null;
		}
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setPlayerRespawnAt(int normalizedX, int normalizedY, int playerNo) {
		if (playerNo == 0) {
			player1Respawn = new PlayerRespawn(normalizedX, normalizedY, SpriteType.PLAYER_RESPAWN, this, playerNo);
		} else {
			player2Respawn = new PlayerRespawn(normalizedX, normalizedY, SpriteType.PLAYER_RESPAWN_ORANGE, this, playerNo);
		}

	}

	public void removePlayerRespawn(int playerNo) {
		if (playerNo == 0) {
			if (player1Respawn != null) {
				player1Respawn = null;
				log("Player blue respawn removed");
			}
		} else {
			if (player2Respawn != null) {
				player2Respawn = null;
				log("Player orange respawn removed");
			}
		}
	}

	public void setWinInfo(String winAction, String nextLevel) {
		this.winAction = winAction;
		this.nextLevel = nextLevel;
	}

	public void setGameState(int state) {
		stopEverything();
		TankWar.setGameState(state);
	}

	public void removeBonus(Bonus bonus) {
		deadBonuses.add(bonus);
	}

	public PlayerRespawn getPlayer1Respawn() {
		return player1Respawn;
	}

	public PlayerRespawn getPlayer2Respawn() {
		return player2Respawn;
	}

	public void setBgMusicName(String name) {
		this.musicName = name;
	}

	public Player getPlayer2() {
		return player2;
	}

	public List<GameObject> getCollisionList(GameObject go) {
		List<GameObject> returnObjects = new ArrayList<GameObject>();
		return quad.retrieve(returnObjects, go);
	}

	public void toggleSound() {
		if (music.isPlaying()) {
			music.stop();
		} else {
			music.playAsMusic(1.0f, .5f, true);
		}
	}

	public void setPlayerBlueRespawnAt(int normalizedX, int normalizedY) {
		if (player1Respawn == null || player1Respawn.getX() != normalizedX || player1Respawn.getY() != normalizedY) {
			player1Respawn = new PlayerRespawn(normalizedX, normalizedY, SpriteType.PLAYER_RESPAWN, this, 0);
			log("Player blue respawn placed on (" + normalizedX + "," + normalizedY + ")");
		}
	}

	public void setPlayerOrangeRespawnAt(int normalizedX, int normalizedY) {
		if (player2Respawn == null || player2Respawn.getX() != normalizedX || player2Respawn.getY() != normalizedY) {
			player2Respawn = new PlayerRespawn(normalizedX, normalizedY, SpriteType.PLAYER_RESPAWN_ORANGE, this, 1);
			log("Player orange respawn placed on (" + normalizedX + "," + normalizedY + ")");
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setTileMatrix(Tile[][] tm) {
		this.tileMatrix = tm;
	}

	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}

	public void setLevelNumber(int i) {
		levelNo = i;
	}

	public int getLevelNumber() {
		return levelNo;
	}

	public void setEnemyRespawn(EnemyRespawn enemyRespawn) {
		this.enemyRespawn = enemyRespawn;

	}

	public void setPlayer1Respawn(PlayerRespawn pr) {
		this.player1Respawn = pr;
	}

	public void setPlayer2Respawn(PlayerRespawn pr) {
		this.player2Respawn = pr;
	}

	public void setMusicName(String name) {
		musicName = name;
	}

	public void setMusic(Audio audio) {
		this.music = audio;
	}

	public EnemyRespawn getEnemyRespawn() {
		return enemyRespawn;
	}

	public String getMusicName() {
		return musicName;
	}

	public String getWinAction() {
		return winAction;
	}

	public String getNextLevel() {
		return nextLevel;
	}

	public String getPath() {
		return path;
	}

	public void setLevelEngine(LevelEngine levelEngine) {
		this.levelEngine = levelEngine;
	}

	public void setWinAction(String text) {
		winAction = text;
	}

	public void renderHighlight() {
		int mouseX = (Mouse.getX() / Tile.WIDTH) * Tile.WIDTH;
		int mouseY = Display.getHeight() - (Mouse.getY() / Tile.HEIGHT + 1) * Tile.HEIGHT;
		glColor3f(.8f, .8f, .8f);
		glBegin(GL_QUADS);
		glVertex2f(mouseX, mouseY);
		glVertex2f(mouseX + Tile.WIDTH, mouseY);
		glVertex2f(mouseX + Tile.WIDTH, mouseY + Tile.HEIGHT);
		glVertex2f(mouseX, mouseY + Tile.HEIGHT);
		glEnd();
		glColor3f(1f, 1f, 1f);
	}

	public void onFocus(int x, int y) {
		renderHighlight();
		if (action != null) {
			action.execute();
		}
	}

	public void setOnFocusAction(Action a) {
		this.action = a;
	}
}