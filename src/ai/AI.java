package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import level.Level;
import level.Tile;
import entity.ArmedCharacter;
import entity.Player;
import entity.Projectile;
import entity.ProjectileType;


/**
 * My awesome class with Manhattan Distance method.
 * 
 * @author Kay
 * 
 */
public class AI {

	/* AI control */
	private int pathDelay = 128;
	private int nextPath = pathDelay;
	private int waitDelay = 0;
	private int maxWait = 64;
	private int maxPathUse = 5;
	private int pathUse = -1; 

	private List<Direction> currentPath = new ArrayList<Direction>();
	private Tile currentTile;
	private Random random = new Random();

	private boolean debug = false;

	/* Dependencies */
	private ArmedCharacter ac;
	private Level level;

	/* Constructor */
	public AI(AIType aiType, ArmedCharacter ac, Level level) {
		this.pathDelay = aiType.getPathDelay();
		this.maxWait = aiType.getMaxWait();
		this.maxPathUse = aiType.getMaxPathUse();
		this.level = level;
		this.ac = ac;
		currentTile = level.getTileMatrix()[ac.getX() / Tile.WIDTH][ac.getY() / Tile.HEIGHT];
	}

	/* Methods */
	public Direction aStarToPlayer() {

		Player player = (random.nextInt(2) == 0) ? level.getPlayer1() : level.getPlayer2();
		if (player != null) {
			int x = ac.getX();
			int y = ac.getY();
			int playerX = player.getCenterX() / Tile.WIDTH;
			int playerY = player.getCenterY() / Tile.HEIGHT;

			int currentX = (x + ac.getWidth() / 2) / Tile.WIDTH;
			int currentY = (y + ac.getHeight() / 2) / Tile.HEIGHT;

			Direction bestMove = Direction.NODIR;
			List<Node> openList = new ArrayList<Node>();
			List<Node> closedList = new ArrayList<Node>();

			double distanceToPlayer = AI.manhattanDistance(playerX, playerY, currentX, currentY);
			double qualityOfBestMove = Integer.MAX_VALUE;
			// int pathQuality = 0;

			// Set up the grid
			Tile[][] tiles = level.getTileMatrix();
			Node[][] nodesGrid = new Node[tiles.length][tiles[0].length];

			// Add starting node (Node that the direction is NODIR)
			Node startingNode = new Node(currentX, currentY, 0, distanceToPlayer, Direction.NODIR);
			nodesGrid[currentX][currentY] = startingNode;

			// Add starting node to the list
			openList.add(startingNode);

			boolean stopMessages = false;

			// Search the path
			while (currentX != playerX || currentY != playerY) {
				if (!stopMessages)
					log(currentX + " " + currentY);
				openList.remove(nodesGrid[currentX][currentY]);
				closedList.add(nodesGrid[currentX][currentY]);

				if (0 <= currentY - 1 && !closedList.contains(nodesGrid[currentX][currentY - 1]) && !tiles[currentX][currentY - 1].getType().isSolid()) {
					distanceToPlayer = AI.manhattanDistance(playerX, playerY, currentX, currentY - 1);
					if (distanceToPlayer < qualityOfBestMove) {
						qualityOfBestMove = distanceToPlayer;
						bestMove = Direction.UP;
					}
					if (nodesGrid[currentX][currentY - 1] == null) {
						Node node = new Node(currentX, currentY - 1, 1 + nodesGrid[currentX][currentY].getG(), distanceToPlayer, Direction.DOWN);
						nodesGrid[currentX][currentY - 1] = node;
						if (!openList.add(node)) {
							node = openList.get(openList.indexOf(node));
							if (1 + nodesGrid[currentX][currentY].getG() < node.getG()) {
								node.setDirection(Direction.DOWN);
								node.setG(1 + nodesGrid[currentX][currentY].getG());
							}
						}
					}
				}
				if (0 <= currentX - 1 && !closedList.contains(nodesGrid[currentX - 1][currentY]) && !tiles[currentX - 1][currentY].getType().isSolid()) {
					distanceToPlayer = AI.manhattanDistance(playerX, playerY, currentX - 1, currentY);
					if (distanceToPlayer < qualityOfBestMove) {
						qualityOfBestMove = distanceToPlayer;
						bestMove = Direction.LEFT;
					}
					if (nodesGrid[currentX - 1][currentY] == null) {
						Node node = new Node(currentX - 1, currentY, 1 + nodesGrid[currentX][currentY].getG(), distanceToPlayer, Direction.RIGHT);
						nodesGrid[currentX - 1][currentY] = node;
						if (!openList.add(node)) {
							node = openList.get(openList.indexOf(node));
							if (1 + nodesGrid[currentX][currentY].getG() < node.getG()) {
								node.setDirection(Direction.RIGHT);
								node.setG(1 + nodesGrid[currentX][currentY].getG());
							}
						}
					}
				}
				if (currentY + 1 < level.getHeight() && !closedList.contains(nodesGrid[currentX][currentY + 1])
						&& !tiles[currentX][currentY + 1].getType().isSolid()) {
					distanceToPlayer = AI.manhattanDistance(playerX, playerY, currentX, currentY + 1);
					if (distanceToPlayer < qualityOfBestMove) {
						qualityOfBestMove = distanceToPlayer;
						bestMove = Direction.DOWN;
					}
					if (nodesGrid[currentX][currentY + 1] == null) {
						Node node = new Node(currentX, currentY + 1, 1 + nodesGrid[currentX][currentY].getG(), distanceToPlayer, Direction.UP);
						nodesGrid[currentX][currentY + 1] = node;
						if (!openList.add(node)) {
							node = openList.get(openList.indexOf(node));
							if (1 + nodesGrid[currentX][currentY].getG() < node.getG()) {
								node.setDirection(Direction.UP);
								node.setG(1 + nodesGrid[currentX][currentY].getG());
							}
						}
					}
				}
				if (currentX + 1 < level.getWidth() && !closedList.contains(nodesGrid[currentX + 1][currentY])
						&& !tiles[currentX + 1][currentY].getType().isSolid()) {
					distanceToPlayer = AI.manhattanDistance(playerX, playerY, currentX + 1, currentY);
					if (distanceToPlayer < qualityOfBestMove) {
						qualityOfBestMove = distanceToPlayer;
						bestMove = Direction.RIGHT;
					}
					if (nodesGrid[currentX + 1][currentY] == null) {
						Node node = new Node(currentX + 1, currentY, 1 + nodesGrid[currentX][currentY].getG(), distanceToPlayer, Direction.LEFT);
						nodesGrid[currentX + 1][currentY] = node;
						if (!openList.add(node)) {
							node = openList.get(openList.indexOf(node));
							if (1 + nodesGrid[currentX][currentY].getG() < node.getG()) {
								node.setDirection(Direction.LEFT);
								node.setG(1 + nodesGrid[currentX][currentY].getG());
							}
						}
					}
				}

				// Do we have a change to move forward or do we have to go back?
				if (bestMove.equals(Direction.NODIR)) {
					// Go back
					bestMove = nodesGrid[currentX][currentY].getDirection();
					// log("Can't follow this way (no possible moves)");
				}
				if (bestMove.equals(Direction.UP)) {
					currentY--;
				} else if (bestMove.equals(Direction.LEFT)) {
					currentX--;
				} else if (bestMove.equals(Direction.DOWN)) {
					currentY++;
				} else if (bestMove.equals(Direction.RIGHT)) {
					currentX++;
				} else {
					if (!stopMessages) {
						stopMessages = true;
						log("Error getting next move");
					}
					if (openList.isEmpty()) {
						break;
					} else {
						// Needed to be initialized
						Node nextNodeToCheck = openList.get(0);
						double bestF = Integer.MAX_VALUE;
						for (Node n : openList) {
							if (n.getF() < bestF) {
								nextNodeToCheck = n;
								bestF = n.getF();
							}
						}
						currentX = nextNodeToCheck.getX();
						currentY = nextNodeToCheck.getY();
					}
				}

				bestMove = Direction.NODIR;
				qualityOfBestMove = Integer.MAX_VALUE;
			}

			// for (int i = 0; i < nodesGrid.length; i++) {
			// for (int j = 0; j < nodesGrid[i].length; j++) {
			// if (nodesGrid[j][i] != null) {
			// System.out.print(nodesGrid[j][i].getDirection() + " ");
			// } else {
			// System.out.print("NODIR ");
			// }
			// }
			// System.out.println();
			// }
			// Calculate path to the player
			int xDiv32 = x / 32;
			int yDiv32 = y / 32;
			log("Calculating path...");
			// Reuse bestMove (don't be confused about it)
			while (currentX != xDiv32 || currentY != yDiv32) {
				bestMove = nodesGrid[currentX][currentY].getDirection();

				// System.out.println(currentX + " " + currentY);
				if (bestMove.equals(Direction.UP)) {
					currentPath.add(Direction.DOWN);
					currentY--;
				} else if (bestMove.equals(Direction.LEFT)) {
					currentPath.add(Direction.RIGHT);
					currentX--;
				} else if (bestMove.equals(Direction.DOWN)) {
					currentPath.add(Direction.UP);
					currentY++;
				} else if (bestMove.equals(Direction.RIGHT)) {
					currentPath.add(Direction.LEFT);
					currentX++;
				} else {
					log("Error in direction");
				}

			}

			log("Path calculated");
		}
		if (currentPath.isEmpty()) {
			return Direction.NODIR;
		}
		return currentPath.remove(currentPath.size() - 1);
	}

