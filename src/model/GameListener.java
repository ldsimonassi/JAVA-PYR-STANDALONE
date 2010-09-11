package model;

/**
 * Cualquier componente que desee estar al tanto del avance
 * del juego debe implementar esta interfaz y suscribirse al
 * juego que desee.
 * @author Dario
 */
public interface GameListener {
	/**
	 * Este m�todo es llamado cuando el estado del juego
	 * al que se est� suscripto haya cambiado.
	 * 
	 * @param core
	 */
	public void gameStatusChanged(GameCore core);
}
