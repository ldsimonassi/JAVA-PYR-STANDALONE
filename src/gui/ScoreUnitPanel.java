package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GameCore;
import model.GameListener;
import model.Player;
import model.ScoreUnit;

public class ScoreUnitPanel extends JPanel implements GameListener {
	private static final long serialVersionUID = 660729313996380368L;
	ScoreUnit myScoreUnit;

	JLabel lblWinner= new JLabel();
	
	JButton btRight= new JButton();
	JButton btLeft= new JButton();
	JButton btUp= new JButton();
	JButton btDown= new JButton();
	
	public ScoreUnitPanel(ScoreUnit scoreUnit, GameControlPanel gcp) {
		this.myScoreUnit= scoreUnit;
		this.myScoreUnit.getGameCore().addGameListener(this);
		initPanel();
	}

	static final int LINE_SIZE = 8;

	/**
	 * Realiza el setup inicial del panel gráfico que representa un scoreUnit
	 */
	private void initPanel() {
		btLeft.setRolloverEnabled(false);
		btRight.setRolloverEnabled(false);
		btUp.setRolloverEnabled(false);
		btDown.setRolloverEnabled(false);
		btLeft.setBorder(BorderFactory.createEmptyBorder());
		btRight.setBorder(BorderFactory.createEmptyBorder());
		btDown.setBorder(BorderFactory.createEmptyBorder());
		btUp.setBorder(BorderFactory.createEmptyBorder());
		
		btUp.setBackground(Color.lightGray);
		btDown.setBackground(Color.lightGray);
		btRight.setBackground(Color.lightGray);
		btLeft.setBackground(Color.lightGray);
		
		btLeft.setMaximumSize(new Dimension(LINE_SIZE, 10000));
		btLeft.setPreferredSize(new Dimension(LINE_SIZE, 10000));
		btRight.setMaximumSize(new Dimension(LINE_SIZE, 10000));
		btRight.setPreferredSize(new Dimension(LINE_SIZE, 10000));
		btUp.setMaximumSize(new Dimension(10000, LINE_SIZE));
		btUp.setPreferredSize(new Dimension(10000, LINE_SIZE));
		btDown.setMaximumSize(new Dimension(10000, LINE_SIZE));
		btDown.setPreferredSize(new Dimension(10000, LINE_SIZE));

		this.setLayout(new BorderLayout());
		this.add(lblWinner, BorderLayout.CENTER);
		
		this.add(btUp, BorderLayout.NORTH);
		this.add(btDown, BorderLayout.SOUTH);
		this.add(btLeft, BorderLayout.WEST);
		this.add(btRight, BorderLayout.EAST);
	
		gameStatusChanged(myScoreUnit.getGameCore());

		ActionListener action= new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//myScoreUnit.getDown().play()
				if(e.getSource()==btDown) myScoreUnit.getDown().play();
				if(e.getSource()==btUp) myScoreUnit.getUp().play();
				if(e.getSource()==btLeft) myScoreUnit.getLeft().play();
				if(e.getSource()==btRight) myScoreUnit.getRight().play();
			}
		};
		
		btDown.addActionListener(action);
		btUp.addActionListener(action);
		btLeft.addActionListener(action);
		btRight.addActionListener(action);
	}

	private void updateButton(JButton bt, Player p){ 
		if(p!=null){
			bt.getModel().setPressed(true);
			bt.getModel().setEnabled(false);
			bt.setBackground(p.getColor());
		}
		else{
			bt.getModel().setPressed(false);
			bt.getModel().setEnabled(true);	
		}
	}
	
	public void gameStatusChanged(GameCore core) {
		updateButton(btDown,  myScoreUnit.getDown().getPlayer());
		updateButton(btUp,    myScoreUnit.getUp().getPlayer());
		updateButton(btLeft,  myScoreUnit.getLeft().getPlayer());
		updateButton(btRight, myScoreUnit.getRight().getPlayer());
		Player winner= myScoreUnit.getWinner();
		if(winner!=null){
			lblWinner.setText(winner.toString());
			lblWinner.getParent().setBackground(winner.getColor());
		}
	}

	public static void main(String[] args) throws Exception{
		HashSet<Player> set= new HashSet<Player>();
		set.add(new Player("Dario", Color.RED));
		set.add(new Player("Rolando", Color.BLUE));
		set.add(new Player("Juan", Color.YELLOW));
		GameCore core= new GameCore(2, 2, set);
		ScoreUnit unit= core.getScoreUnit(0, 0);
		unit.getDown().play();
		GameControlPanel gcp= new GameControlPanel(core);
		ScoreUnitPanel pnl= new ScoreUnitPanel(unit, gcp); 
		JFrame frm= new JFrame("ScoreUnitPanel Test");
		frm.getContentPane().add(pnl, BorderLayout.CENTER);
		frm.setSize(new Dimension(200, 100));
		frm.setVisible(true);
		frm.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
