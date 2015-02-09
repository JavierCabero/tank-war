package level;

import entity.ArmedCharacterType;
import gui.Button;
import gui.EditableTextView;
import gui.HorizontalLayout;
import gui.VerticalLayout;
import gui.ViewSquareArea;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import sprite.SpriteType;
import util.Action;

import core.GameState;
import core.GameStateFactory;
import core.Main;

public class LevelEditor implements GameState {

	enum DeployType {
		TILE, PLAYER_RESPAWN, ENEMY_RESPAWN
	}

	enum PlayerRespawnType {
		BLUE, ORANGE
	}

	private static final String defaultDir = "res/levels/";
	private static final String defaultName = "default.xml";

	/* Control Variables */
	private static boolean debug = true;
	private Level level;

	/* Deploy control */
	private DeployType deployType = DeployType.TILE;
	private TileType tileDeployType = TileType.BRICK_WALL;
	private PlayerRespawnType playerRespawnDeployType = PlayerRespawnType.BLUE;
	private JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/res/custom");

	/* Variables for GameObject placement control */
	private int playerNo;

	/* Offsets */
	private int level_x_offset = 94;
	private int level_y_offset = 64;

	/* Layout */
	private VerticalLayout layout = null;
	private ViewSquareArea tileDeployList = new ViewSquareArea(8, level_y_offset, 76, 152);
	private ViewSquareArea respawnDeployList = new ViewSquareArea(8, level_y_offset + 152 + 8, 76, 76);
	private ViewSquareArea enemyDeployList = new ViewSquareArea(8, level_y_offset + 152 + 8 + 76 + 8, 76, 76);
	private ViewSquareArea menuList = new ViewSquareArea(8, 8, Main.EDITOR_WIDTH - 16, 48);
	private ViewSquareArea levelSettingList = new ViewSquareArea(level_x_offset + 640 + 8, 64, 144, 400);

	/* ------------- CONSTRUCTOR ------------- */
	public LevelEditor() {

		// Load screen
		setUpLayout();

		// Load default level
		loadLevel(defaultDir + defaultName);

	}

	/* ------------- CONSTRUCTOR AUXILIAR METHODS ------------- */

	private void loadLevel(String absDir) {
		level = LevelLoader.loadFile(absDir, true);
		level.setX(level_x_offset);
		level.setY(level_y_offset);
		level.setOnFocusAction(new Action() {

			@Override
			public void execute() {
				if (Mouse.isButtonDown(0)) {
					deploySelection();
				} else if (Mouse.isButtonDown(1)) {
					deleteSelection();
				}
			}
		});

		// Update TextViews when level loads
		refreshSettingList();

		((HorizontalLayout) ((HorizontalLayout) layout.getBottomChild()).getLeftChild()).setRightView(level);
	}

	private void setUpLayout() {

		// TODO: Automatic layouts

		// Load buttons
		setUpDeployLists();

		// Load menu buttons
		setUpMenuList();

		// Load settings list
		setUpLevelSettingsList();

		/* Deploy lists */
		VerticalLayout a1 = new VerticalLayout();
		a1.setTopView(respawnDeployList);
		a1.setBottomView(enemyDeployList);
		a1.setMidPoint(Main.EDITOR_HEIGHT - (level_y_offset + 152 + 8 + 76 + 8));

		VerticalLayout a2 = new VerticalLayout();
		a2.setTopView(tileDeployList);
		a2.setBottomView(a1);
		a2.setMidPoint(Main.EDITOR_HEIGHT - (level_y_offset + 152 + 8));

		HorizontalLayout l1 = new HorizontalLayout();
		l1.setLeftView(a2);
		l1.setRightView(level);
		l1.setMidPoint(level_x_offset);

		HorizontalLayout l2 = new HorizontalLayout();

		l2.setLeftView(l1);
		l2.setRightView(levelSettingList);
		l2.setMidPoint(level_x_offset + 640);

		layout = new VerticalLayout();
		layout.setTopView(menuList);
		layout.setBottomView(l2);
		layout.setMidPoint(Main.EDITOR_HEIGHT - level_y_offset);

	}

