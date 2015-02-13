package core;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static util.Debugger.log;

public class Main {

	/* Display Constants */
	public static final int MAIN_WIDTH = 640 + 160;
	public static final int MAIN_HEIGHT = 640;
	public static final int EDITOR_WIDTH = 94 + 640 + 160;
	public static final int EDITOR_HEIGHT = 640 + 64;

	public static final String MAIN_TITLE = "Tank War";
	public static final String EDITOR_TITLE = "Level editor";

	/* Control Variables */
	private static GameState state;
	private static boolean isCloseRequested = false;
	private static boolean debug = true;

	/* Videogame */
	public void start() {

		// Set up screen
		setUpScreen(MAIN_WIDTH, MAIN_HEIGHT, MAIN_TITLE);
		log("Everything ready to render!");

		// Create menu
		state = GameStateFactory.setGameState(GameStateFactory.MENU);

		while (!Display.isCloseRequested()) {

			// Internal close request
			if (isCloseRequested) {
				break;
			}
			
			// Clear screen
			glClear(GL_COLOR_BUFFER_BIT);

			// We check for this
			state.input();
			state.update();
			state.render();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
		AL.destroy();
	}

	public static void setUpScreen(int width, int height, String name) {
		setUpDisplay(width, height, name);
		setUpOpenGL(width, height);
	}

	private static void setUpDisplay(int width, int height, String name) {
		try {
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(false);
			Display.setTitle(name);
			if (!Display.isCreated()) {
				Display.create();
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private static void setUpOpenGL(int width, int height) {
		// Initialization code OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// (0,0) on bottom left
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		// Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glScissor(0, 0, width, height);
		glViewport(0, 0, width, height);
	}



	public static void main(String[] args) {
		Main main = new Main();
		log("Starting game loop");
		main.start(); // Start game loop
	}

	public static void setGameState(int id){
		GameState gs = GameStateFactory.setGameState(id);
		if(gs != null) {
			state = gs;
		}
	}
	
	public static void setCloseRequested(boolean b) {
		isCloseRequested = b;
	}

	// TODO: Tutorial
	// TODO: Base war

}
