package level;

import org.lwjgl.input.Keyboard;

import audio.AudioBuffer;

import sprite.Sprite;
import sprite.SpriteType;
import sprite.SpriteFactory;

import core.GameState;
import core.GameStateFactory;
import core.Main;

public class LevelEngine implements GameState {

	/* Constants */
	public static String defaultLevel = "levels/level_00.xml";
	private String levelsDir = "res/";

	/* Control variables */
	// TODO: Implement in-game menu (also acts as pause)
	private boolean pause = false;
	private Level level;

	/* Loading control */
	private int counter = 2;
	private Sprite loadingScreen = new Sprite(SpriteType.LOADING);
	private String name = defaultLevel;

	/* Pause control */
	private boolean escapePressed;
	private Sprite pausedScreen = new Sprite(SpriteType.PAUSED);

	/* Two players */
	private boolean twoPlayers = false;


	/* Constructor */
	public LevelEngine(boolean twoPlayers) {
		this.twoPlayers = twoPlayers;
	}

	/** It loads a new level by creating a class level */
	public void setFileToLoad(String name) {
		this.name = name;
		counter = 2;
	}

	private void loadSelectedFile() {

		level = LevelLoader.loadFile(levelsDir  + name, twoPlayers);
		level.setLevelEngine(this);
		if (name.equals(defaultLevel)) {
			level.resetScores();
		}
	}

	public void input() {
		if (!pause) {
			if (counter == 0) {
				level.input();
			}
		}

		/* Pause */
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if (!escapePressed) {
				level.toggleSound();
				pause = !pause;
				escapePressed = true;
			}
		} else if (escapePressed) {
			escapePressed = false;
		}

		if (pause && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			setGameState(GameStateFactory.MENU);
		}
	}

	public void update() {
		updateLoadingScreen();
	}

	private void updateLoadingScreen() {

		// Wait a determinate time before loading 
		if (counter > 1) {
			
			// Decrease counter
			counter--;
			
		} else if (counter == 1) {
			
			// Load all the resources needed
			loadResources(); 
			
			/* Load level */
			loadSelectedFile();
			
			// PROVISIONAL: Level number
			level.setLevelNumber(level.getLevelNumber()+1);
			
			/* Start stuff */
			level.toggleSound();
			
			counter--; // Remember to make the last counter decrement
			
		} else if (!pause) {
			
			// As long as counter is 0 and level is not paused
			level.update();
		}
	}

	private void loadResources() {
		/* Load sprites */
		SpriteFactory.loadRemainingTextures();

		/* Load sounds */
		AudioBuffer.loadRemainingSounds();
	}

	public void render() {
		/* Render level */
		if (counter > 0) {
			loadingScreen.render();
		} else {
			if (!pause) {
				level.render();
				level.renderStats();
			} else {
				pausedScreen.render();
			}
		}
	}

	public void setGameState(int state) {
		pause = false;
		level.stopEverything();
		Main.setGameState(state);
	}

	public void restart() {
		setFileToLoad(defaultLevel);
	}

}
