package sprite;

/**
 * This class contains all the information about every sprite in the game. 
 * Tiles usually have only 1 and they are non-animated and non-directional.
 * @author Kay
 *	
 */
public enum SpriteType {

	/* Examples */
	CANNON_BALL("CANNON_BALL", 2, false), 
	NULL("NULL", 1, false), 
	DESTROYED_BRICK_WALL("DESTROYED_BRICK_WALL", 1, false),
	BRICK_WALL("BRICK_WALL", 1, false),
	DIRT_GROUND("DIRT_GROUND", 1, false), 
	MISSILE_EXPLOSION("MISSILE_EXPLOSION", 2, false), 
	PLAYER_BASIC("PLAYER_BASIC", 2, true),
	ENEMY_BASIC("ENEMY_BASIC", 2, true), 
	ENEMY_RESPAWN("ENEMY_RESPAWN", 1, false),
	ENEMY_BASIC_DELETE("ENEMY_BASIC_DELETE", 1, false), 
	ENEMY_STRONG("ENEMY_STRONG", 2, true), 
	MENU_BACKGROUND("MENU_BACKGROUND", 1, false), 
	WARNING_YELLOW("WARNING_YELLOW", 2, false), 
	LEVEL_LIMIT("LEVEL_LIMIT", 1, false), 
	PLAYER_RESPAWN("PLAYER_RESPAWN", 1, false), 
	GAME_OVER("GAME_OVER", 1, false), 
	VICTORY_SPRITE("VICTORY_SPRITE", 1, false), 
	CREDITS("CREDITS", 1, false), 
	ENEMY_RED("ENEMY_RED", 2, true), 
	LOADING("LOADING", 1, false), 
	PAUSED("PAUSED", 1, false), 
	GIANT_ROCKET("GIANT_ROCKET", 2, true),
	GIANT_MISSILE_EXPLOSSION("GIANT_MISSILE_EXPLOSSION", 2, false), 
	GIANT_ROCKET_BONUS("GIANT_ROCKET_BONUS", 1, false), 
	INVULNERABILITY_BONUS("INVULNERABILITY_BONUS", 1, false),
	EPIC_CANNON_BONUS("EPIC_CANNON_BONUS", 1, false), 
	INFO_PANEL("INFO_PANEL", 1, false),
	PLAYER_SMALL("PLAYER_SMALL", 1, false), 
	PLAYER_RESPAWN_ORANGE("PLAYER_RESPAWN_ORANGE", 1, false), 
	ONE_PLAYER_BUTTON("ONE_PLAYER_BUTTON", 1, false),
	TWO_PLAYERS_BUTTON("TWO_PLAYERS_BUTTON", 1, false), 
	CONTROLS_BUTTON("CONTROLS_BUTTON", 1, false),
	RANKINGS_BUTTON("RANKINGS_BUTTON", 1, false),
	EXIT_BUTTON("EXIT_BUTTON", 1, false), 
	PLAYER_BLUE("PLAYER_BLUE", 2, true),
	PLAYER_ORANGE("PLAYER_ORANGE", 2, true),
	PLAYER_BLUE_SMALL("PLAYER_BLUE_SMALL", 1, false),
	PLAYER_ORANGE_SMALL("PLAYER_ORANGE_SMALL", 1, false), 
	GRASS_GROUND("GRASS_GROUND", 1, false),
	STONE_WALL("STONE_WALL",1,false),
	WATER("WATER", 1, false), 
	EARTH_GROUND("EARTH_GROUND", 1, false),
	SAND_GROUND("SAND_GROUND", 1, false), 
	CONTROLS("CONTROLS", 1, false), 
	EDITOR_BUTTON("EDITOR_BUTTON", 1, false), 
	OPEN_BUTTON("OPEN_BUTTON", 1, false), 
	NEW_BUTTON("NEW_BUTTON", 1, false),
	SAVE_BUTTON("SAVE_BUTTON", 1, false),
	CLEAR_BUTTON("CLEAR_BUTTON", 1, false), 
	RELOAD_BUTTON("RELOAD_BUTTON", 1, false), 
	BLANK_BUTTON("BLANK_BUTTON", 1, false), 
	START_GAME("START_GAME", 1, false), 
	BACK_BUTTON("BACK_BUTTON", 1, false), 
	BACK_WHITE_BUTTON("BACK_WHITE_BUTTON", 1, false);

	/* Variables */
	private String name;
	private int maxState;
	private boolean directional;

	/* Constructor */
	SpriteType(String name, int maxState, boolean directional) {
		this.name = name;
		this.maxState = maxState;
		this.directional = directional;
	}

	/* Methods */
	public String getName() {
		return name;
	}

	public int getMaxState() {
		return maxState;
	}

	public boolean isDirectional() {
		return directional;
	}
}
