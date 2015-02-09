package entity;

public class Damage {

	/* Variables */
	private int damage = 1;

	/* Constructor */
	public Damage(DamageType damageType) {
		this.damage = damageType.getDamage();
	}

	/* Methods */
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
