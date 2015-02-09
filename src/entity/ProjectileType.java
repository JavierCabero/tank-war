package entity;

import org.newdawn.slick.openal.Audio;

import audio.AudioBuffer;
import audio.AudioType;


import sprite.SpriteType;

public enum ProjectileType {

	CANNON_BALL(SpriteType.CANNON_BALL, DamageType.LOW), 
	GIANT_ROCKET(SpriteType.GIANT_ROCKET, DamageType.MEDIUM, SpriteType.GIANT_MISSILE_EXPLOSSION, HealthType.HIGH);
	
	/* Variables */
	private SpriteType spriteType;
	private DamageType damageType;
	private SpriteType effectOnDead = SpriteType.MISSILE_EXPLOSION;
	private HealthType healthType = HealthType.LOW;
	private Audio shootAudio = AudioBuffer.getAudio(AudioType.CANNON_SHOOT);
	
	/* Constructor */
	ProjectileType(SpriteType spriteType, DamageType damageType){
		this.spriteType = spriteType;
		this.damageType = damageType;
	}
	
	ProjectileType(SpriteType spriteType, DamageType damageType, SpriteType effectOnDead){
		this(spriteType, damageType);
		this.effectOnDead = effectOnDead;
	}
	ProjectileType(SpriteType spriteType, DamageType damageType, SpriteType effectOnDead, HealthType healthType){
		this(spriteType, damageType, effectOnDead);
		this.healthType = healthType;
	}

	/* Methods */
	public SpriteType getSpriteType() {
		return spriteType;
	}

	public void setSpriteType(SpriteType spriteType) {
		this.spriteType = spriteType;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public SpriteType getEffectOnDead() {
		return effectOnDead;
	}

	public HealthType getHealthType() {
		return healthType;
	}

	public Audio getShootAudio() {
		return shootAudio;
	}
}
