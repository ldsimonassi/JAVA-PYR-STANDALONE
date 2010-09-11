package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameCore;
import model.GameListener;
import model.Player;
import model.ScoreUnit;

public class GameControlPanel extends JPanel implements GameListener{

	private static final long serialVersionUID = -6989730117528957766L;

	private GameCore myCore;
	private JPanel mainPanel= new JPanel();
	private GameStatusPanel gameStatus;

	
	public GameControlPanel(GameCore core){
		this.myCore= core;
		core.addGameListener(this);
		gameStatus= new GameStatusPanel(myCore);
		initPanel();
	}
	
	private void initPanel() {
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);

		mainPanel.setLayout(new GridLayout(myCore.getScoreUnitsY(), myCore.getScoreUnitsX()));

		for (int y = 0; y < myCore.getScoreUnitsY(); y++) {
			for (int x = 0; x < myCore.getScoreUnitsX(); x++){
				ScoreUnit scoreUnit= myCore.getScoreUnit(x, y);
				ScoreUnitPanel panel= new ScoreUnitPanel(scoreUnit, this);
				panel.lblWinner.setText("["+x+","+y+"]");
				mainPanel.add(panel);
			}
		}
		
		this.add(gameStatus, BorderLayout.EAST);
		
		
	}

	/**
	 * Actualizar los parámetros del los componentes del panel
	 */
	public void gameStatusChanged(GameCore core) {
		
	}
	
	public static void main(String[] args) {
		HashSet<Player> players= new HashSet<Player>();
		players.add(new Player("Dario", Color.RED));
		players.add(new Player("Rolando", Color.BLUE));
		players.add(new Player("Juan", Color.YELLOW));
		GameCore core= new GameCore(2, 2, players);
		GameControlPanel pnl= new GameControlPanel(core);
		JFrame frm= new JFrame("Juego de Puntos y Rayas!");
		frm.getContentPane().add(pnl, BorderLayout.CENTER);
		frm.setSize(new Dimension(200, 100));
		frm.setExtendedState(frm.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frm.setVisible(true);
		frm.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
