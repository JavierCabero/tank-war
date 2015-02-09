package core;

import gui.Menu;
import level.LevelEditor;
import level.LevelEngine;

import org.lwjgl.input.Keyboard;

import sprite.Sprite;
import sprite.SpriteType;

public class GameStateFactory {

	/* Game state */
	public static final int MENU = 0;
	public static final int EXIT = 1;
	public static final int START_GAME = 2;
	public static final int GAME_OVER = 3;
	public static final int CREDITS = 4;
	public static final int CONTROLS = 5;
	public static final int LEVEL_EDITOR = 6;

	private static GameState[] gameStates = new GameState[7];

	private static boolean debug = true;

	public static GameState setGameState(int gameState) {

		switch (gameState) {
		case MENU:
			log("Menu");
			if (gameStates[MENU] == null) {
				gameStates[MENU] = new Menu();
			}
			return gameStates[MENU];
		case EXIT:
			log("Exiting...");
			Main.setCloseRequested(true);
			return null;
		case START_GAME:
			log("Starting game...");
			gameStates[START_GAME] = new LevelEngine(((Menu) gameStates[MENU]).isTwoPlayers());
			return gameStates[START_GAME];
		case GAME_OVER:
			log("Game Over");
			if (gameStates[GAME_OVER] == null) {
				gameStates[GAME_OVER] = new GameState() {
					/* Variables */
					private Sprite sprite = new Sprite(SpriteType.GAME_OVER);
					private final int DELAY = 180;
					private int counter = DELAY;

					public void update() {
						sprite.updateAnimation();
					}

					public void render() {
						sprite.render();
					}

					public void setGameState(int state) {
						Main.setGameState(state);
					}

					public void input() {
						if (counter > 0) {
							counter--;
						} else {
							setGameState(GameStateFactory.MENU);
							counter = DELAY;
						}
					}
				};
			}
			return gameStates[GAME_OVER];
		case CREDITS:
			log("Credits");
			if (gameStates[CREDITS] == null) {
				gameStates[CREDITS] = new GameState() {

					/* Variables */
					private Sprite sprite = new Sprite(SpriteType.CREDITS);

					public void update() {
						sprite.updateAnimation();
					}

					public void render() {
						sprite.render();
					}

					public void setGameState(int state) {
						Main.setGameState(state);
					}

					public void input() {
						if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
							setGameState(GameStateFactory.MENU);
						}
					}
				};
			}
			return gameStates[CREDITS];
		case CONTROLS:
			log("Controls");
			if (gameStates[CONTROLS] == null) {
				gameStates[CONTROLS] = new GameState() {
					
					/* Variables */
					private Sprite sprite = new Sprite(SpriteType.CONTROLS);

					/* Methods */
					public void render() {
						sprite.render();
					}

					public void update() {
						sprite.updateAnimation();
					}

					public void input() {
						if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
							setGameState(GameStateFactory.MENU);
						}
					}

					public void setGameState(int state) {
						Main.setGameState(state);
					}

				};
			}
			return gameStates[CONTROLS];
		case LEVEL_EDITOR:
			log("Level editor");
			if (gameStates[LEVEL_EDITOR] == null) {
				gameStates[LEVEL_EDITOR] = new LevelEditor();
			}
			Main.setUpScreen(Main.EDITOR_WIDTH, Main.EDITOR_HEIGHT, Main.EDITOR_TITLE);
			return gameStates[LEVEL_EDITOR];
		default:
			System.out.println("[Main] Invalid action: " + gameState);
		}
		return null;
	}

	public static void log(String msg) {
		if (debug) {
			System.out.println("[GameStateFactory] " + msg);
		}
	}

}
