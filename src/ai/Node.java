package ai;

/* 4 directional */
public class Node {

	private Direction direction = Direction.NODIR;
	private double g;
	private double h;
	private double f = Integer.MAX_VALUE;
	private int x;
	private int y;

	public Node(int x, int y, double g, double h, Direction direction) {
		this.x = x;
		this.y = y;
		this.g = g;
		this.h = h;
		this.f = g + h;
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
		this.f = this.g + this.h;
	}

	public double getH() {
		return h;
	}

	public double getF() {
		return f;
	}

	public boolean equals(Object o) {
		return (this.x == ((Node) o).getX()) && (this.y == ((Node) o).getY());
	}
}
