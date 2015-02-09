package level;

import core.GameObject;

public class Tile extends GameObject {

	// 16 looks cool
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;

	/* Tile type */
	private TileType type;

	/* Constructor */
	public Tile(int x, int y, TileType type) {
		super(x, y, type.getSpriteType());
		this.type = type;
	}

	public String toString() {
		// TODO: Check this
		return "Tile (" + x + "," + y + ") as " + type.toString();
	}

	public TileType getType() {
		return type;
	}
}
