package level;

import sprite.SpriteType;

public enum TileType {

	NULL(SpriteType.NULL, true, false), 
	DESTROYED_BRICK_WALL(SpriteType.DESTROYED_BRICK_WALL, false, false),
	BRICK_WALL(SpriteType.BRICK_WALL,true, true),
	STONE_WALL(SpriteType.STONE_WALL,true,true),
	DIRT_GROUND(SpriteType.DIRT_GROUND, false, false), 
	GRASS_GROUND(SpriteType.GRASS_GROUND, false, false), 
	LEVEL_LIMIT(SpriteType.LEVEL_LIMIT, true, false), 
	WATER(SpriteType.WATER, true, false), 
	EARTH_GROUND(SpriteType.EARTH_GROUND,false,false),
	SAND_GROUND(SpriteType.SAND_GROUND,false,false);

	public SpriteType spriteType;
	public boolean solid;
	public boolean destructible;

	TileType(SpriteType spriteType, boolean solid, boolean destructible) {
		this.spriteType = spriteType;
		this.solid = solid;
		this.destructible = destructible;
	}

	public String getName() {
		return spriteType.getName();
	}

	public SpriteType getSpriteType() {
		return spriteType;
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isDestructible() {
		return destructible;
	}
	// Maybe this is not needed because of enum class implementation
	// public String toString(){
	// return spriteType.getName();
	// }
}
