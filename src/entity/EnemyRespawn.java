package entity;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import level.Level;

import org.lwjgl.util.Renderable;

import sprite.Sprite;
import sprite.SpriteType;
import util.Updatable;
import core.GameObject;

/**
 * Spawns enemy at the (x,y) location at random intervals of time. Its respawns
 * extends class GameObject to be able to be rendered in level editor.
 * 
 * @author Kay
 * 
 */
public class EnemyRespawn implements Updatable, Renderable {

	/* Data */
	private List<ArmedCharacterType> enemyList;
	private List<GameObject> respawnsList;

	/* Logic */
	private int spawn_rate = 384;
	private int next_spawn = spawn_rate;

	/* Auxiliary */
	private Level level;
	private Random random = new Random();
	private GameObject nextRespawn;

	/* Constructor */
	public EnemyRespawn(Level level) {
		this.level = level;
		enemyList = new ArrayList<ArmedCharacterType>();
		respawnsList = new ArrayList<GameObject>();
		next_spawn = spawn_rate / 2 + random.nextInt(spawn_rate);
	}

	/* Methods */
	public void addEnemy(ArmedCharacterType enemyType) {
		enemyList.add(enemyType);
	}

	public void addRespawn(int x, int y) {
		respawnsList.add(new GameObject(x, y, SpriteType.ENEMY_RESPAWN));
	}

	public List<ArmedCharacterType> getEnemyList() {
		return enemyList;
	}

	public List<GameObject> getRespawnsList() {
		return respawnsList;
	}

	public void update() {
		if (!enemyList.isEmpty()) {
			if (next_spawn > 0) {
				if (next_spawn == 64) {
					nextRespawn = respawnsList.get(random.nextInt(respawnsList.size()));
					level.addSpriteEffect(nextRespawn.getX(), nextRespawn.getY(), SpriteType.WARNING_YELLOW);
				}
				next_spawn--;
			} else {
				level.addEnemyAt(nextRespawn.getX(), nextRespawn.getY(), enemyList.remove(0));
				next_spawn = spawn_rate / 2 + random.nextInt(spawn_rate);
			}
		}
	}

	public void render() {
		for (GameObject go : respawnsList) {
			go.render();
		}
	}

	public boolean removeEnemyRespawnAt(int x, int y) {
		int i = 0;
		while (i < respawnsList.size()) {
			if (respawnsList.get(i).containsPoint(x, y)) {
				return respawnsList.remove(i) != null;
			}
			i++;
		}
		return false;
	}

	public void removeLastEnemyOfList() {
		if (enemyList.size() > 0) {
			enemyList.remove(enemyList.size() - 1);
		}
	}

	public void renderEnemyListAt(int x, int y) {
		glPushMatrix();
		glTranslatef(x, y, 0);
		for (int i = 0; i < enemyList.size(); i++) {
			glTranslatef(28, 0, 0);
			new Sprite(enemyList.get(i).getSpriteType()).render();
			if ((i + 1) % 4 == 0) {
				glTranslatef(-112, 30, 0);
			}
		}
		glPopMatrix();
	}

	public boolean noMoreEnemies() {
		return enemyList.isEmpty();
	}

	public void renderEnemyList() {
//		int offset = Display.getWidth() - 64;
		glPushMatrix();
		glTranslatef(638, 486, 0);
		for (int i = 0; i < enemyList.size(); i++) {
			glTranslatef(28, 0, 0);
			new Sprite(enemyList.get(i).getSpriteType()).render();
			if ((i + 1) % 4 == 0) {
				glTranslatef(-112, 30, 0);
			}
		}
		glPopMatrix();
	}

	public void clear() {
		enemyList.clear();
		respawnsList.clear();
	}

}
