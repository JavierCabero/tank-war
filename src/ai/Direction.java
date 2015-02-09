package ai;

public enum Direction {

	NODIR(-1), UP(0), LEFT(1), DOWN(2), RIGHT(3);

	private int direction = 0;

	Direction(int d) {
		this.direction = d;
	}

	public int getValue() {
		return direction;
	}

	public String toString() {
		switch (direction) {
		case 0:
			return "UP   ";
		case 1:
			return "LEFT ";
		case 2:
			return "DOWN ";
		case 3:
			return "RIGHT";
		default:
			return "NODIR";

		}
	}
}