	private void setUpMenuList() {

		/* Config menuList */
		menuList.setXDistance(108);
		menuList.setVisibleArea(true);

		/* Create buttons */
		Button b = new Button(0, 0, SpriteType.NEW_BUTTON, new Action() {

			public void execute() {
				int option = fc.showSaveDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getAbsolutePath();
					loadLevel(defaultDir + defaultName);
					level.setPath(path);
				}
			}
		});
		b.setLogMessage("New level button activated");
		menuList.addView(b);

		b = new Button(0, 0, SpriteType.OPEN_BUTTON, new Action() {

			public void execute() {
				int option = fc.showOpenDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getAbsolutePath();
					loadLevel(path);
				}
			}
		});
		b.setLogMessage("Open level button activated");
		menuList.addView(b);

		b = new Button(0, 0, SpriteType.SAVE_BUTTON, new Action() {

			public void execute() {
				int option = fc.showSaveDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getAbsolutePath();
					LevelLoader.saveFile(level, path);
				}
			}
		});
		b.setLogMessage("Save level button activated");
		menuList.addView(b);

		b = new Button(0, 0, SpriteType.CLEAR_BUTTON, new Action() {

			public void execute() {
				level.clear();
			}
		});
		b.setLogMessage("Clear level button activated");
		menuList.addView(b);

		b = new Button(0, 0, SpriteType.RELOAD_BUTTON, new Action() {

			public void execute() {
				if (level != null) {
					loadLevel(level.getPath());
				}
			}
		});
		b.setLogMessage("Reload level button activated");
		menuList.addView(b);

		b = new Button(0, 0, SpriteType.BACK_WHITE_BUTTON, new Action() {

			public void execute() {
				setGameState(GameStateFactory.MENU);
			}
		});
		b.setLogMessage("Back to menu button activated");
		menuList.addView(b);

	}

	private void setUpLevelSettingsList() {
		levelSettingList.setYDistance(48);
		levelSettingList.setVisibleArea(true);

		// Create changelevel label
		{
			final EditableTextView b = new EditableTextView(0, 0, SpriteType.BLANK_BUTTON);
			b.setOnClickAction(new Action() {

				public void execute() {

					// Show win action options
					String[] options = new String[2];
					options[0] = "changelevel";
					options[1] = "credits";
					int selection = JOptionPane.showOptionDialog(null, "Please, select win action:", "Win action ", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, options, null);

					if (selection == JOptionPane.CLOSED_OPTION) {
						return;
					}

					// If the selection was 'changelevel' ask for its name
					if (selection == 0) {

						String nextLevel = JOptionPane.showInputDialog("Please, type the name of the level to change");

						if (nextLevel != null) {
							level.setWinInfo(options[selection], nextLevel);
							b.setCurrentText("Next lvl: " + nextLevel);

						}
					} else {
						// If we selected credits
						level.setWinAction(options[selection]);
						b.setCurrentText(options[selection]);
					}
				}
			});

			b.setCurrentText("No win action");
			b.setLogMessage("Edit level winAction activated");
			levelSettingList.addView(b);
		}

		{
			final EditableTextView b = new EditableTextView(0, 0, SpriteType.BLANK_BUTTON);
			b.setOnClickAction(new Action() {

				public void execute() {

					String musicName = JOptionPane.showInputDialog("Please, type the name of the level to change");

					if (musicName != null) {
						level.setMusicName(musicName);
						b.setCurrentText("Music: " + musicName);
					}
				}
			});
			b.setCurrentText("No music");
			b.setLogMessage("Edit level music activated");
			levelSettingList.addView(b);
		}
	}

	private void setUpDeployLists() {

		setUpTileDeployList();
		setUpRespawnDeployList();
		setUpEnemyDeployList();

		// TODO: Fullscreen
		// TODO: Add info labels
	}

	private void setUpTileDeployList() {

		tileDeployList.setVisibleArea(true);

		/* Brick Wall button */
		Button b = new Button(0, 0, SpriteType.BRICK_WALL, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.BRICK_WALL;
			}
		});
		b.setLogMessage("Brick wall button activated");
		tileDeployList.addView(b);

		/* Stone Wall button */
		b = new Button(0, 0, SpriteType.STONE_WALL, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.STONE_WALL;
			}
		});
		b.setLogMessage("Stone wall button activated");
		tileDeployList.addView(b);

		/* Dirt Ground button */
		b = new Button(0, 0, SpriteType.DIRT_GROUND, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.DIRT_GROUND;
			}
		});
		b.setLogMessage("Dirt ground button activated");
		tileDeployList.addView(b);

		/* Grass Ground button */
		b = new Button(0, 0, SpriteType.GRASS_GROUND, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.GRASS_GROUND;
			}
		});
		b.setLogMessage("Grass ground button activated");
		tileDeployList.addView(b);

		/* Earth Ground button */
		b = new Button(0, 0, SpriteType.EARTH_GROUND, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.EARTH_GROUND;
			}
		});
		b.setLogMessage("Earth ground button activated");
		tileDeployList.addView(b);

		/* Sand Ground button */
		b = new Button(0, 0, SpriteType.SAND_GROUND, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.SAND_GROUND;
			}
		});
		b.setLogMessage("Sand ground button activated");
		tileDeployList.addView(b);

		/* Water button */
		b = new Button(0, 0, SpriteType.WATER, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.WATER;
			}
		});
		b.setLogMessage("Water button activated");
		tileDeployList.addView(b);

		/* Level Limit button */
		b = new Button(0, 0, SpriteType.LEVEL_LIMIT, new Action() {

			public void execute() {
				deployType = DeployType.TILE;
				tileDeployType = TileType.LEVEL_LIMIT;
			}
		});
		b.setLogMessage("Water button activated");
		tileDeployList.addView(b);

	}

	private void setUpRespawnDeployList() {

		respawnDeployList.setVisibleArea(true);

		/* Player Respawn Blue button */
		Button b = new Button(0, 0, SpriteType.PLAYER_RESPAWN, new Action() {

			public void execute() {
				deployType = DeployType.PLAYER_RESPAWN;
				playerRespawnDeployType = PlayerRespawnType.BLUE;
			}
		});
		b.setLogMessage("Player Respawn Blue button activated");
		respawnDeployList.addView(b);

		/* Player Respawn Orange button */
		b = new Button(0, 0, SpriteType.PLAYER_RESPAWN_ORANGE, new Action() {

			public void execute() {
				deployType = DeployType.PLAYER_RESPAWN;
				playerRespawnDeployType = PlayerRespawnType.ORANGE;
			}
		});
		b.setLogMessage("Player Respawn Orange button activated");
		respawnDeployList.addView(b);

		/* Enemy Respawn button */
		b = new Button(0, 0, SpriteType.ENEMY_RESPAWN, new Action() {

			public void execute() {
				deployType = DeployType.ENEMY_RESPAWN;
			}
		});
		b.setLogMessage("Enemy Respawn button activated");
		respawnDeployList.addView(b);
	}

	private void setUpEnemyDeployList() {

		enemyDeployList.setVisibleArea(true);

		/* Delete last enemy button */
		Button b = new Button(0, 0, SpriteType.ENEMY_BASIC_DELETE, new Action() {

			public void execute() {
				level.removeLastEnemyOfList();
			}
		});
		b.setLogMessage("Delete last enemy button activated");
		enemyDeployList.addView(b);

		/* Add basic enemy button */
		b = new Button(0, 0, SpriteType.ENEMY_BASIC, new Action() {

			public void execute() {
				level.addEnemyToList(ArmedCharacterType.ENEMY_BASIC);
			}
		});
		b.setLogMessage("Add basic enemy button activated");
		enemyDeployList.addView(b);

		/* Add strong enemy button */
		b = new Button(0, 0, SpriteType.ENEMY_STRONG, new Action() {

			public void execute() {
				level.addEnemyToList(ArmedCharacterType.ENEMY_STRONG);
			}
		});
		b.setLogMessage("Add strong enemy button activated");
		enemyDeployList.addView(b);

		/* Add red enemy button */
		b = new Button(0, 0, SpriteType.ENEMY_RED, new Action() {

			public void execute() {
				level.addEnemyToList(ArmedCharacterType.ENEMY_RED);
			}
		});
		b.setLogMessage("Add red enemy button activated");
		enemyDeployList.addView(b);
	}

	/* ------------- INPUT AUXILIAR METHODS ------------- */
	private void deploySelection() {
		int normalizedX = (Mouse.getX() - level_x_offset) / Tile.WIDTH;
		int normalizedY = Display.getHeight() / 32 - (Mouse.getY() + level_y_offset) / Tile.HEIGHT - 1;
		if (deployType.equals(DeployType.TILE)) {
			if (!level.getTileAt(normalizedX, normalizedY).getType().equals(tileDeployType)) {
				level.setTileAt(normalizedX, normalizedY, tileDeployType);
			}
		} else if (deployType.equals(DeployType.PLAYER_RESPAWN)) {
			// If tile is not solid place the player
			if (!level.getTileAt(normalizedX, normalizedY).getType().isSolid()) {
				normalizedX *= Tile.WIDTH;
				normalizedY *= Tile.HEIGHT;
				// TODO: Create better respawns
				if (playerRespawnDeployType.equals(PlayerRespawnType.BLUE)) {
					level.setPlayerBlueRespawnAt(normalizedX, normalizedY);
				} else {
					level.setPlayerOrangeRespawnAt(normalizedX, normalizedY);
				}
			}
		} else if (deployType.equals(DeployType.ENEMY_RESPAWN)) {
			// If tile is not solid place the enemy respawn
			if (!level.getTileAt(normalizedX, normalizedY).getType().isSolid()) {
				normalizedX *= Tile.WIDTH;
				normalizedY *= Tile.HEIGHT;
				level.addEnemyRespawnAt(normalizedX, normalizedY);
			}
		}
	}

	private void deleteSelection() {
		int mouseX;
		int mouseY;
		if (deployType.equals(DeployType.TILE)) {
			mouseX = (Mouse.getX() - level_x_offset) / Tile.WIDTH;
			mouseY = Display.getHeight() / 32 - (Mouse.getY() + level_y_offset) / Tile.HEIGHT - 1;
			if (!level.getTileAt(mouseX, mouseY).getType().equals(TileType.NULL)) {
				level.setTileAt(mouseX, mouseY, TileType.NULL);
			}
		} else if (deployType.equals(DeployType.PLAYER_RESPAWN)) {
			// TODO: Think about this: maybe it should be in Level class
			level.removePlayerRespawn(playerNo);
		} else if (deployType.equals(DeployType.ENEMY_RESPAWN)) {
			// Send message to delete enemy respawn
			mouseX = (Mouse.getX() - level_x_offset);
			mouseY = (Mouse.getY() + level_y_offset);
			if (level.removeEnemyRespawnAt(mouseX, Display.getHeight() - mouseY)) {
				log("Enemy removed on (" + mouseX + "," + mouseY + ")");
			}

		}
	}

	/* ------------- GAME STATE PRINCIPAL METHODS ------------- */
	public void input() {

		// TODO: Create button shortcuts

		// /* Load */
		// if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
		//
		// }
		//
		// /* Save */
		// if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		// LevelLoader.saveFile(level, level.getPath());
		// }
		//
		// /* Clear */
		// if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
		// level.clear();
		// }

	}

	private void refreshSettingList() {
		String winAction = level.getWinAction();
		if (winAction != null) {
			if (winAction.equals("credits")) {
				((EditableTextView) levelSettingList.get(0)).setCurrentText(winAction);
			} else {
				((EditableTextView) levelSettingList.get(0)).setCurrentText(level.getNextLevel() != null ? "Next lvl: " + level.getNextLevel()
						: "No win action");
			}
		}
		((EditableTextView) levelSettingList.get(1)).setCurrentText(level.getMusicName() != null ? "Music: " + level.getMusicName() : "No music");
	}

	public void update() {
		// Así me gusta, vacío, como debe estar
	}

	public void render() {

		/* Render everything */
		layout.render();

		level.renderRespawns();
		level.renderEnemyListAt(Main.EDITOR_WIDTH - 256, Main.EDITOR_HEIGHT - 256);

		layout.onFocus(Mouse.getX(), Mouse.getY());

	}

	/* ------------- OTHER METHODS ------------- */
	public void setGameState(int state) {
		Main.setGameState(state);
		Main.setUpScreen(Main.MAIN_WIDTH, Main.MAIN_HEIGHT, Main.MAIN_TITLE);
	}

	public static void log(String msg) {
		if (debug)
			System.out.println("[Level Editor] " + msg);
	}
}
