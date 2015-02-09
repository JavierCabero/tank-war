package core;

import org.lwjgl.util.Renderable;

import util.Updatable;

public interface GameState extends Renderable, Updatable{

	public void input();
	
	/** GameStates must stop their working resources like music before giving the turn to other state */
	public void setGameState(int state);

}
