package model;

/**
 * Contrato para hacer que una clase sea parte de un 
 * juego de punto y raya. 
 * @author Dario
 */
public abstract class GameElement {
	private GameCore myCore;

	public GameElement(GameCore core){
		this.myCore= core;
	}

	public GameCore getGameCore(){
		return myCore;
	}
}
