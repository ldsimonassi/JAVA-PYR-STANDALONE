package model;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Módulo que encapsula toda la sesión (estados) de un juego y verifica 
 * el cumplimiento de sus reglas.
 */
public class GameCore {
	/**
	 * Estructuras de datos:
	 */
	private SortedSet<Player> activePlayers;
	private SortedSet<Player> players;
	private Wire[] wires;
	private ScoreUnit[][] scoreUnits;
	private int scoreUnitsX, scoreUnitsY;
	private int gameCounter= 0;
	

	/**
	 * Crear un engine de "Punto Raya".
	 * @param scoreUnitsX cantidad de puntajes en X (En el punto raya tradicional son 2)
	 * @param scoreUnitsY cantidad de puntajes en X (En el punto raya tradicional son 2)
	 */
	public GameCore(int scoreUnitsX, int scoreUnitsY, Set<Player> playersSet){
		this.activePlayers= new TreeSet<Player>(playersSet); 
		this.players= new TreeSet<Player>(playersSet);
			//playersSet.toArray(new Player[playersSet.size()]);
		this.scoreUnitsX= scoreUnitsX;
		this.scoreUnitsY= scoreUnitsY;

		// Crear las estructuras de datos.
		wires= new Wire[calculateMaxWires(scoreUnitsX, scoreUnitsY)];
		scoreUnits= new ScoreUnit[scoreUnitsX][scoreUnitsY];

		// Creo todos los cables!
		for (int n = 0; n < wires.length; n++) {
			wires[n]= new Wire(n, this);
		}
		
		// Ahora asocio los cables a los scoreUnits.
		int absoluteWireNumber= 0;
		for (int i = 0; i < scoreUnitsX; i++) {
			for (int j = 0; j < scoreUnitsY; j++) {
				scoreUnits[i][j]= new ScoreUnit(this);
				//Si estoy al tope de la izquierda uso un cable nuevo, sino uso el de la derecha del punto de la izquierda
				scoreUnits[i][j].setLeft(i==0? wires[absoluteWireNumber++]: scoreUnits[i-1][j].getRight());
				//Lo mismo para arriba
				scoreUnits[i][j].setUp(j==0? wires[absoluteWireNumber++]: scoreUnits[i][j-1].getDown());
				//Uso un cable nuevo para la derecha.
				scoreUnits[i][j].setRight(wires[absoluteWireNumber++]);
				//Uso un cable nuevo para abajo.
				scoreUnits[i][j].setDown(wires[absoluteWireNumber++]);
			}
		}
		// Listo, la estructura está inicializada.	
	}

	/**
	 * Retornar la socreUnit en la posición x, y (empieza de 0,0
	 * Para un juego normal de Puntos y Rayas se tienen 4 score units [(0,0);(1,0);(0,1);(1,1)]
	 * @param x
	 * @param y
	 * @return
	 */
	public ScoreUnit getScoreUnit(int x, int y){
		if(x>=scoreUnitsX || y>=scoreUnitsY) 
			return null;
		return scoreUnits[x][y];
	}

	
	/******************************
	 * Players related operations *
	 ******************************/

	/**
	 * Obtener el jugador que tiene el turno actualmente
	 * @return el Player que tiene que jugar
	 */
	public Player getCurrentPlayer() {
		return (Player)activePlayers.toArray()[gameCounter%activePlayers.size()];
	}

	/**
	 * Pasar a la próxima jugada.
	 */
	public void nextPlayer() {
		gameCounter++;
	}

