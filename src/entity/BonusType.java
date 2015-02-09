package entity;

import sprite.SpriteType;

public enum BonusType {

	GIANT_ROCKET(1024, SpriteType.GIANT_ROCKET_BONUS, ProjectileType.GIANT_ROCKET, 1), 
	EPIC_CANNON(1024, SpriteType.EPIC_CANNON_BONUS, WeaponType.EPIC_CANNON,	250), 
	INVULNERABILITY(1024, SpriteType.INVULNERABILITY_BONUS, 390);

	/* Variables */
	private int type = 0;
	private int remainingTime = 1024;
	private SpriteType spriteType = SpriteType.GIANT_ROCKET_BONUS;
	private ProjectileType projectileType = ProjectileType.GIANT_ROCKET;
	private int ammo = 1;
	private int time = 128;
	private WeaponType weaponType = WeaponType.SLOW_CANNON;

	/* Constructors */
	BonusType(int remainingTime, SpriteType spriteType) {
		this.remainingTime = remainingTime;
		this.spriteType = spriteType;
	}

	// Giant Rocket
	BonusType(int remainingTime, SpriteType spriteType, ProjectileType projectileType, int ammo) {
		this(remainingTime, spriteType);
		type = 0;
		this.projectileType = projectileType;
		this.ammo = ammo;
	}

	// Epic cannon
	BonusType(int remainingTime, SpriteType spriteType, int time) {
		this(remainingTime, spriteType);
		type = 1;
		this.time = time;
	}

	// Invulnerability
	BonusType(int remainingTime, SpriteType spriteType, WeaponType weaponType, int time) {
		this(remainingTime, spriteType);
		type = 2;
		this.time = time;
		this.weaponType = weaponType;
	}

	/* Getters */
	public int getType() {
		return type;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public ProjectileType getProjectileType() {
		return projectileType;
	}

	public int getAmmo() {
		return ammo;
	}

	public SpriteType getSpriteType() {
		return spriteType;
	}

	public int getTime() {
		return time;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}
}
