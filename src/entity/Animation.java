package entity;

import util.Updatable;

public class Animation implements Updatable {

	/* Animation */
	protected int speed = 5;
	protected int nextChange = speed;
	protected int state = 0;
	protected int maxState = 1;
	protected boolean alwaysAnimate = false;

	/* Constructor */
	public Animation(int maxState) {
		this.maxState = maxState;
	}

	/* Methods */
	public void update() {
		if (nextChange <= 0) {
			state = (state + 1) % maxState;
			nextChange = speed;
		} else {
			nextChange--;
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public int getMaxState() {
		return maxState;
	}

	public int getState() {
		return state;
	}

	public void setAlwaysAnimate(boolean value) {
		this.alwaysAnimate = value;
	}

	public boolean isAlwaysAnimate() {
		return this.alwaysAnimate;
	}

}
