package entity;

import ai.AIType;
import sprite.SpriteType;

public enum ArmedCharacterType {

	PLAYER_BASIC(SpriteType.PLAYER_BASIC, WeaponType.MEDIUM_CANNON, HealthType.LOW),
	ENEMY_BASIC(SpriteType.ENEMY_BASIC, WeaponType.MEDIUM_CANNON, HealthType.LOW), 
	ENEMY_STRONG(SpriteType.ENEMY_STRONG, WeaponType.SLOW_CANNON, HealthType.MEDIUM),
	ENEMY_RED(SpriteType.ENEMY_RED, WeaponType.FAST_CANNON, HealthType.SUPER_HIGH, AIType.SUPER_SMART);

	/* Variables */
	private SpriteType spriteType = SpriteType.NULL;
	private WeaponType weaponType = WeaponType.MEDIUM_CANNON;
	private HealthType healthType = HealthType.LOW;
	private AIType aiType = AIType.NORMAL;

	/* Constructor */
	ArmedCharacterType(SpriteType spriteType, WeaponType weaponType, HealthType healthType) {
		this.spriteType = spriteType;
		this.weaponType = weaponType;
		this.healthType = healthType;
	}

	ArmedCharacterType(SpriteType spriteType, WeaponType weaponType, HealthType healthType, AIType aiType) {
		this.spriteType = spriteType;
		this.weaponType = weaponType;
		this.healthType = healthType;
		this.aiType = aiType;
	}

	/* Methods */
	public void setSpriteType(SpriteType spriteType) {
		this.spriteType = spriteType;
	}

	public SpriteType getSpriteType() {
		return spriteType;
	}

	public String getSpriteName() {
		return spriteType.getName();
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public HealthType getHealthType() {
		return healthType;
	}

	public void setHealthType(HealthType healthType) {
		this.healthType = healthType;
	}

	public AIType getAIType() {
		return aiType;
	}

}