	/**
	 * Informa si el juego ha terminado.
	 * El juego termina cuando todos los ScoreUnits tienen ganador.
	 * @return true si el juego finalizó, false si el juego sigue en pie
	 */
	public boolean isGameFinished(){
		//if(activePlayers.size()<=1)
		//	return true;
		for (int i = 0; i < scoreUnits.length; i++) {
			for (int j = 0; j < scoreUnits[i].length; j++) {
				Player winner= scoreUnits[i][j].getWinner();
				if(winner==null)
					return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Indicar al core que el jugador p abandone la jugada
	 * @param p jugador
	 * @return false en caso de que sea el ultimo jugador.
	 */
	public boolean abandon(Player p){
		//No puede abandonar el último jugador.
		if(activePlayers.size()<=1){
			return false;
		}
		
		activePlayers.remove(p);

		//Si es el ultimo jugador: Juego todos los cables
		if(activePlayers.size()<=1){
			System.out.println("Ultimo jugador, se le entregan todos los puntos!");
			for (int i = 0; i < wires.length; i++) {
				if(wires[i].player==null)
					System.out.println("Playing["+i+"]:"+wires[i].play());
			}
		}
		fireGameStatusChanged();
		return true;
	}
	
	public boolean isPlayerActive(Player p){
		return activePlayers.contains(p);
	}
	
	/**
	 * Retorna la lista completa de jugadores
	 * @return
	 */
	public Player[] getPlayers() {
		return (Player[])players.toArray(new Player[activePlayers.size()]);
	}
	
	/**
	 * Obtener los puntos del jugador p en lo que va del juego.
	 * @param p jugador del que se desean obtener los puntos
	 * @return puntos obtenidos por el jugador de referencia
	 */
	public int getPlayerPoints(Player p){
		int points= 0;
		for (int i = 0; i < scoreUnits.length; i++) {
			for (int j = 0; j < scoreUnits[i].length; j++) {
				Player winner= scoreUnits[i][j].getWinner();
				if(p.equals(winner)) points++;
			}
		}
		return points;
	}

	
	/***********************************************
	 * Manejo de eventos del juego:
	 ***********************************************/
	
	/**
	 * Lista de interesados en el estado del juego
	 */
	LinkedList<GameListener> listeners= new LinkedList<GameListener>();
	
	/**
	 * Agrega un interesado a la lista.
	 * @param listener
	 */
	public void addGameListener(GameListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * Dispara un evento avisando a todos los interesados que el juego cambio
	 */
	public void fireGameStatusChanged(){
		for (GameListener listener : listeners) {
			listener.gameStatusChanged(this);
		}
	}

	public int getScoreUnitsX() {
		return scoreUnitsX;
	}

	public int getScoreUnitsY() {
		return scoreUnitsY;
	}


	/******************************
	 * Métodos de ayuda (Helpers) *
	 ******************************/
	/**
	 * Calcula el número de cables que hay en un sistema de X*Y ScoreUnits
	 */
	private static int calculateMaxWires(int scoreUnitsX, int scoreUnitsY){
		return (scoreUnitsX*(scoreUnitsY+1))+(scoreUnitsY*(scoreUnitsX+1));
	}
	
	
	/***
	 * Main de ejemplo del uso del módulo de GameCore para punto raya.
	 * @param args  No se usan
	 */
	public static void main(String[] args) {
		try {
			//Ejemplo de uso del módulo del juego Stand Alone, sin interfaz gráfica de usuario.
			
			//Creo un set de jugadores en el orden que deseo que jueguen
			HashSet<Player> players= new HashSet<Player>();
			
			players.add(new Player("Dario", Color.RED)); 
			players.add(new Player("Rolando", Color.BLUE));
			
			//Creo una instancia del módulo de Punto Raya, con los jugadores creados anteriormente
			//Es un juego de 4 cuadrados, dos horizontales y dos verticales (el tradicional)
			GameCore core= new GameCore(2, 2, players);
			
			/*
			 * |a|b|
			 * |c|d|
			 */

			//Obtengo el código
			ScoreUnit a= core.getScoreUnit(0, 0); // Izquierda arriba
			ScoreUnit b= core.getScoreUnit(1, 0); // Derecha arriba
			ScoreUnit c= core.getScoreUnit(0, 1); // Izquierda abajo
			ScoreUnit d= core.getScoreUnit(1, 1); // Derecha abajo
			
			System.out.println("Playing all wires of block A:");
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a a.left:"+ a.getLeft().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a a.right:"+ a.getRight().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a a.up:"+ a.getUp().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a a.down:"+ a.getDown().play());
			
			System.out.println("Playing all wires of block B:");
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a b.left:"+ b.getLeft().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a b.right:"+ b.getRight().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a b.up:"+ b.getUp().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a b.down:"+ b.getDown().play());
			
			System.out.println("Playing all wires of block C:");
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a c.left:"+ c.getLeft().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a c.right:"+ c.getRight().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a c.up:"+ c.getUp().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a c.down:"+ c.getDown().play());

			System.out.println("Playing all wires of block D:");
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a d.left:"+ d.getLeft().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a d.right:"+ d.getRight().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a d.up:"+ d.getUp().play());
			System.out.println(core.getCurrentPlayer()+" está tratando de jugar a d.down:"+ d.getDown().play());

			if(core.isGameFinished()){
				System.out.println("*****************************************");
				System.out.println("* Juego finalizado, tabla de resultados *");
				System.out.println("*                                       *");
				for (Player player : players) {
					System.out.println("Puntos de ["+player+"]:"+core.getPlayerPoints(player));	
				}
				System.out.println("*****************************************");
			}else{
				System.out.println("El juego no terminó!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
