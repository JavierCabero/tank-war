package entity;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import core.GameObject;
import sprite.SpriteType;
import util.Collision;
import level.Level;
import level.Tile;
import level.TileType;
import ai.Direction;

public class Projectile extends GameObject {

	/* Variables */
	private Direction dir = Direction.UP;

	/* Logic control variables */
	private int speed = 5;
	private Level level;
	private ArmedCharacter armedCharacter;
	private Damage damage;
	private SpriteType effectOnDead = SpriteType.MISSILE_EXPLOSION;

	public Projectile(int x, int y, ProjectileType projectileType, Direction dir, ArmedCharacter armedCharacter, Level level) {
		super(x, y, projectileType.getSpriteType());
		this.dir = dir;
		sprite.setDirection(dir);
		this.level = level;
		this.effectOnDead = projectileType.getEffectOnDead();
		this.damage = new Damage(projectileType.getDamageType());
		this.armedCharacter = armedCharacter;
		this.health = new Health(projectileType.getHealthType());
		// Someday I'll implement centered entities...
		this.x -= getHalfWidth();
		this.y -= getHalfHeight();
	}

	public ArmedCharacter getArmedCharacter() {
		return armedCharacter;
	}

	public int getSpeed() {
		return speed;
	}

	// public void setDirection(Direction dir) {
	// this.dir = dir;
	// }

	// TODO: This shouldn't be here but in the level part with a boolean return
	// value for collision
	public void update() {
		// Move and check collisions
		// This is weird because collision logic is in its own class but player
		// has collision logic outside its class (in Level)
		Tile[][] tileMatrix = level.getTileMatrix();
		int matrixWidth = tileMatrix.length;
		int matrixHeight = tileMatrix[0].length;
		// Move
		switch (dir) {
		case UP:
			moveY(-speed);
			break;
		case LEFT:
			moveX(-speed);
			break;
		case DOWN:
			moveY(speed);
			break;
		case RIGHT:
			moveX(speed);
			break;
		default:
			break;
		}

		// Position data
		int leftX = x / 32;
		int rightX = (x + sprite.getHeight() - 1) / 32;
		int topY = y / 32;
		int bottomY = (y + sprite.getHeight() - 1) / 32;
		if (!(0 <= leftX && rightX < matrixWidth && 0 <= topY && bottomY < matrixHeight)) {
			level.removeProjectile(this);
			return;
		}

		/* Check collision with tiles */
		// TODO: Create tiles that make projectiles bounce
		for (int i = leftX; i <= rightX; i++) {
			for (int j = topY; j <= bottomY; j++) {
				if (tileMatrix[i][j].getType().isSolid()) {
					if (!tileMatrix[i][j].getType().getName().equals(TileType.WATER.getName())) {
						damage(1);
						if (tileMatrix[i][j].getType().isDestructible()) {
							if (armedCharacter.equals(level.getPlayer1())) {
								level.getPlayer1Respawn().increaseScore1(100);
							} else if (armedCharacter.equals(level.getPlayer2())) {
								level.getPlayer2Respawn().increaseScore2(100);
							}
							level.removeTile(i, j);
						}
					}
				}
			}
		}

		/* Check collision with other game objects */
		for (GameObject that : level.getCollisionList(this)) {
			if (!that.equals(this) && !that.equals(armedCharacter) && Collision.rectangleCollision(this, that)
					&& !(armedCharacter.getClass().equals(Player.class) && that.getClass().equals(Player.class))) {
				damage(that.damage(damage.getDamage()));
				if (that.isDead()) {
					Player player1 = level.getPlayer1();
					Player player2 = level.getPlayer2();
					if (armedCharacter.equals(player1)) {
						level.getPlayer1Respawn().increaseScore1(200);
					} else if (armedCharacter.equals(player2)) {
						level.getPlayer2Respawn().increaseScore2(200);
					}
				}
			}
		}
	}

	public void render() {
		glPushMatrix();
		glTranslatef(x, y, 0);
		sprite.render();
		glPopMatrix();
	}

	public SpriteType getEffectOnDead() {
		return effectOnDead;
	}

	public int getDamage() {
		return damage.getDamage();
	}
}
// /* Check collision with characters */
// for (Character c : level.getCharacters()) {
// if (!armedCharacter.equals(c) &&
// !(armedCharacter.getClass().equals(Player.class) &&
// c.getClass().equals(Player.class))
// && Collision.rectangleCollision(x, y, sprite.getWidth(), sprite.getHeight(),
// c.getX(), c.getY(), c.getWidth(), c.getHeight())) {
// if (level.damageCharacter(c, damage.getDamage())) {
// if (armedCharacter.equals(level.getPlayer1())) {
// int valor = 0;
// ArmedCharacterType act = ((ArmedCharacter) c).getType();
// if (act.equals(ArmedCharacterType.ENEMY_BASIC)) {
// valor = 300;
// } else if (act.equals(ArmedCharacterType.ENEMY_STRONG)) {
// valor = 500;
// } else {
// valor = 2500;
// }
// if (armedCharacter.equals(level.getPlayer1())) {
// level.getPlayer1Respawn().increaseScore1(valor);
// } else if (armedCharacter.equals(level.getPlayer2())) {
// level.getPlayer2Respawn().increaseScore2(valor);
// }
// } else if (armedCharacter.equals(level.getPlayer2())) {
// int valor = 0;
// ArmedCharacterType act = ((ArmedCharacter) c).getType();
// if (act.equals(ArmedCharacterType.ENEMY_BASIC)) {
// valor = 300;
// } else if (act.equals(ArmedCharacterType.ENEMY_STRONG)) {
// valor = 500;
// } else {
// valor = 2500;
// }
// if (armedCharacter.equals(level.getPlayer1())) {
// level.getPlayer1Respawn().increaseScore1(valor);
// } else if (armedCharacter.equals(level.getPlayer2())) {
// level.getPlayer2Respawn().increaseScore2(valor);
// }
// }
// }
// if (health.isAlive())
// damage(1);
// }
// }
//
// /* Check collision with other projectiles */
// for (Projectile that : level.getProjectiles()) {
// if (!that.equals(this)
// && Collision.rectangleCollision(x, y, sprite.getWidth(), sprite.getHeight(),
// that.getX(), that.getY(), that.getWidth(), that.getHeight())) {
// if (health.isAlive())
// damage(1);
// that.damage(damage.getDamage());
// if (that.isDead()) {
// Player player1 = level.getPlayer1();
// Player player2 = level.getPlayer2();
// if (armedCharacter.equals(player1) &&
// !that.getArmedCharacter().equals(player2)) {
// level.getPlayer1Respawn().increaseScore1(200);
// } else if (armedCharacter.equals(player2) &&
// !that.getArmedCharacter().equals(player1)) {
// level.getPlayer2Respawn().increaseScore2(200);
// }
// }
// }
// }
/* Player hitbox is 26x26 */
