package level;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import sprite.SpriteType;
import audio.AudioBuffer;
import audio.AudioType;
import core.GameObject;
import entity.ArmedCharacterType;
import entity.Character;
import entity.Enemy;
import entity.EnemyRespawn;
import entity.PlayerRespawn;

public class LevelLoader {

	private static boolean debug = true;

	public static Level loadFile(String path, boolean twoPlayers) {

		/* Create level */
		Level level = new Level();

		if (path.equals(LevelEngine.defaultLevel)) {
			level.setLevelNumber(1);
		} else {
			level.setLevelNumber(level.getLevelNumber());
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			log("Loading level from" + path);

			level.setPath(path);
			
			Document document = builder.build(path);
			if (document != null) {
				/* Root element is "level" */
				Element root = document.getRootElement();

				/* First child is the tiles data */
				Element tilesData = root.getChild("tiles");
				if (tilesData != null) {

					int width = Integer.parseInt(tilesData.getAttributeValue("width"));
					int height = Integer.parseInt(tilesData.getAttributeValue("height"));

					Tile[][] tileMatrix = new Tile[width][height];

					for (Element t : tilesData.getChildren()) {
						int x = Integer.parseInt(t.getAttributeValue("x"));
						int y = Integer.parseInt(t.getAttributeValue("y"));
						TileType type = TileType.valueOf(t.getAttributeValue("type").toUpperCase());
						tileMatrix[x][y] = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT, type);
					}

					/* Put it in level */
					level.setWidth(width);
					level.setHeight(height);
					level.setTileMatrix(tileMatrix);
				}

				/* Second element is the set of enemies */
				List<Character> characters = new ArrayList<Character>();

				Element enemiesData = root.getChild("enemies");
				if (enemiesData != null) {
					for (Element e : enemiesData.getChildren()) {
						int x = Integer.parseInt(e.getAttributeValue("x"));
						int y = Integer.parseInt(e.getAttributeValue("y"));
						ArmedCharacterType enemyType = ArmedCharacterType.valueOf(e.getAttributeValue("ArmedCharacterType"));
						characters.add(new Enemy(x, y, enemyType, level));
					}
				}
				level.setCharacters(characters);

				Element enemyRespawnsData = root.getChild("enemyRespawns");
				if (enemyRespawnsData != null) {
					EnemyRespawn enemyRespawn = new EnemyRespawn(level);

					/* Load all respawn locations */
					Element respawnsList = enemyRespawnsData.getChild("respawnsList");
					if (respawnsList != null) {
						for (Element e : respawnsList.getChildren()) {
							int x = Integer.parseInt(e.getAttributeValue("x"));
							int y = Integer.parseInt(e.getAttributeValue("y"));
							enemyRespawn.addRespawn(x, y);
						}
					} else {
						log("Missing enemy respawns locations");
					}

					/* Load enemy list */
					Element enemyList = enemyRespawnsData.getChild("enemyList");
					if (enemyList != null) {
						for (Element e : enemyList.getChildren()) {
							enemyRespawn.addEnemy(ArmedCharacterType.valueOf(e.getAttributeValue("ArmedCharacterType")));
						}
					} else {
						log("Missing enemyList");
					}
					level.setEnemyRespawn(enemyRespawn);
				} else {
					log("Missing enemyRespawns");
				}

				/* Third element is the player data */
				Element player1Data = root.getChild("player1Respawn");
				if (player1Data != null) {
					int x = Integer.parseInt(player1Data.getAttributeValue("x"));
					int y = Integer.parseInt(player1Data.getAttributeValue("y"));
					// ArmedCharacterType armedCharacterType =
					// ArmedCharacterType.valueOf(playerData.getAttributeValue("ArmedCharacterType"));
					// player = new Player(x, y, armedCharacterType, this);
					PlayerRespawn player1Respawn = new PlayerRespawn(x, y, SpriteType.PLAYER_RESPAWN, level, 0);
					level.setPlayer1Respawn(player1Respawn);
				} else {
					log("Missing player1RespawnData");
				}

				Element player2Data = root.getChild("player2Respawn");
				if (player2Data != null) {
					int x = Integer.parseInt(player2Data.getAttributeValue("x"));
					int y = Integer.parseInt(player2Data.getAttributeValue("y"));
					PlayerRespawn player2Respawn = new PlayerRespawn(x, y, SpriteType.PLAYER_RESPAWN_ORANGE, level, 1);
					if(!twoPlayers){
						player2Respawn.setLifes(0);
					}
					level.setPlayer2Respawn(player2Respawn);
				} else {
					log("Missing player2RespawnData");
				}

				/* Audio */
				Element bgMusic = root.getChild("bgMusic");
				if (bgMusic != null) {
					String bgMusicName = bgMusic.getAttributeValue("name");
					level.setMusicName(bgMusicName);
					level.setMusic(AudioBuffer.getAudio(AudioType.valueOf(bgMusicName.toUpperCase())));
				} else {
					log("No sound loaded");
				}

				/* Get win info */
				Element winInfo = root.getChild("winInfo");
				String winAction = winInfo.getAttributeValue("winAction");
				String nextLevel = winInfo.getAttributeValue("nextLevel");
				level.setWinInfo(winAction, nextLevel);

				/* Good job */
				log("Level loaded");
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			log("Level file not found, using default configuration");

		}

		/* Return the loaded level */
		return level;

	}

