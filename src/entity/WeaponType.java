package entity;


public enum WeaponType {

	SLOW_CANNON(40, 3),
	MEDIUM_CANNON(25, 3),
	FAST_CANNON(10, 3),
	EPIC_CANNON(5, 500),
	LEGENDARY_ROCKET_LAUNCHER(64, 1, ProjectileType.GIANT_ROCKET);
	
	/* Variables */
	private int fireRate = 10;
	private int timeToShoot = 0;
	private int ammo = 5;
	private ProjectileType projectileType = ProjectileType.CANNON_BALL;
	
	/* Constructors */
	WeaponType(int fireRate, int ammo) {
		this.fireRate = fireRate;
		this.timeToShoot = 0;
		this.ammo = ammo;
	}
	
	WeaponType(int fireRate, int ammo, ProjectileType projectileType){
		this(fireRate, ammo);
		this.projectileType = projectileType;
	}

	public int getFireRate() {
		return fireRate;
	}

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	public int getTimeToShoot() {
		return timeToShoot;
	}

	public void setTimeToShoot(int timeToShoot) {
		this.timeToShoot = timeToShoot;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public ProjectileType getProjectileType() {
		return projectileType;
	}
}
