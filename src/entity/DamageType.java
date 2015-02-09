package entity;

public enum DamageType {

	LOW(1),
	MEDIUM(3),
	HIGH(7);
	
	/* Variables */
	private int damage = 1;
	
	/* Constructor */
	DamageType(int damage){
		this.damage = damage;
	}

	/* Methods */
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
