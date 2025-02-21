package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Examen Formatif POO2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Modules");
		menuBar.add(mnNewMenu);
		
		JMenuItem MenuItemEmprunt = new JMenuItem("Emprunt");
		MenuItemEmprunt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmpruntView empruntView = new EmpruntView();
				setContentPane(empruntView);
				revalidate();
				repaint();
			}
		});
		mnNewMenu.add(MenuItemEmprunt);
		
		JMenuItem MenuItemMembre = new JMenuItem("Membre");
		mnNewMenu.add(MenuItemMembre);
		
		JMenuItem MenuItemLivre = new JMenuItem("Livre");
		MenuItemLivre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LivreView livreView = new LivreView();
				setContentPane(livreView);
			    revalidate();
			    repaint();
			}
		});
		mnNewMenu.add(MenuItemLivre);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
}
