package usecases;

import java.awt.Color;
import java.util.HashSet;

import model.GameCore;
import model.Player;

public class AbandonoPartida {
	public static void main(String[] args) {
		HashSet<Player> players= new HashSet<Player>();
		
		Player dario= new Player("Dario", Color.RED);
		Player rolando= new Player("Rolando", Color.BLUE);
		players.add(dario); 
		players.add(rolando);
		
		GameCore core= new GameCore(2, 2, players);
		
		// Darío abandona la partida
		core.abandon(dario);
	}
}
