package entity;

import level.Level;
import ai.Direction;

/**
 * This class contains a weapon component.
 * 
 * @author Kay
 * 
 */
public class ArmedCharacter extends Character {

	/* Weapon */
	protected Weapon weapon;
	protected ArmedCharacterType type;
	
	/* Constructor */
	public ArmedCharacter(int x, int y, ArmedCharacterType armedCharacterType, Level level) {
		super(x, y, armedCharacterType.getSpriteType(), level);
		this.type = armedCharacterType;
		weapon = new Weapon(armedCharacterType.getWeaponType());
		health = new Health(armedCharacterType.getHealthType(), 128);
		sprite.setBlinkTime(128);
	}

	/* Methods delegated to weapon */
	/**
	 * Tells the weapon component to shoot.
	 * 
	 * @return true if the weapon did shoot.
	 */
	public boolean shoot() {
		return weapon.shoot();
	}

	public int getAmmo() {
		return weapon.getAmmo();
	}

	public boolean isReadyToShoot() {
		return weapon.isReadyToFire();
	}

	public void addAmmo(int quantity) {
		weapon.addAmmo(quantity);
	}

	public int getWeaponX() {
		if (dir.equals(Direction.UP) || dir.equals(Direction.DOWN)) {
			return x + getHalfWidth();
		} else if (dir.equals(Direction.LEFT)) {
			return x;
		} else {
			return x + getWidth();
		}
	}

	public int getWeaponY() {
		if (dir.equals(Direction.LEFT) || dir.equals(Direction.RIGHT)) {
			return y + getHalfHeight();
		} else if (dir.equals(Direction.UP)) {
			return y;
		} else {
			return y + getHeight();
		}
	}

	public void update() {
		super.update();
		weapon.update();
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public ArmedCharacterType getType() {
		return type;
	}
}
