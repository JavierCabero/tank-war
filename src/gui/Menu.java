package gui;


import javax.swing.JFileChooser;

import level.LevelEngine;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import sprite.Sprite;
import sprite.SpriteType;
import util.Action;
import core.GameState;
import core.GameStateFactory;
import core.Main;

public class Menu implements GameState {

	/* Background */
	private Sprite sprite;
	private boolean twoPlayers = false;
	

	private ViewSquareArea currentButtons = null;
	
	private ViewSquareArea mainButtons = null;
	private ViewSquareArea historyButtons = null;
	private boolean debug = true;

	public Menu() {
		
		/* Load background */
		this.sprite = new Sprite(SpriteType.MENU_BACKGROUND);
		
		//TODO: Make rankings 
		//TODO: Make continue button
		// The last argument is never used and should be removed
		setupMainButtons();
		setupHistoryButtons();
	}

	private void setupHistoryButtons() {
		historyButtons = new ViewSquareArea(Display.getWidth() / 2 - 84, 218, 164, 0); 	
		historyButtons.setYDistance(74);
		
		historyButtons.addView(new Button(0, 0, SpriteType.ONE_PLAYER_BUTTON, new Action() {
			
			public void execute() {
				log("One player pressed");
				twoPlayers = false;
				setGameState(GameStateFactory.START_GAME);
			}
		}));
		historyButtons.addView(new Button(0, 0, SpriteType.TWO_PLAYERS_BUTTON, new Action() {
			
			public void execute() {
				log("Two players pressed");
				twoPlayers = true;
				setGameState(GameStateFactory.START_GAME);
			}
		}));
		
		historyButtons.addView(new Button(0, 0, SpriteType.TWO_PLAYERS_BUTTON, new Action() {
			
			public void execute() {
				log("Custom game pressed");
				//twoPlayers = true;
				
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/res/custom"); 
				int option = fc.showOpenDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getName();
					System.out.println(path);
					if(path != null) {
						LevelEngine.defaultLevel = "custom/" + path;
					}
				}
				setGameState(GameStateFactory.START_GAME);
				
				
				// TODO: Improved code usability
				/*
				loadGame() {
					
					// Setup loading screen
					
					// Asynchronous selected level load
					   
						// On load: Setup level viewer
					 
						// Have fun
				}
				*/
			}
		}));
		
		historyButtons.addView(new Button(0, 0, SpriteType.BACK_BUTTON, new Action() {
			
			public void execute() {
				log("Back pressed");
				currentButtons = mainButtons;
			}
		}));
	}

	private void setupMainButtons() {
		mainButtons = new ViewSquareArea(Display.getWidth() / 2 - 84, 218, 164, 0); 	
		mainButtons.setYDistance(74);
		
		mainButtons.addView(new Button(0, 0, SpriteType.START_GAME, new Action() {
			

			public void execute() {
				currentButtons = historyButtons;
			}
		}));
		mainButtons.addView(new Button(0, 0, SpriteType.CONTROLS_BUTTON, new Action() {
			
			public void execute() {
				setGameState(GameStateFactory.CONTROLS);
			}
		}));
		mainButtons.addView(new Button(0, 0, SpriteType.EDITOR_BUTTON, new Action() {
			
			public void execute() {
				setGameState(GameStateFactory.LEVEL_EDITOR);
			}
		}));
	//	buttons.add(new Button(GameStateFactory.SCREEN_WIDTH / 2 - rankings.getHalfWidth(), 218 + 74 + 74 + 74, SpriteType.RANKINGS_BUTTON));
		mainButtons.addView(new Button(0, 0, SpriteType.EXIT_BUTTON, new Action() {
			
			public void execute() {
				setGameState(GameStateFactory.EXIT);
			}
		}));	
		
		/* Set this buttons as the default buttons */
		currentButtons = mainButtons;
	}

	public void input() {
		// Nuthing
	}

	public void update() {
		currentButtons.onFocus(Mouse.getX(), Mouse.getY());
	}

	private void log(String msg) {
		if (debug) {
			System.out.println("[Menu] " + msg);
		}
	}

	public void render() {
		sprite.render();
		currentButtons.render();
	}

	public void setGameState(int state) {
		Main.setGameState(state);
	}

	public boolean isTwoPlayers() {
		return twoPlayers;
	}

}
