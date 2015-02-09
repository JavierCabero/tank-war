package entity;

import java.util.ArrayList;

import level.Level;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.input.Keyboard;



import sprite.Sprite;
import sprite.SpriteType;

import ai.Direction;

public class Player extends ArmedCharacter {

	private static Controller ctrllr;
	private int number = 0;
	private ArrayList<Controller> foundControllers = new ArrayList<Controller>();

	/* Constructor */
	public Player(int x, int y, ArmedCharacterType armedCharacterType, Level level, int number) {
		super(x, y, armedCharacterType, level);
		this.number = number;
		if (number == 0) {
			sprite = new Sprite(SpriteType.PLAYER_BLUE);
			sprite.setBlinkTime(128);

			/* We search for controllers */
			searchForControllers();
			if (!foundControllers.isEmpty()) {
				ctrllr = foundControllers.get(0);
			}

		} else {
			sprite = new Sprite(SpriteType.PLAYER_ORANGE);
			sprite.setBlinkTime(128);
		}
	}

	public void input() {

		Direction dir = Direction.NODIR;

		if (number == 0) {

			if (ctrllr != null) {
				Component[] components = ctrllr.getComponents();
				Float value = components[12].getPollData();
				if (value.equals(Component.POV.UP) || value.equals(Component.POV.UP_RIGHT) || value.equals(Component.POV.UP_LEFT)) {
					dir = Direction.UP;
				} else if (value.equals(Component.POV.LEFT) || value.equals(Component.POV.DOWN_LEFT)) {
					dir = Direction.LEFT;
				} else if (value.equals(Component.POV.DOWN)) {
					dir = Direction.DOWN;
				} else if (value.equals(Component.POV.RIGHT) || value.equals(Component.POV.DOWN_RIGHT)) {
					dir = Direction.RIGHT;
				}
				if (dir != Direction.NODIR) {
					this.dir = dir;
					move();
				}
				ctrllr.poll();
				if (components[0].getPollData() != 0 && shoot()) {
					ProjectileType pt = getWeapon().getNextProjectileType();
					pt.getShootAudio().playAsSoundEffect(1f, 1f, false);
					level.addProjectile(new Projectile(getWeaponX(), getWeaponY(), pt, getDirection(), this, level));
				}
			} else {
				if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
					dir = Direction.UP;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
					dir = Direction.LEFT;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					dir = Direction.DOWN;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
					dir = Direction.RIGHT;
				}

				if (dir != Direction.NODIR) {
					this.dir = dir;
					move();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && shoot()) {
					ProjectileType pt = getWeapon().getNextProjectileType();
					pt.getShootAudio().playAsSoundEffect(1f, 1f, false);
					level.addProjectile(new Projectile(getWeaponX(), getWeaponY(), pt, getDirection(), this, level));
				}
			}

		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
				dir = Direction.UP;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
				dir = Direction.LEFT;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
				dir = Direction.DOWN;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
				dir = Direction.RIGHT;
			}

			if (dir != Direction.NODIR) {
				this.dir = dir;
				move();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && shoot()) {
				ProjectileType pt = getWeapon().getNextProjectileType();
				pt.getShootAudio().playAsSoundEffect(1f, 1f, false);
				level.addProjectile(new Projectile(getWeaponX(), getWeaponY(), pt, getDirection(), this, level));
			}
		}

	}

	public void addBonusProjectile(ProjectileType projectileType, int ammount) {
		weapon.addBonusProjectiles(projectileType, ammount);
	}

	public void setInvulnerabilityTime(int time) {
		health.setInvulnerabilityTime(time);
		sprite.setBlinkTime(time);
	}

	public void setTemporaryWeapon(WeaponType weaponType, int time) {
		weapon.setTemporaryWeapon(weaponType, time);
	}

	public void update() {
		super.update();
		input();
	}

	private void searchForControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.STICK || controller.getType() == Controller.Type.GAMEPAD
					|| controller.getType() == Controller.Type.WHEEL || controller.getType() == Controller.Type.FINGERSTICK) {
				// Add new controller to the list of all controllers.
				foundControllers.add(controller);
			}
		}
	}
}

/* Player hitbox is now 26x26 */