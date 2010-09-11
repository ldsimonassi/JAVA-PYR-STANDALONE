package model;

/**
 * Cualquier componente que desee estar al tanto del avance
 * del juego debe implementar esta interfaz y suscribirse al
 * juego que desee.
 * @author Dario
 */
public interface GameListener {
	/**
	 * Este método es llamado cuando el estado del juego
	 * al que se está suscripto haya cambiado.
	 * 
	 * @param core
	 */
	public void gameStatusChanged(GameCore core);
}
