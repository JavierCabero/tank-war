package entity;

public enum HealthType {

	LOW(1),
	MEDIUM(3),
	HIGH(10), 
	SUPER_HIGH(20), 
	INFINITE(-1);
	
	/* Variables */
	private int health = 1;
	private int maxHealth = 1;

	/* Constructor */
	HealthType (int maxHealth){
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
	HealthType(int maxHealth, int health) {
		this.maxHealth = maxHealth;
		this.health = health;
	}	

	/* Methods */
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

}
