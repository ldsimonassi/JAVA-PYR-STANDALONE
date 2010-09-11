package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.GameCore;
import model.GameListener;
import model.Player;

public class GameStatusPanel extends JPanel implements GameListener{

	private static final long serialVersionUID = 3612736022540232360L;
	DefaultTableModel modelPlayers= new DefaultTableModel();
	JTable tblPlayerPoints;
	JLabel lblGameStatus= new JLabel("Sin datos");
	JButton btAbandon= new JButton("Abandonar!");
	private GameCore core;

	public GameStatusPanel(GameCore core) {
		core.addGameListener(this);
		this.core= core;
		initPanel();
	}

	private void initPanel() {
		Dimension pref= this.getPreferredSize();
		this.setPreferredSize(new Dimension(350, pref.height));
		this.setLayout(new BorderLayout());
		lblGameStatus.setFont(lblGameStatus.getFont().deriveFont(20.0f));
		updateStatusColors();
		tblPlayerPoints= new JTable(modelPlayers);
		modelPlayers.addColumn("Jugador");
		modelPlayers.addColumn("Puntos");
		for (int i = 0; i < core.getPlayers().length; i++) {
			modelPlayers.addRow(new Object[]{core.getPlayers()[i], 0});
		}

		JScrollPane jsp= new JScrollPane(tblPlayerPoints);
		jsp.setPreferredSize(new Dimension(150, 1000));
		jsp.setMaximumSize(new Dimension(150, 1000));
		jsp.setMinimumSize(new Dimension(150, 1000));
		this.add(jsp, BorderLayout.CENTER);
		this.add(lblGameStatus, BorderLayout.NORTH);
		this.add(btAbandon, BorderLayout.SOUTH);
		btAbandon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!core.isGameFinished()){
					Player p= core.getCurrentPlayer();
					core.abandon(p);
				}
				else{
					JOptionPane.showMessageDialog(GameStatusPanel.this, "No se puede abandonar:\nLa partida ha finalizado!");
				}
			}
		});
		gameStatusChanged(core);
	}

	public void gameStatusChanged(GameCore core) {
		Player[] players= core.getPlayers();
		for (int i = 0; i < players.length; i++) {
			tblPlayerPoints.getModel().setValueAt(players[i], i, 0);
			tblPlayerPoints.getModel().setValueAt(core.isPlayerActive(players[i])?core.getPlayerPoints(players[i]):"Abandono con: "+core.getPlayerPoints(players[i]), i, 1);
		}

		updateStatusColors();
		
		
		if(core.isGameFinished()){
			lblGameStatus.setText("Juego finalizado!");
		}else{
			lblGameStatus.setText("Es el turno de:"+ core.getCurrentPlayer()+"!");
		}
		
	}

	public void updateStatusColors(){
		Color c= core.getCurrentPlayer().getColor();
		Color inverted= new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
		lblGameStatus.setForeground(c);
		lblGameStatus.setBackground(inverted);
	}
}