	private void movementUpdate() {
		if (waitDelay == 0) {
			/* Over just one tile */
			if (overAnOnlyTile()) {
				/* If we are on a new different tile */
				if (!currentTile.equals(level.getTileMatrix()[ac.getX() / Tile.WIDTH][ac.getY() / Tile.HEIGHT])) {
					currentTile = level.getTileMatrix()[ac.getX() / Tile.WIDTH][ac.getY() / Tile.HEIGHT];
					/* If possible, get next move */
					if (!currentPath.isEmpty()) {
						ac.setDirection(currentPath.remove(currentPath.size() - 1));
						if (maxPathUse != -1) {
							pathUse--;
						}
					}
					/* If we reached the maximum moves of the path */
					if (pathUse <= 0 && maxPathUse != -1) {
						currentPath.clear();
						pathUse = maxPathUse;
					}
				} else if (currentPath.isEmpty()) {
					/* If there is no path to follow */
					if (nextPath == 0) {
						ac.setDirection(aStarToPlayer());
						nextPath = pathDelay / 2 + random.nextInt(pathDelay);
//						List<GameObject> characters = level.getCollisionList(ac);
						ac.move();
					} else {
						currentPath.add(randomMove());
						nextPath--;
					}
				} else {
					/* We need to move on to the next tile */
					if (!ac.move()) {
						currentPath.clear();
						ac.setDirection(randomMove());
					}
				}
			} else {
				/* Over several tiles at once */
				if (!ac.move()) {
					/*
					 * We couldn't move to the non-solid tile because there is
					 * some obstacle in the way
					 */
					currentPath.clear();
					ac.setDirection(randomMove());
				}
			}
			/* 1% probability of waiting the next time */
			if (random.nextInt(100) == 0) {
				waitDelay = random.nextInt(maxWait);
			}
		} else {
			waitDelay--;
		}
	}

