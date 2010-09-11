package usecases;

import java.awt.Color;
import java.util.HashSet;

import model.GameCore;
import model.Player;
import model.ScoreUnit;
import model.Wire;

public class JuegoYNoGano {
	public static void main(String[] args) {
		HashSet<Player> players= new HashSet<Player>();
		
		players.add(new Player("Dario", Color.RED)); 
		players.add(new Player("Rolando", Color.BLUE));
		
		GameCore core= new GameCore(2, 2, players);
		
		ScoreUnit a= core.getScoreUnit(0, 0); // Izquierda arriba
	
		Wire leftUpWire= a.getLeft();
	
		leftUpWire.play();
	}
	
}
