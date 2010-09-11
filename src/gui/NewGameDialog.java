package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.GameCore;
import model.Player;

public class NewGameDialog extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	JTextField txtScoreUnitsX;
	JTextField txtScoreUnitsY;
	JButton addPlayerButton;
	JTextField txtName= new JTextField("");
	HashSet<Player> players= new HashSet<Player>();
	
	DefaultTableModel tableModelPlayers= new DefaultTableModel();
	JTable tablePlayers;
	Screen myScreen;
	
	public NewGameDialog(Screen s){
		myScreen= s;
		initPanel();
	}

	private void initPanel() {
		this.setSize(new Dimension(200, 150));
		this.setTitle("Nueva partida...");
		JPanel pnlGameData= new JPanel(new VerticalLayout());

		JPanel pnlX= new JPanel(new FlowLayout());
		txtScoreUnitsX= new JTextField("2");
		txtScoreUnitsX.setPreferredSize(new Dimension(50, txtScoreUnitsX.getMinimumSize().height));
		pnlX.add(new JLabel("Cuadriculas en X:"));
		pnlX.add(txtScoreUnitsX);

		JPanel pnlY= new JPanel(new FlowLayout());
		txtScoreUnitsY= new JTextField("2");
		txtScoreUnitsY.setPreferredSize(new Dimension(50, txtScoreUnitsX.getMinimumSize().height));
		pnlY.add(new JLabel("Cuadriculas en Y:"));
		pnlY.add(txtScoreUnitsY);

		pnlGameData.add(pnlX);
		pnlGameData.add(pnlY);

		tablePlayers= new JTable(tableModelPlayers);
		tableModelPlayers.addColumn("Nombre");
		tableModelPlayers.addColumn("Color");
		JPanel pnlPlayers= new JPanel(new BorderLayout());
		final JPanel pnlPlayersTop= new JPanel(new BorderLayout());
		pnlPlayers.add(pnlPlayersTop, BorderLayout.NORTH);
		pnlPlayers.add(new JScrollPane(tablePlayers), BorderLayout.CENTER);

		addPlayerButton= new JButton("Agregar el jugador");
		pnlPlayersTop.add(addPlayerButton, BorderLayout.SOUTH);
		
		final JPanel pnlName= new JPanel(new FlowLayout());
		pnlName.add(new JLabel("Nombre:"));
		//txtName.setPreferredSize(new Dimension(100, txtName.getPreferredSize().height));
		txtName.setColumns(5);
		pnlName.setBackground(Color.RED);
		pnlName.add(txtName);
		pnlPlayersTop.add(pnlName, BorderLayout.CENTER);

		JButton btColor= new JButton("Color...");
		pnlPlayersTop.add(btColor, BorderLayout.EAST);
		btColor.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Color c= JColorChooser.showDialog(NewGameDialog.this, "Elija el color del jugador", Color.BLUE);
				if(c!=null)
					pnlName.setBackground(c);

			}
		});
		
		
		pnlPlayers.setPreferredSize(new Dimension(200, 200));
		this.getContentPane().add(pnlPlayers, BorderLayout.EAST);
		this.getContentPane().add(pnlGameData, BorderLayout.CENTER);
		JButton btStartGame= new JButton("Iniciar jugada!");
		this.getContentPane().add(btStartGame, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		btStartGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					int x= Integer.valueOf(txtScoreUnitsX.getText());
					int y= Integer.valueOf(txtScoreUnitsY.getText());
				
					if(players.size() >=2 && 
					   x >= 1 && 
					   y >=1){
						GameCore core= new GameCore(x, y, players);
						System.out.println("Creando jugada ["+core+"+");
						myScreen.createNewGame(core);
						
						
						NewGameDialog.this.setVisible(false);
						myScreen.allGames.remove(NewGameDialog.this);
					}
					else{
						JOptionPane.showMessageDialog(NewGameDialog.this, "X e Y deben ser mayores que 0 y debe haber al menos 2 jugadores.");
					}
				} catch (NumberFormatException nfe){
					JOptionPane.showMessageDialog(NewGameDialog.this, "Los campos de X e Y deben ser numéricos.");
				}
			}
		});

		addPlayerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

				Player p= new Player(txtName.getText(), pnlName.getBackground());
				System.out.println("Agregando jugador: ["+p+"]");
				if(players.add(p)){
					tableModelPlayers.addRow(new Object[]{p, "R:"+p.getColor().getRed()+"G:"+p.getColor().getGreen()+"B:"+p.getColor().getBlue()});
					pnlName.setBackground(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
				}
				else{
					JOptionPane.showMessageDialog(NewGameDialog.this, "Ya existe un jugador de nombre ["+txtName.getText()+"]");					
				}
			}
		});

		this.setSize(new Dimension(400, 200));
		this.setVisible(true);		
		myScreen.allGames.add(this);
	}
	
	public static void main(String[] args) {
		new Screen();
/*		NewGameDialog newGame= new NewGameDialog();
		newGame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});*/
	}
}