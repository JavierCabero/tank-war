package entity;

import util.Collision;
import audio.AudioBuffer;
import audio.AudioType;
import core.GameObject;
import level.Level;

public class Bonus extends GameObject {

	private static boolean debug = true;
	private BonusType bonusType = BonusType.GIANT_ROCKET;
	private int remainingTime = 1024;
	private int type = 0;
	private Level level;
	private ProjectileType projectileType;
	private int ammount = 1;
	private int time = 128;
	private WeaponType weaponType;

	public Bonus(int x, int y, BonusType bonusType, Level level) {
		super(x, y, bonusType.getSpriteType());
		this.setBonusType(bonusType);
		this.type = bonusType.getType();
		this.ammount = bonusType.getAmmo();
		this.projectileType = bonusType.getProjectileType();
		this.remainingTime = bonusType.getRemainingTime();
		this.level = level;
		this.time = bonusType.getTime();
		this.weaponType = bonusType.getWeaponType();
	}

	public void update() {
		if (remainingTime == 128) {
			sprite.setBlinkTime(128);
		} else if (remainingTime <= 0) {
			level.removeBonus(this);
		}
		remainingTime--;
		Player player1 = level.getPlayer1();

		if (player1 != null && Collision.rectangleCollision(x, y, getWidth(), getHeight(), player1.getX(), player1.getY(), player1.getWidth(), player1.getHeight())) {
			log("Bonus used!");
			switch (type) {
			case 0:
				player1.addBonusProjectile(projectileType, ammount);
				break;
			case 1:
				player1.setInvulnerabilityTime(time);
				AudioBuffer.getAudio(AudioType.INVINCIBLE).playAsSoundEffect(1f, 1f, false);
				break;
			case 2:
				player1.setTemporaryWeapon(weaponType, time);
				break;
			}

			level.removeBonus(this);
		}
		Player player2 = level.getPlayer2();
		if (player2 != null && Collision.rectangleCollision(x, y, getWidth(), getHeight(), player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight())) {
			log("Bonus used!");
			switch (type) {
			case 0:
				player2.addBonusProjectile(projectileType, ammount);
				break;
			case 1:
				player2.setInvulnerabilityTime(time);
				AudioBuffer.getAudio(AudioType.INVINCIBLE).playAsSoundEffect(1f, 1f, false);
				break;
			case 2:
				player2.setTemporaryWeapon(weaponType, time);
				break;
			}

			level.removeBonus(this);
		}
	}

	public void log(String string) {
		if (debug)
			System.out.println("[Bonus] " + string);
	}

	public BonusType getBonusType() {
		return bonusType;
	}

	public void setBonusType(BonusType bonusType) {
		this.bonusType = bonusType;
	}
}