	public static void saveFile(Level level, String path) {
		Document document = new Document();

		/* Save root */
		Element levelRoot = new Element("level");
		document.setRootElement(levelRoot);

		/* Save tiles */
		Element tilesData = new Element("tiles");
		levelRoot.addContent(tilesData);

		int width = level.getWidth();
		int height = level.getHeight();

		tilesData.setAttribute("width", String.valueOf(width));
		tilesData.setAttribute("height", String.valueOf(height));

		Tile[][] tileMatrix = level.getTileMatrix();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Element tile = new Element("tile");
				tile.setAttribute("x", String.valueOf(x));
				tile.setAttribute("y", String.valueOf(y));
				tile.setAttribute("type", String.valueOf(tileMatrix[x][y].getType().getName()));
				tilesData.addContent(tile);
			}
		}

		/* Save enemies */
		Element enemyRespawnsData = new Element("enemyRespawns");
		levelRoot.addContent(enemyRespawnsData);

		/* Save respawn location data */
		Element respawnsList = new Element("respawnsList");
		enemyRespawnsData.addContent(respawnsList);

		EnemyRespawn enemyRespawn = level.getEnemyRespawn();
		for (GameObject p : enemyRespawn.getRespawnsList()) {
			Element respawn = new Element("respawn");
			respawnsList.addContent(respawn);
			respawn.setAttribute("x", String.valueOf(p.getX()));
			respawn.setAttribute("y", String.valueOf(p.getY()));
		}

		/* Save enemy list data */
		Element enemyList = new Element("enemyList");
		enemyRespawnsData.addContent(enemyList);
		for (ArmedCharacterType et : enemyRespawn.getEnemyList()) {
			Element enemyType = new Element("enemyType");
			enemyList.addContent(enemyType);
			enemyType.setAttribute("ArmedCharacterType", String.valueOf(et.toString()));
		}

		/* Save player 1 */
		PlayerRespawn player1Respawn = level.getPlayer1Respawn();
		if (player1Respawn != null) {
			Element playerData = new Element("player1Respawn");
			levelRoot.addContent(playerData);

			playerData.setAttribute("x", String.valueOf(player1Respawn.getX()));
			playerData.setAttribute("y", String.valueOf(player1Respawn.getY()));
			// playerData.setAttribute("ArmedCharacterType", "PLAYER_BASIC");
		}
		/* Save player 2 */
		PlayerRespawn player2Respawn = level.getPlayer2Respawn();
		if (player2Respawn != null) {
			Element playerData = new Element("player2Respawn");
			levelRoot.addContent(playerData);

			playerData.setAttribute("x", String.valueOf(player2Respawn.getX()));
			playerData.setAttribute("y", String.valueOf(player2Respawn.getY()));
			// playerData.setAttribute("ArmedCharacterType", "PLAYER_BASIC");
		}

		/* Save sound info */
		String musicName = level.getMusicName();
		if (musicName != null) {
			Element bgMusic = new Element("bgMusic");
			levelRoot.addContent(bgMusic);
			bgMusic.setAttribute("name", musicName);
		}

		/* Save win info */
		Element winInfo = new Element("winInfo");
		levelRoot.addContent(winInfo);

		String winAction = level.getWinAction();
		String nextLevel = level.getNextLevel();
		if(winAction != null) {
			winInfo.setAttribute("winAction", winAction);
		}
		if(nextLevel != null) {
			winInfo.setAttribute("nextLevel", nextLevel);
		}
		try {
			XMLOutputter output = new XMLOutputter();
			output.output(document, new FileOutputStream(path));
			log("Level saved in: " + path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void log(String msg) {
		if (debug)
			System.out.println("[LevelLoader] " + msg);
	}
}
