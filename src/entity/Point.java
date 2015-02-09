package entity;

/**
 * Almost every class in the game contains an x and y that means their location.
 * However, this class is used to store information only in enemy spawns.
 * 
 * @author Kay
 * 
 */
public class Point {

	private int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