	private Direction randomMove() {
		switch (random.nextInt(4)) {
		case 0:
			return Direction.UP;
		case 1:
			return Direction.LEFT;
		case 2:
			return Direction.DOWN;
		default:
			return Direction.RIGHT;
		}
	}

	private boolean overAnOnlyTile() {
		return ac.getX() / Tile.WIDTH == (ac.getX() + ac.getWidth() - 1) / Tile.WIDTH
				&& ac.getY() / Tile.HEIGHT == (ac.getY() + ac.getHeight() - 1) / Tile.HEIGHT;
	}

	public void update() {
		/* Shoot if its worth it */
		if (!shootCheck()) {
			/* A* or random move */
			movementUpdate();
		}
	}

	private boolean shootCheck() {

		Player player = level.getPlayer1();
		if (player != null) {
			int playerX = player.getCenterX() / (ac.getHalfWidth() + player.getHalfWidth());
			int playerY = player.getCenterY() / (ac.getHalfHeight() + player.getHalfHeight());
			int currentX = ac.getCenterX() / (ac.getHalfWidth() + player.getHalfWidth());
			int currentY = ac.getCenterY() / (ac.getHalfHeight() + player.getHalfHeight());

			int diffX = playerX - currentX;
			int diffY = playerY - currentY;

			Direction dir = ac.getDirection();
			if ((dir.equals(Direction.UP) && diffX == 0 && diffY <= 0) || (dir.equals(Direction.LEFT) && diffY == 0 && diffX <= 0)
					|| (dir.equals(Direction.DOWN) && diffX == 0 && diffY >= 0) || (dir.equals(Direction.RIGHT) && diffY == 0 && diffX >= 0)) {

				if (ac.shoot()) {
					ProjectileType pt = ac.getWeapon().getNextProjectileType();
					pt.getShootAudio().playAsSoundEffect(1f, 1f, false);
					level.addProjectile(new Projectile(ac.getWeaponX(), ac.getWeaponY(), pt, ac.getDirection(), ac, level));
					return true;
				}
			}
		}
		Player player2 = level.getPlayer2();
		if (player2 != null) {
			int playerX = player2.getCenterX() / (ac.getHalfWidth() + player2.getHalfWidth());
			int playerY = player2.getCenterY() / (ac.getHalfHeight() + player2.getHalfHeight());
			int currentX = ac.getCenterX() / (ac.getHalfWidth() + player2.getHalfWidth());
			int currentY = ac.getCenterY() / (ac.getHalfHeight() + player2.getHalfHeight());

			int diffX = playerX - currentX;
			int diffY = playerY - currentY;

			Direction dir = ac.getDirection();
			if ((dir.equals(Direction.UP) && diffX == 0 && diffY <= 0) || (dir.equals(Direction.LEFT) && diffY == 0 && diffX <= 0)
					|| (dir.equals(Direction.DOWN) && diffX == 0 && diffY >= 0) || (dir.equals(Direction.RIGHT) && diffY == 0 && diffX >= 0)) {

				if (ac.shoot()) {
					ProjectileType pt = ac.getWeapon().getNextProjectileType();
					pt.getShootAudio().playAsSoundEffect(1f, 1f, false);
					level.addProjectile(new Projectile(ac.getWeaponX(), ac.getWeaponY(), pt, ac.getDirection(), ac, level));
					return true;
				}
			}
		}
		return false;
	}

	public static double manhattanDistance(int x, int y, int x2, int y2) {
		int diffX = x - x2;
		int diffY = y - y2;
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}

	private void log(String msg) {
		if (debug) {
			System.out.println("[AI] " + msg);
		}
	}

}
