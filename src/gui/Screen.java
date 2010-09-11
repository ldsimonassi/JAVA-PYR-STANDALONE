package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.GameCore;

/**
 * Clase principal del programa inicia la ventana y captura los eventos
 * @author dsimonassi
 *
 */
public class Screen extends JFrame{
	
	private static final long serialVersionUID = 1L;

	JDesktopPane allGames= new JDesktopPane();
	Screen(){
		this.setTitle("Juego de Puntos y Rayas!");
		this.setSize(new Dimension(800, 600));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		JMenuBar mb= new JMenuBar();
		JMenu mnuGame= new JMenu("Juego");
		mb.add(mnuGame);
		JMenuItem miNew= new JMenuItem("Nuevo...");
		mnuGame.add(miNew);
		JMenuItem miExit= new JMenuItem("Salir");
		mnuGame.add(miExit);
		
		miNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Creando dialogo");
				NewGameDialog dialog= new NewGameDialog(Screen.this);
				dialog.setVisible(true);
			}
		});
		
		miExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		this.getContentPane().add(mb, BorderLayout.NORTH);
		this.getContentPane().add(allGames, BorderLayout.CENTER);
		this.setVisible(true);
	}
	

	public void createNewGame(GameCore core) {
		JInternalFrame frm= new JInternalFrame("Juego de ["+core.getScoreUnitsX()+" x "+core.getScoreUnitsY()+"] entre ["+core.getPlayers().length+"]");
		frm.getContentPane().add(new GameControlPanel(core));
		frm.setSize(new Dimension(800, 600));
		frm.setVisible(true);
		frm.setResizable(true);
		frm.setMaximizable(true);
		frm.setClosable(true);

		allGames.add(frm);
		try{ frm.setMaximum(true);
		
		}catch(Exception e){
			
		}
	}

	public static void main(String[] args) {
		new Screen();
	}

}
