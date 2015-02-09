package entity;

import java.util.LinkedList;
import java.util.List;

import util.Updatable;


public class Weapon implements Updatable {

	/* Variables */
	private int fireRate = 10;
	private int timeToShoot = 0;
	private int ammo = 5;
	private ProjectileType projectileType = ProjectileType.CANNON_BALL;
	private List<ProjectileType> bonusProjectiles = new LinkedList<ProjectileType>();
	private WeaponType weaponType;
	private int temporaryWeaponTime = 0;

	/* Methods */
	public Weapon(WeaponType weaponType) {
		this.fireRate = weaponType.getFireRate();
		this.timeToShoot = 0;
		this.ammo = weaponType.getAmmo();
		this.projectileType = weaponType.getProjectileType();
		this.weaponType = weaponType;
	}

	private void loadWeaponType(WeaponType weaponType) {
		this.fireRate = weaponType.getFireRate();
		this.timeToShoot = 0;
		this.ammo = weaponType.getAmmo();
		this.projectileType = weaponType.getProjectileType();
	}

	public boolean isReadyToFire() {
		return timeToShoot <= 0;
	}

	public int getAmmo() {
		return ammo;
	}

	// public void setAmmo(int i) {
	// ammo = i;
	// }

	public void addAmmo(int i) {
		ammo += i;
	}

	/**
	 * 
	 * @return true if it did shoot.
	 */
	public boolean shoot() {
		if (timeToShoot == 0 && ammo > 0) {
			timeToShoot = fireRate;
			ammo--;
			
			return true;
		} else {
			return false;
		}
	}

	public void update() {
		if (timeToShoot > 0)
			timeToShoot--;
		if (temporaryWeaponTime > 1) {
			temporaryWeaponTime--;
		} else if (temporaryWeaponTime == 1) {
			loadWeaponType(weaponType);
			temporaryWeaponTime--;
		}
	}

	public ProjectileType getNextProjectileType() {
		if (!bonusProjectiles.isEmpty()) {
			return bonusProjectiles.remove(0);
		}
		return projectileType;
	}

	public void addBonusProjectiles(ProjectileType projectileType, int ammount) {
		for (int i = 0; i < ammount; i++) {
			bonusProjectiles.add(projectileType);
		}
	}

	public void setTemporaryWeapon(WeaponType weaponType, int time) {
		temporaryWeaponTime = time;
		loadWeaponType(weaponType);
	}
}
