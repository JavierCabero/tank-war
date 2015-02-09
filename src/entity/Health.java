package entity;

import util.Math;
import util.Updatable;

public class Health implements Updatable {
	/* Variables */
	private static boolean debug = true;
	private int health = 1;
	private int maxHealth = 1;
	private int invulnerabilityTime = 0;

	/* Constructor */
	public Health(HealthType healthType) {
		this.health = healthType.getHealth();
		this.maxHealth = healthType.getMaxHealth();
	}

	public Health(HealthType healthType, int invulnerability) {
		this(healthType);
		this.invulnerabilityTime = invulnerability;
	}

	/* Methods */
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void heal(int ammount) {
		health += Math.min(ammount, maxHealth - ammount);
	}

	/**
	 * 
	 * @param ammount
	 * @return the ammount of damage done
	 */
	public int damage(int damage) {
		if (damage < 0) {
			log("Warning: negative damage");
		}
		if (invulnerabilityTime == 0) {
			health -= damage;
		}
		return min(damage, damage + health);
	}

	private void log(String string) {
		if (debug) {
			System.out.println("[Health] " + string);
		}
	}

	private int min(int a, int b) {
		return a < b ? a : b;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void update() {
		if (invulnerabilityTime > 0) {
			invulnerabilityTime--;
		}
	}

	public boolean isAlive() {
		return health > 0;
	}

	public void setInvulnerabilityTime(int time) {
		invulnerabilityTime = time;
	}

}
